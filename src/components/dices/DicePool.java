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

    
    // Constructors
    public DicePool() {
        diceArray = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
        isDiceLockedAt = new boolean[]{false, false, false, false, false};
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
        if (0 <= index && index < diceArray.length) {
            isDiceLockedAt[index] = false;
        }
    }

    public void lockDiceAt(int index) {
        if (0 <= index && index < diceArray.length) {
            isDiceLockedAt[index] = true;
        }
    }


    // Getter Setter
    public Dice[] getDiceArray() {
        return diceArray;
    }
}
