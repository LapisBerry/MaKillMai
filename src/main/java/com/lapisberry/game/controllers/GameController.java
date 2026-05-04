package com.lapisberry.game.controllers;

import com.lapisberry.game.entities.characters.CharacterEnum;
import com.lapisberry.game.entities.dice.Die;
import com.lapisberry.game.entities.dice.DieFace;
import com.lapisberry.game.entities.dice.DiePool;
import com.lapisberry.game.entities.players.Player;
import com.lapisberry.game.entities.players.Role;
import com.lapisberry.utils.Config;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Authoritative server-side game state and rules engine. The instance is
 * mutated only on the server; clients receive {@code GameStatePacket}
 * snapshots and never call these methods directly.
 *
 * <p>Turn lifecycle:
 * <pre>
 *   ROLLING ── roll/lock up to 3 times ──┐
 *                                        ▼
 *               (auto: stone damage, rot resolution)
 *                                        │
 *                ┌───────────────────────┴─────────┐
 *                ▼                                 ▼
 *   AWAITING_PURE_MAGIC                       RESOLVING
 *  (3+ PURE_MAGIC dice)                (attacks / heals one by one)
 *                │                                 │
 *                └─────────────► RESOLVING ────────┘
 *                                        │
 *                                        ▼
 *                                    TURN_OVER ──► next turn
 * </pre>
 */
