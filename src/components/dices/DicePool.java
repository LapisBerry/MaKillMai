package components.dices;

public class DicePool {
    // Fields
    private Dice[] dicePool;
    private boolean[] isDiceLockedAt;

    
    // Constructors
    public DicePool() {
        dicePool = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
        isDiceLockedAt = new boolean[]{false, false, false, false, false};
    }


    // Methods
    public void rollAllDices() { // TODO
        for (int i = 0; i < dicePool.length; ++i) {

        }
    }
}
