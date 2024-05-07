package components.dices;

import java.util.Arrays;

/**
 * The {@code DicePool} class
 * <p>This class implements dice pool.
 * <p>Q: What is dice pool?
 * <p>A: Every player will have 5 dice. Dice pool is like a plate for those 5 dices!
 * <p>You can roll all dices in the pool. You can lock the specific dice. You can unlock the specific dice.
 * @author LapisBerry
 */
public class DicePool {
    // Fields
    private final Dice[] diceArray;
    private final boolean[] isDiceLockedAt;
    private final boolean[] isUnlockableAt;

    
    // Constructors
    public DicePool() {
        diceArray = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
        isDiceLockedAt = new boolean[]{false, false, false, false, false};
        isUnlockableAt = new boolean[]{true, true, true, true, true};
    }


    // Methods
    public void rollAllUnlockedDices() {
        for (int i = 0; i < diceArray.length; ++i) {
            if (!isDiceLockedAt[i]) {
                diceArray[i].roll();
            }
        }
    }

    public void unlockAllDices() {
        Arrays.fill(isDiceLockedAt, false);
    }

    public void unlockDiceAt(int index) {
        isDiceLockedAt[index] = false;
    }

    public void lockDiceAt(int index) {
        isDiceLockedAt[index] = true;
    }

    public void makeDiceUnableToBeUnlockedAt(int index) {
        isUnlockableAt[index] = false;
    }

    public void makeAllDiceUnlockable() {
        Arrays.fill(isUnlockableAt, true);
    }


    // Getter Setter
    public Dice[] getDiceArray() {
        return diceArray;
    }
}
