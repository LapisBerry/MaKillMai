package components.dices;

public class DicePool {
    // Fields
    private Dice[] dicePool; // "Wanna use ArrayList<> instead?" - @LapisBerry
    private boolean[] isDiceLockedAt;

    
    // Constructors
    public DicePool() {
        dicePool = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
        isDiceLockedAt = new boolean[]{false, false, false, false, false};
    }


    // Methods
    public void rollAllUnlockedDices() {
        for (int i = 0; i < dicePool.length; ++i) {
            if (!isDiceLockedAt[i]) {
                dicePool[i].rollDice();
            }
        }
    }

    public void unlockAllDices() {
        for (int i = 0; i < isDiceLockedAt.length; ++i) {
            isDiceLockedAt[i] = false;
        }
    }

    public void unlockDiceAt(int index) {
        if (0 <= index && index < dicePool.length) {
            isDiceLockedAt[index] = false;
        }
    }

    public void lockDiceAt(int index) {
        if (0 <= index && index < dicePool.length) {
            isDiceLockedAt[index] = true;
        }
    }

    // Getter Setter
    public Dice[] getDicePool() {
        return dicePool;
    }

    public void setDicePool(Dice[] dicePool) {
        this.dicePool = dicePool;
    }
}
