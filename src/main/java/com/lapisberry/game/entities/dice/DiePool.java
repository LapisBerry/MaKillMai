package com.lapisberry.game.entities.dice;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class DiePool implements Serializable {
    @Serial
    private static final long serialVersionUID = -8762762137011732365L;
    // Fields
    private final ArrayList<Die> dice;

    // Constructors
    public DiePool() {
        this.dice = new ArrayList<>(Arrays.asList(new Die(), new Die(), new Die(), new Die(), new Die()));
    }

    // Deep copy constructors
    public DiePool(DiePool oldDiePool) {
        this.dice = new ArrayList<>();
        for (Die oldDie : oldDiePool.dice) {
            this.dice.add(new Die(oldDie));
        }
    }

    // Methods
    public void rollAllUnlockDice() {
        for (Die die : dice) {
            if (!die.isLocked()) die.roll();
        }
    }

    public void lockDie(int index) {
        dice.get(index).setLocked(true);
    }

    public void unlockDie(int index) {
        dice.get(index).setLocked(false);
    }

    public void makeDieUnlockable(int index) {
        dice.get(index).setUnlockable(true);
    }

    public void makeDieUnunlockable(int index) {
        dice.get(index).setUnlockable(false);
    }

    public void resetDiePool() {
        for (Die die : dice) {
            die.setUnlockable(true);
            die.setLocked(false);
            die.setTargetCharacter(null);
        }
    }

    // Getters Setters
    public ArrayList<Die> getDice() {
        return dice;
    }
}
