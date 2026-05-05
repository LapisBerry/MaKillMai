package com.lapisberry.game.controllers;

import com.lapisberry.game.entities.characters.BartCassidy;
import com.lapisberry.game.entities.characters.CharacterEnum;
import com.lapisberry.game.entities.characters.KitCarlson;
import com.lapisberry.game.entities.characters.PedroRamirez;
import com.lapisberry.game.entities.characters.SidKetchum;
import com.lapisberry.game.entities.characters.SlabTheKiller;
import com.lapisberry.net.packets.DamageResponsePacket;
import com.lapisberry.game.entities.dice.Die;
import com.lapisberry.game.entities.dice.DieFace;
import com.lapisberry.game.entities.dice.DiePool;
import com.lapisberry.game.entities.players.Player;
import com.lapisberry.game.entities.players.Role;
import com.lapisberry.utils.Config;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
 *               (auto: stone resolution, rot resolution, Suzy hook)
 *                                        │
 *                ┌───────────────────────┴─────────┐
 *                ▼                                 ▼
 *   AWAITING_PURE_MAGIC                       RESOLVING
 *  (≥ threshold PURE_MAGIC dice)         (attacks / heals one by one)
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
        AWAITING_SID_HEAL,
        ROLLING,
        AWAITING_PURE_MAGIC,
        RESOLVING,
        AWAITING_DAMAGE_RESPONSE,
        AWAITING_KIT_DISCARD,
        TURN_OVER,
        GAME_OVER
    }

    /** Snapshot of an in-flight ATTACK that paused for a Bart/Pedro decision. */
    public static final class PendingDamage implements Serializable {
        @Serial private static final long serialVersionUID = 1L;
        public final int targetClientId;
        public final int sourceClientId;
        public final int amount;
        public final boolean canTakeRot;     // Bart and rotPool > 0
        public final boolean canDiscardRot;  // Pedro and target.rot > 0

        public PendingDamage(int targetClientId, int sourceClientId, int amount,
                             boolean canTakeRot, boolean canDiscardRot) {
            this.targetClientId = targetClientId;
            this.sourceClientId = sourceClientId;
            this.amount = amount;
            this.canTakeRot = canTakeRot;
            this.canDiscardRot = canDiscardRot;
        }
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
    private PendingDamage pendingDamage; // non-null only during AWAITING_DAMAGE_RESPONSE

    // Lifecycle ---------------------------------------------------------------

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
        rollsLeft = currentPlayer().getCharacter().getRollPerTurn();
        recentlyDeadClientIds.clear();
        currentPlayer().getCharacter().onTurnStart(this, currentPlayer());
        if (currentPlayer().getCharacter() instanceof SidKetchum) {
            phase = Phase.AWAITING_SID_HEAL;
        } else {
            phase = Phase.ROLLING;
        }
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

        Player current = currentPlayer();
        switch (die.getDieFace()) {
            case ATTACK_1, ATTACK_2 -> {
                int distance = computeMinDistance(current, target);
                if (distance < 0 || !current.getCharacter().canHitAtDistance(die.getDieFace(), distance)) {
                    return false;
                }
                die.setResolved(true);
                if (!dealAttackDamage(target, current, 1)) {
                    return true; // suspended for Bart/Pedro response
                }
            }
            case HEALTH_POTION -> {
                int amount = target.getCharacter().onHealReceived(target == current, 1);
                target.getCharacter().heal(amount);
                die.setResolved(true);
                if (current.getCharacter() instanceof KitCarlson) {
                    phase = Phase.AWAITING_KIT_DISCARD;
                    return true;
                }
            }
            default -> {
                return false; // ROT_POWER / PURE_MAGIC / STONE_SUPPRESSOR are auto-resolved
            }
        }

        afterMutation();
        return true;
    }

    public synchronized boolean handleUsePureMagic(int clientId, boolean accept) {
        if (!isCurrentPlayer(clientId) || phase != Phase.AWAITING_PURE_MAGIC) return false;

        Player current = currentPlayer();
        if (accept) {
            current.getCharacter().clearRot();
            for (Player p : players) {
                if (p == current || !p.isAlive()) continue;
                int dmg = p.getCharacter().onPureMagicDamage(1);
                if (dmg > 0) {
                    p.getCharacter().takeDamage(dmg);
                    checkDeath(p);
                }
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

    /**
     * Kit Carlson follow-up after resolving a HEALTH_POTION: optionally remove
     * 1 rot from any alive player (returning it to the pool), or skip.
     */
    public synchronized boolean handleKitDiscard(int clientId, int targetClientId, boolean skip) {
        if (!isCurrentPlayer(clientId) || phase != Phase.AWAITING_KIT_DISCARD) return false;
        if (!(currentPlayer().getCharacter() instanceof KitCarlson)) return false;

        if (!skip) {
            Player target = findPlayerById(targetClientId);
            if (target == null || !target.isAlive()) return false;
            if (target.getCharacter().getRotPower() <= 0) return false;
            target.getCharacter().addRot(-1);
            rotPool = Math.min(Config.ROT_POOL_SIZE, rotPool + 1);
        }
        // Return to RESOLVING (or TURN_OVER if nothing left).
        phase = hasAnyUnresolvedTargetableDie() ? Phase.RESOLVING : Phase.TURN_OVER;
        return true;
    }

    /** Sid Ketchum start-of-turn heal: pick any alive player to heal +1 HP, or skip. */
    public synchronized boolean handleSidHeal(int clientId, int targetClientId, boolean skip) {
        if (!isCurrentPlayer(clientId) || phase != Phase.AWAITING_SID_HEAL) return false;
        if (!(currentPlayer().getCharacter() instanceof SidKetchum)) return false;

        if (!skip) {
            Player target = findPlayerById(targetClientId);
            if (target == null || !target.isAlive()) return false;
            target.getCharacter().heal(1);
        }
        phase = Phase.ROLLING;
        return true;
    }

    /**
     * Slab the Killer combo: pair an unresolved HEALTH_POTION die with an
     * unresolved ATTACK_1/ATTACK_2 die to deal 2 damage to a single target.
     * Both dice are consumed; the heal effect is replaced.
     */
    public synchronized boolean handleUseSlabAbility(int clientId, int beerDieIndex,
                                                     int attackDieIndex, int targetClientId) {
        if (!isCurrentPlayer(clientId) || phase != Phase.RESOLVING) return false;
        if (beerDieIndex == attackDieIndex) return false;
        if (beerDieIndex < 0 || beerDieIndex >= diePool.getDice().size()) return false;
        if (attackDieIndex < 0 || attackDieIndex >= diePool.getDice().size()) return false;

        Player current = currentPlayer();
        if (!(current.getCharacter() instanceof SlabTheKiller slab)) return false;
        if (slab.isAbilityUsedThisTurn()) return false;

        Die beerDie = diePool.getDice().get(beerDieIndex);
        Die attackDie = diePool.getDice().get(attackDieIndex);
        if (beerDie.isResolved() || attackDie.isResolved()) return false;
        if (beerDie.getDieFace() != DieFace.HEALTH_POTION) return false;
        if (attackDie.getDieFace() != DieFace.ATTACK_1 && attackDie.getDieFace() != DieFace.ATTACK_2) return false;

        Player target = findPlayerById(targetClientId);
        if (target == null || !target.isAlive() || target == current) return false;
        int distance = computeMinDistance(current, target);
        if (distance <= 0 || !current.getCharacter().canHitAtDistance(attackDie.getDieFace(), distance)) {
            return false;
        }

        slab.markAbilityUsed();
        beerDie.setResolved(true);
        attackDie.setResolved(true);
        if (!dealAttackDamage(target, current, 2)) {
            return true; // suspended for Bart/Pedro response
        }

        afterMutation();
        return true;
    }

    // Phase transitions -------------------------------------------------------

    private void enterResolutionPhase() {
        // STONE_SUPPRESSOR: 3+ ends rolling (already happened via this code path).
        // Per the rules there's no damage from stones themselves; just mark them resolved.
        for (Die d : diePool.getDice()) {
            if (d.getDieFace() == DieFace.STONE_SUPPRESSOR) d.setResolved(true);
        }

        resolveAllRotPower();
        if (phase == Phase.GAME_OVER) return;
        if (!currentPlayer().isAlive()) {
            advanceTurn();
            return;
        }

        // Suzy Lafayette / future end-of-rolling hooks.
        currentPlayer().getCharacter().onAfterRollingPhase(this, currentPlayer());

        transitionAfterAutoEffects();
    }

    private void transitionAfterAutoEffects() {
        if (phase == Phase.GAME_OVER) return;

        int unresolvedPureMagic = countUnresolvedFace(DieFace.PURE_MAGIC);
        if (unresolvedPureMagic >= currentPlayer().getCharacter().getDiceRequiredForPureMagic()) {
            phase = Phase.AWAITING_PURE_MAGIC;
            return;
        }
        // Below threshold: silently consume PURE_MAGIC dice.
        for (Die d : diePool.getDice()) {
            if (!d.isResolved() && d.getDieFace() == DieFace.PURE_MAGIC) d.setResolved(true);
        }

        phase = hasAnyUnresolvedTargetableDie() ? Phase.RESOLVING : Phase.TURN_OVER;
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
        boolean rerollable = currentPlayer().getCharacter().canRerollStoneSuppressor();
        for (Die d : diePool.getDice()) {
            if (d.getDieFace() == DieFace.STONE_SUPPRESSOR) {
                d.setUnlockable(rerollable);
                d.setLocked(true);
            }
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
        for (Player p : players) {
            if (!p.isAlive()) continue;
            int rot = p.getCharacter().getRotPower();
            p.getCharacter().clearRot();
            int damage = p.getCharacter().onRotAttackDamage(rot);
            if (damage > 0) {
                p.getCharacter().takeDamage(damage);
                checkDeath(p);
            }
        }
        rotPool = Config.ROT_POOL_SIZE;
        checkWinCondition();
    }

    /**
     * Apply damage from an ATTACK_1 / ATTACK_2 die. Returns {@code false} when
     * the event is suspended for a Bart/Pedro response — callers must skip
     * their post-damage bookkeeping in that case (the response handler will
     * resume it). Returns {@code true} when damage was applied immediately.
     */
    private boolean dealAttackDamage(Player target, Player source, int amount) {
        boolean targetIsBart = target.getCharacter() instanceof BartCassidy;
        boolean targetIsPedro = target.getCharacter() instanceof PedroRamirez;
        boolean canTakeRot = targetIsBart && rotPool > 0;
        boolean canDiscardRot = targetIsPedro && target.getCharacter().getRotPower() > 0;
        if (canTakeRot || canDiscardRot) {
            pendingDamage = new PendingDamage(target.getClientId(), source.getClientId(),
                    amount, canTakeRot, canDiscardRot);
            phase = Phase.AWAITING_DAMAGE_RESPONSE;
            return false;
        }
        applyAttackDamageImmediate(target, source, amount);
        return true;
    }

    private void applyAttackDamageImmediate(Player target, Player source, int amount) {
        target.getCharacter().takeDamage(amount);
        checkDeath(target);
        if (target.isAlive() && source != null) {
            target.getCharacter().onTakeAttackDamage(this, target, source);
        }
    }

    /**
     * Bart Cassidy / Pedro Ramirez decide how to respond to an ATTACK that
     * paused mid-flight. Only the targeted player can call this.
     */
    public synchronized boolean handleDamageResponse(int clientId, DamageResponsePacket.Choice choice) {
        if (phase != Phase.AWAITING_DAMAGE_RESPONSE || pendingDamage == null) return false;
        if (clientId != pendingDamage.targetClientId) return false;

        Player target = findPlayerById(pendingDamage.targetClientId);
        Player source = findPlayerById(pendingDamage.sourceClientId);
        if (target == null) return false;

        PendingDamage pd = pendingDamage;
        pendingDamage = null;
        phase = Phase.RESOLVING; // restore base phase before mutating

        switch (choice) {
            case TAKE_ROT -> {
                if (!pd.canTakeRot || rotPool <= 0) {
                    applyAttackDamageImmediate(target, source, pd.amount);
                } else {
                    target.getCharacter().addRot(1);
                    rotPool--;
                    if (rotPool == 0) {
                        triggerRotAttack();
                    }
                }
            }
            case DISCARD_ROT -> {
                if (!pd.canDiscardRot || target.getCharacter().getRotPower() <= 0) {
                    applyAttackDamageImmediate(target, source, pd.amount);
                } else {
                    target.getCharacter().addRot(-1);
                    rotPool = Math.min(Config.ROT_POOL_SIZE, rotPool + 1);
                }
            }
            case ACCEPT -> applyAttackDamageImmediate(target, source, pd.amount);
        }

        afterMutation();
        return true;
    }

    private void checkDeath(Player p) {
        if (p.getCharacter().isAlive() || p.isRoleRevealed()) {
            return;
        }
        p.setRoleRevealed(true);
        recentlyDeadClientIds.add(p.getClientId());
        // Fire onOtherPlayerEliminated for everyone else.
        for (Player other : players) {
            if (other == p || !other.isAlive()) continue;
            other.getCharacter().onOtherPlayerEliminated(this, other, p);
        }
    }

    /** Public so character hooks can grant rot back to the field/players. */
    public synchronized void giveRot(Player target, int amount) {
        if (target == null || amount <= 0) return;
        for (int i = 0; i < amount && rotPool > 0; i++) {
            target.getCharacter().addRot(1);
            rotPool--;
            if (rotPool == 0) {
                triggerRotAttack();
                if (!target.isAlive()) return;
            }
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

    /**
     * Returns the minimum distance from {@code from} to {@code to} when
     * walking the table over only living players. Returns -1 if either
     * player isn't alive, 0 if they are the same player.
     */
    public synchronized int computeMinDistance(Player from, Player to) {
        if (from == to) return 0;
        List<Player> alive = livingPlayersByPosition();
        int fromIdx = alive.indexOf(from);
        int toIdx = alive.indexOf(to);
        if (fromIdx < 0 || toIdx < 0) return -1;
        int n = alive.size();
        int cw = (toIdx - fromIdx + n) % n;
        int ccw = (fromIdx - toIdx + n) % n;
        return Math.min(cw, ccw);
    }

    /** Computes valid target client IDs per unresolved die — used by the packet builder. */
    public synchronized Map<Integer, ArrayList<Integer>> computeValidTargetIdsByDieIndex() {
        Map<Integer, ArrayList<Integer>> result = new HashMap<>();
        if (phase != Phase.RESOLVING) return result;
        Player current = currentPlayer();
        if (current == null) return result;

        for (int i = 0; i < diePool.getDice().size(); i++) {
            Die d = diePool.getDice().get(i);
            if (d.isResolved()) continue;
            DieFace face = d.getDieFace();
            ArrayList<Integer> ids = new ArrayList<>();
            if (face == DieFace.HEALTH_POTION) {
                for (Player p : players) {
                    if (p.isAlive()) ids.add(p.getClientId());
                }
            } else if (face == DieFace.ATTACK_1 || face == DieFace.ATTACK_2) {
                Set<Integer> seen = new HashSet<>();
                for (Player p : players) {
                    if (!p.isAlive() || p == current) continue;
                    int distance = computeMinDistance(current, p);
                    if (distance > 0 && current.getCharacter().canHitAtDistance(face, distance)
                            && seen.add(p.getClientId())) {
                        ids.add(p.getClientId());
                    }
                }
            } else {
                continue;
            }
            result.put(i, ids);
        }
        return result;
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

    public PendingDamage getPendingDamage() {
        return pendingDamage;
    }

    public Role getWinningRole() {
        return winningRole;
    }

    public boolean isGameOver() {
        return phase == Phase.GAME_OVER;
    }
}