public class GameController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum Phase {
        WAITING_FOR_START,
        ROLLING,
        AWAITING_PURE_MAGIC,
        RESOLVING,
        TURN_OVER,
        GAME_OVER
    }

    // Fields
    private final ArrayList<Player> players = new ArrayList<>(); // turn order
    private final DiePool diePool = new DiePool();
    private int currentTurnIndex = -1;
    private int rollsLeft;
    private int rotPool = Config.ROT_POOL_SIZE;
    private Phase phase = Phase.WAITING_FOR_START;
    private Role winningRole; // null until GAME_OVER
    private final Set<Integer> recentlyDeadClientIds = new HashSet<>();

    // Lifecycle ---------------------------------------------------------------

    /**
     * Begins the game with the lobby's shuffled players + characters. Sets
     * each player's seat and assigns the corresponding character. The Emperor
     * goes first; turn order then follows the {@code shuffledPlayers} list.
     */
    public synchronized void startGame(List<Player> shuffledPlayers,
                                       List<CharacterEnum> shuffledCharacters) {
        players.clear();
        players.addAll(shuffledPlayers);
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            p.setPosition(i);
            p.setCharacter(shuffledCharacters.get(i).createCharacter());
            if (p.getRole() == Role.EMPEROR) {
                p.getCharacter().increaseMaxHp(Config.EMPEROR_HP_BONUS);
                p.setRoleRevealed(true); // Emperor identity is public
            }
        }

        currentTurnIndex = findEmperorIndex();
        rotPool = Config.ROT_POOL_SIZE;
        winningRole = null;
        beginCurrentPlayerTurn();
    }

    private int findEmperorIndex() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getRole() == Role.EMPEROR) return i;
        }
        return 0;
    }

    private void beginCurrentPlayerTurn() {
        diePool.resetForNewTurn();
        rollsLeft = Config.DEFAULT_ROLL_PER_TURN;
        recentlyDeadClientIds.clear();
        phase = Phase.ROLLING;
    }

    private void advanceTurn() {
        if (phase == Phase.GAME_OVER) return;
        for (int step = 1; step <= players.size(); step++) {
            int next = (currentTurnIndex + step) % players.size();
            if (players.get(next).isAlive()) {
                currentTurnIndex = next;
                beginCurrentPlayerTurn();
                return;
            }
        }
        // Nobody alive (shouldn't happen) — game over fallback.
        phase = Phase.GAME_OVER;
    }

    // Action handlers ---------------------------------------------------------

    public synchronized boolean handleRoll(int clientId) {
        if (!isCurrentPlayer(clientId) || phase != Phase.ROLLING) return false;
        if (rollsLeft <= 0) return false;

        diePool.rollAllUnlockedDice();
        autoLockStoneSuppressors();
        rollsLeft--;

        int stones = diePool.countFace(DieFace.STONE_SUPPRESSOR);
        if (stones >= Config.DEFAULT_DICE_REQUIRED_FOR_STONE_END_TURN || rollsLeft == 0) {
            enterResolutionPhase();
        }
        return true;
    }

    public synchronized boolean handleToggleLock(int clientId, int dieIndex) {
        if (!isCurrentPlayer(clientId) || phase != Phase.ROLLING) return false;
        if (dieIndex < 0 || dieIndex >= diePool.getDice().size()) return false;
        Die die = diePool.getDice().get(dieIndex);
        if (!die.isUnlockable()) return false;
        die.setLocked(!die.isLocked());
        return true;
    }

    public synchronized boolean handleEndRolling(int clientId) {
        if (!isCurrentPlayer(clientId) || phase != Phase.ROLLING) return false;
        rollsLeft = 0;
        enterResolutionPhase();
        return true;
    }

    public synchronized boolean handleResolveDie(int clientId, int dieIndex, int targetClientId) {
        if (!isCurrentPlayer(clientId) || phase != Phase.RESOLVING) return false;
        if (dieIndex < 0 || dieIndex >= diePool.getDice().size()) return false;

        Die die = diePool.getDice().get(dieIndex);
        if (die.isResolved()) return false;

        Player target = findPlayerById(targetClientId);
        if (target == null || !target.isAlive()) return false;

        switch (die.getDieFace()) {
            case ATTACK_1 -> {
                if (!isAttackTargetValid(target, 1)) return false;
                dealDamage(target, 1);
            }
            case ATTACK_2 -> {
                if (!isAttackTargetValid(target, 2)) return false;
                dealDamage(target, 1);
            }
            case HEALTH_POTION -> target.getCharacter().heal(1);
            default -> {
                return false; // ROT_POWER / PURE_MAGIC / STONE_SUPPRESSOR are auto-resolved
            }
        }

        die.setResolved(true);
        afterMutation();
        return true;
    }

    private void dealDamage(Player target, int amount) {
        target.getCharacter().takeDamage(amount);
        checkDeath(target);
    }

    public synchronized boolean handleUsePureMagic(int clientId, boolean accept) {
        if (!isCurrentPlayer(clientId) || phase != Phase.AWAITING_PURE_MAGIC) return false;

        Player current = currentPlayer();
        if (accept) {
            current.getCharacter().clearRot();
            for (Player p : players) {
                if (p == current || !p.isAlive()) continue;
                dealDamage(p, 1);
            }
        }
        // Mark all PURE_MAGIC dice resolved either way.
        for (Die d : diePool.getDice()) {
            if (d.getDieFace() == DieFace.PURE_MAGIC) d.setResolved(true);
        }

        checkWinCondition();
        if (phase == Phase.GAME_OVER) return true;
        if (!current.isAlive()) {
            advanceTurn();
            return true;
        }
        phase = hasAnyUnresolvedTargetableDie() ? Phase.RESOLVING : Phase.TURN_OVER;
        return true;
    }

    public synchronized boolean handleEndTurn(int clientId) {
        if (!isCurrentPlayer(clientId)) return false;
        if (phase != Phase.TURN_OVER) return false;
        advanceTurn();
        return true;
    }

    // Phase transitions -------------------------------------------------------

    private void enterResolutionPhase() {
        applyStoneSuppressorDamage();
        if (!currentPlayer().isAlive()) {
            // Current player KO'd by their own dynamites - end their turn.
            checkWinCondition();
            if (phase != Phase.GAME_OVER) advanceTurn();
            return;
        }
        resolveAllRotPower();
        if (!currentPlayer().isAlive() || phase == Phase.GAME_OVER) {
            if (phase != Phase.GAME_OVER) advanceTurn();
            return;
        }
        transitionAfterAutoEffects();
    }

    private void transitionAfterAutoEffects() {
        if (phase == Phase.GAME_OVER) return;

        int unresolvedPureMagic = countUnresolvedFace(DieFace.PURE_MAGIC);
        if (unresolvedPureMagic >= currentPlayer().getCharacter().getDiceRequiredForPureMagic()) {
            phase = Phase.AWAITING_PURE_MAGIC;
            return;
        }
        // PURE_MAGIC below threshold: no effect, mark resolved.
        for (Die d : diePool.getDice()) {
            if (!d.isResolved() && d.getDieFace() == DieFace.PURE_MAGIC) d.setResolved(true);
        }

        if (hasAnyUnresolvedTargetableDie()) {
            phase = Phase.RESOLVING;
        } else {
            phase = Phase.TURN_OVER;
        }
    }

    private void afterMutation() {
        checkWinCondition();
        if (phase == Phase.GAME_OVER) return;

        if (!currentPlayer().isAlive()) {
            advanceTurn();
            return;
        }

        if (phase == Phase.RESOLVING && !hasAnyUnresolvedTargetableDie()) {
            phase = Phase.TURN_OVER;
        }
    }

    // Auto-effects ------------------------------------------------------------

    private void autoLockStoneSuppressors() {
        for (Die d : diePool.getDice()) {
            if (d.getDieFace() == DieFace.STONE_SUPPRESSOR) {
                d.setUnlockable(false);
                d.setLocked(true);
            }
        }
    }

    private void applyStoneSuppressorDamage() {
        int stones = diePool.countFace(DieFace.STONE_SUPPRESSOR);
        // Mark every STONE_SUPPRESSOR resolved regardless of count.
        for (Die d : diePool.getDice()) {
            if (d.getDieFace() == DieFace.STONE_SUPPRESSOR) d.setResolved(true);
        }
        if (stones >= Config.DEFAULT_DICE_REQUIRED_FOR_STONE_END_TURN) {
            currentPlayer().getCharacter().takeDamage(1);
            checkDeath(currentPlayer());
            checkWinCondition();
        }
    }

    private void resolveAllRotPower() {
        for (Die d : diePool.getDice()) {
            if (d.isResolved() || d.getDieFace() != DieFace.ROT_POWER) continue;
            d.setResolved(true);
            currentPlayer().getCharacter().addRot(1);
            rotPool = Math.max(0, rotPool - 1);
            if (rotPool == 0) {
                triggerRotAttack();
                if (phase == Phase.GAME_OVER || !currentPlayer().isAlive()) return;
            }
        }
    }

    private void triggerRotAttack() {
        // Snapshot rot per player, clear, then deal damage.
        for (Player p : players) {
            if (!p.isAlive()) continue;
            int rot = p.getCharacter().getRotPower();
            p.getCharacter().clearRot();
            if (rot > 0) {
                p.getCharacter().takeDamage(rot);
                checkDeath(p);
            }
        }
        rotPool = Config.ROT_POOL_SIZE;
        checkWinCondition();
    }

    private void checkDeath(Player p) {
        if (!p.getCharacter().isAlive() && !p.isRoleRevealed()) {
            p.setRoleRevealed(true);
            recentlyDeadClientIds.add(p.getClientId());
        }
    }

    // Win condition -----------------------------------------------------------

    private void checkWinCondition() {
        if (phase == Phase.GAME_OVER) return;

        Player emperor = findEmperor();
        boolean emperorAlive = emperor != null && emperor.isAlive();

        long aliveRebelOrSpy = players.stream()
                .filter(p -> p.isAlive() && (p.getRole() == Role.REBEL || p.getRole() == Role.SPY))
                .count();
        long aliveTotal = players.stream().filter(Player::isAlive).count();

        if (emperorAlive && aliveRebelOrSpy == 0) {
            winningRole = Role.EMPEROR;
            phase = Phase.GAME_OVER;
            return;
        }
        if (!emperorAlive) {
            // If exactly one player alive and that player is a Spy, Spy wins.
            if (aliveTotal == 1) {
                Player lone = players.stream().filter(Player::isAlive).findFirst().orElse(null);
                if (lone != null && lone.getRole() == Role.SPY) {
                    winningRole = Role.SPY;
                    phase = Phase.GAME_OVER;
                    return;
                }
            }
            winningRole = Role.REBEL;
            phase = Phase.GAME_OVER;
        }
    }

    private Player findEmperor() {
        for (Player p : players) {
            if (p.getRole() == Role.EMPEROR) return p;
        }
        return null;
    }

    // Targeting ---------------------------------------------------------------

    public synchronized boolean isAttackTargetValid(Player target, int distance) {
        if (target == null || !target.isAlive()) return false;
        return getAttackTargetIds(distance).contains(target.getClientId());
    }

    /**
     * Returns the client IDs of valid targets at the given distance from the
     * current player, walking only over living players.
     */
    public synchronized List<Integer> getAttackTargetIds(int distance) {
        List<Player> alive = livingPlayersByPosition();
        int n = alive.size();
        if (n <= distance) return Collections.emptyList();
        int curIdx = alive.indexOf(currentPlayer());
        if (curIdx < 0) return Collections.emptyList();
        Set<Integer> ids = new HashSet<>();
        ids.add(alive.get((curIdx + distance) % n).getClientId());
        ids.add(alive.get(((curIdx - distance) % n + n) % n).getClientId());
        return new ArrayList<>(ids);
    }

    public synchronized List<Integer> getHealTargetIds() {
        List<Integer> ids = new ArrayList<>();
        for (Player p : players) {
            if (p.isAlive()) ids.add(p.getClientId());
        }
        return ids;
    }

    private List<Player> livingPlayersByPosition() {
        List<Player> alive = new ArrayList<>();
        for (Player p : players) if (p.isAlive()) alive.add(p);
        alive.sort((a, b) -> Integer.compare(a.getPosition(), b.getPosition()));
        return alive;
    }

    // Helpers -----------------------------------------------------------------

    private boolean isCurrentPlayer(int clientId) {
        return phase != Phase.GAME_OVER
                && phase != Phase.WAITING_FOR_START
                && currentTurnIndex >= 0
                && currentTurnIndex < players.size()
                && players.get(currentTurnIndex).getClientId() == clientId;
    }

    public Player currentPlayer() {
        if (currentTurnIndex < 0 || currentTurnIndex >= players.size()) return null;
        return players.get(currentTurnIndex);
    }

    private Player findPlayerById(int clientId) {
        for (Player p : players) if (p.getClientId() == clientId) return p;
        return null;
    }

    private int countUnresolvedFace(DieFace face) {
        return diePool.countUnresolvedFace(face);
    }

    private boolean hasAnyUnresolvedTargetableDie() {
        for (Die d : diePool.getDice()) {
            if (d.isResolved()) continue;
            DieFace f = d.getDieFace();
            if (f == DieFace.ATTACK_1 || f == DieFace.ATTACK_2 || f == DieFace.HEALTH_POTION) {
                return true;
            }
        }
        return false;
    }

    // Getters -----------------------------------------------------------------

    public List<Player> getPlayers() {
        return players;
    }

    public DiePool getDiePool() {
        return diePool;
    }

    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }

    public int getCurrentTurnClientId() {
        Player p = currentPlayer();
        return p == null ? -1 : p.getClientId();
    }

    public int getRollsLeft() {
        return rollsLeft;
    }

    public int getRotPool() {
        return rotPool;
    }

    public Phase getPhase() {
        return phase;
    }

    public Role getWinningRole() {
        return winningRole;
    }

    public boolean isGameOver() {
        return phase == Phase.GAME_OVER;
    }
}
