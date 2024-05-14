package game.logic.components.dices;

import game.logic.components.players.Player;

import java.util.Arrays;

/**
 * The {@code DicePool} class
 * <p>This class implements dice pool.
 * <p>Q: What is dice pool?
 * <p>A: Every player will have 5 dice. Dice pool is like a plate for those 5 dices!
 * <p>You can roll all dices in the pool. You can lock the specific dice. You can unlock the specific dice.
 *
 * @author LapisBerry
 */
public class DicePool {
    // Fields
    private final Dice[] diceArray;
    private final boolean[] isDiceLockedAt;
    private final boolean[] isUnlockableAt;
    private final Player[] playerTargetedByDiceAt;


    // Constructors
    public DicePool() {
        diceArray = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
        isDiceLockedAt = new boolean[]{false, false, false, false, false};
        isUnlockableAt = new boolean[]{true, true, true, true, true};
        playerTargetedByDiceAt = new Player[]{null, null, null, null, null};
    }


    // Methods
    public void rollAllUnlockedDices() {
        for (int i = 0; i < diceArray.length; ++i) {
            if (!isDiceLockedAt[i]) {
                diceArray[i].roll();
            }
        }
    }

    public void resetAllDices() {
        unlockAllDices();
        makeAllDiceUnlockable();
        resetPlayerTargetedByDice();
    }

    public void unlockDiceAt(int index) {
        if (isUnlockableAt[index]) isDiceLockedAt[index] = false;
    }

    public void lockDiceAt(int index) {
        isDiceLockedAt[index] = true;
    }

    public void makeDiceUnableToBeUnlockedAt(int index) {
        isUnlockableAt[index] = false;
    }

    public boolean isDiceLockedAt(int index) {
        return isDiceLockedAt[index];
    }

    public boolean isUnlockableAt(int index) {
        return isUnlockableAt[index];
    }

    public void setPlayerTargetedByDiceAt(int index, Player player) {
        playerTargetedByDiceAt[index] = player;
    }

    private void unlockAllDices() {
        Arrays.fill(isDiceLockedAt, false);
    }

    private void makeAllDiceUnlockable() {
        Arrays.fill(isUnlockableAt, true);
    }

    private void resetPlayerTargetedByDice() {
        Arrays.fill(playerTargetedByDiceAt, null);
    }


    // Getter Setter
    public Dice[] getDiceArray() {
        return diceArray;
    }

    public Dice getDiceAt(int index) {
        return diceArray[index];
    }

    public Player getPlayerTargetedByDiceAt(int index) {
        return playerTargetedByDiceAt[index];
    }
}
