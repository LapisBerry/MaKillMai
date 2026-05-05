package com.lapisberry.net.packets;

import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.entities.characters.SlabTheKiller;
import com.lapisberry.game.entities.dice.Die;
import com.lapisberry.game.entities.dice.DieFace;
import com.lapisberry.game.entities.players.Player;
import com.lapisberry.game.entities.players.Role;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Per-recipient snapshot of the live game. Built by the server with
 * {@link #from(GameController, int)} so each player's role is hidden from
 * everyone except the player themselves, the public Emperor, and players
 * whose role has already been revealed (by death). Carries the current
 * player's dice tray so observers can watch the rolls.
 */
public class GameStatePacket extends ServerPacket {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final class PlayerView implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        public final int clientId;
        public final int position;
        public final String playerName;
        public final String characterName;
        public final String characterAbility;
        public final int hp;
        public final int maxHp;
        public final int rotPower;
        public final Role role; // null when hidden
        public final boolean alive;
        public final boolean roleRevealed;

        public PlayerView(int clientId, int position, String playerName,
                          String characterName, String characterAbility,
                          int hp, int maxHp, int rotPower,
                          Role role, boolean alive, boolean roleRevealed) {
            this.clientId = clientId;
            this.position = position;
            this.playerName = playerName;
            this.characterName = characterName;
            this.characterAbility = characterAbility;
            this.hp = hp;
            this.maxHp = maxHp;
            this.rotPower = rotPower;
            this.role = role;
            this.alive = alive;
            this.roleRevealed = roleRevealed;
        }
    }

    public static final class DieView implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        public final DieFace face;
        public final boolean locked;
        public final boolean unlockable;
        public final boolean resolved;

        public DieView(DieFace face, boolean locked, boolean unlockable, boolean resolved) {
            this.face = face;
            this.locked = locked;
            this.unlockable = unlockable;
            this.resolved = resolved;
        }
    }

    // Fields
    private final ArrayList<PlayerView> players;
    private final ArrayList<DieView> dice;
    private final int recipientClientId;
    private final int currentTurnClientId;
    private final int rollsLeft;
    private final int rotPool;
    private final GameController.Phase phase;
    private final Role winningRole; // non-null when phase == GAME_OVER
    private final HashMap<Integer, ArrayList<Integer>> validTargetsByDieIndex;
    private final boolean slabAbilityAvailable;
    private final int pendingDamageTargetClientId; // -1 when none pending
    private final int pendingDamageSourceClientId;
    private final int pendingDamageAmount;
    private final boolean pendingDamageCanTakeRot;
    private final boolean pendingDamageCanDiscardRot;

    public GameStatePacket(ArrayList<PlayerView> players,
                           ArrayList<DieView> dice,
                           int recipientClientId,
                           int currentTurnClientId,
                           int rollsLeft,
                           int rotPool,
                           GameController.Phase phase,
                           Role winningRole,
                           HashMap<Integer, ArrayList<Integer>> validTargetsByDieIndex,
                           boolean slabAbilityAvailable,
                           int pendingDamageTargetClientId,
                           int pendingDamageSourceClientId,
                           int pendingDamageAmount,
                           boolean pendingDamageCanTakeRot,
                           boolean pendingDamageCanDiscardRot) {
        this.players = players;
        this.dice = dice;
        this.recipientClientId = recipientClientId;
        this.currentTurnClientId = currentTurnClientId;
        this.rollsLeft = rollsLeft;
        this.rotPool = rotPool;
        this.phase = phase;
        this.winningRole = winningRole;
        this.validTargetsByDieIndex = validTargetsByDieIndex;
        this.slabAbilityAvailable = slabAbilityAvailable;
        this.pendingDamageTargetClientId = pendingDamageTargetClientId;
        this.pendingDamageSourceClientId = pendingDamageSourceClientId;
        this.pendingDamageAmount = pendingDamageAmount;
        this.pendingDamageCanTakeRot = pendingDamageCanTakeRot;
        this.pendingDamageCanDiscardRot = pendingDamageCanDiscardRot;
    }

    public static GameStatePacket from(GameController gc, int recipientClientId) {
        ArrayList<PlayerView> views = new ArrayList<>();
        for (Player p : gc.getPlayers()) {
            boolean own = p.getClientId() == recipientClientId;
            boolean roleVisible = own || p.isRoleRevealed() || p.getRole() == Role.EMPEROR;
            views.add(new PlayerView(
                    p.getClientId(),
                    p.getPosition(),
                    p.getPlayerName(),
                    p.getCharacter() != null ? p.getCharacter().getName() : "",
                    p.getCharacter() != null ? p.getCharacter().getAbilityDescription() : "",
                    p.getCharacter() != null ? p.getCharacter().getHp() : 0,
                    p.getCharacter() != null ? p.getCharacter().getMaxHp() : 0,
                    p.getCharacter() != null ? p.getCharacter().getRotPower() : 0,
                    roleVisible ? p.getRole() : null,
                    p.isAlive(),
                    p.isRoleRevealed()
            ));
        }
        ArrayList<DieView> dice = new ArrayList<>();
        for (Die d : gc.getDiePool().getDice()) {
            dice.add(new DieView(d.getDieFace(), d.isLocked(), d.isUnlockable(), d.isResolved()));
        }
        HashMap<Integer, ArrayList<Integer>> validTargets = new HashMap<>(gc.computeValidTargetIdsByDieIndex());
        Player current = gc.currentPlayer();
        boolean slabReady = current != null
                && current.getCharacter() instanceof SlabTheKiller slab
                && !slab.isAbilityUsedThisTurn();
        GameController.PendingDamage pd = gc.getPendingDamage();
        int pdTarget = pd != null ? pd.targetClientId : -1;
        int pdSource = pd != null ? pd.sourceClientId : -1;
        int pdAmount = pd != null ? pd.amount : 0;
        boolean pdTakeRot = pd != null && pd.canTakeRot;
        boolean pdDiscardRot = pd != null && pd.canDiscardRot;
        return new GameStatePacket(views, dice, recipientClientId,
                gc.getCurrentTurnClientId(), gc.getRollsLeft(), gc.getRotPool(),
                gc.getPhase(), gc.getWinningRole(), validTargets, slabReady,
                pdTarget, pdSource, pdAmount, pdTakeRot, pdDiscardRot);
    }

    // Getters
    public ArrayList<PlayerView> getPlayers() { return players; }
    public ArrayList<DieView> getDice() { return dice; }
    public int getRecipientClientId() { return recipientClientId; }
    public int getCurrentTurnClientId() { return currentTurnClientId; }
    public int getRollsLeft() { return rollsLeft; }
    public int getRotPool() { return rotPool; }
    public GameController.Phase getPhase() { return phase; }
    public Role getWinningRole() { return winningRole; }
    public HashMap<Integer, ArrayList<Integer>> getValidTargetsByDieIndex() { return validTargetsByDieIndex; }
    public boolean isSlabAbilityAvailable() { return slabAbilityAvailable; }
    public int getPendingDamageTargetClientId() { return pendingDamageTargetClientId; }
    public int getPendingDamageSourceClientId() { return pendingDamageSourceClientId; }
    public int getPendingDamageAmount() { return pendingDamageAmount; }
    public boolean isPendingDamageCanTakeRot() { return pendingDamageCanTakeRot; }
    public boolean isPendingDamageCanDiscardRot() { return pendingDamageCanDiscardRot; }

    @Override
    public String toString() {
        return "GameStatePacket{phase=" + phase + ", currentTurnClientId=" + currentTurnClientId
                + ", rollsLeft=" + rollsLeft + ", rotPool=" + rotPool + ", winner=" + winningRole + "}";
    }
}
