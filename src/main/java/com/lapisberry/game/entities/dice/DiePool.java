package com.lapisberry.game.entities.dice;

import com.lapisberry.utils.Config;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class DiePool implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Fields
    private final ArrayList<Die> dice;

    // Constructors
    public DiePool() {
        this.dice = new ArrayList<>(Config.DICE_PER_PLAYER);
        for (int i = 0; i < Config.DICE_PER_PLAYER; i++) {
            this.dice.add(new Die());
        }
    }

    // Deep copy constructor
    public DiePool(DiePool other) {
        this.dice = new ArrayList<>(other.dice.size());
        for (Die d : other.dice) {
            this.dice.add(new Die(d));
        }
    }

    // Methods
    public void rollAllUnlockedDice() {
        for (Die die : dice) {
            if (!die.isLocked()) die.roll();
        }
    }

    public void resetForNewTurn() {
        for (Die die : dice) {
            die.setLocked(false);
            die.setUnlockable(true);
            die.setResolved(false);
            die.roll();
        }
    }

    public int countFace(DieFace face) {
        int n = 0;
        for (Die d : dice) {
            if (d.getDieFace() == face) n++;
        }
        return n;
    }

    public int countUnresolvedFace(DieFace face) {
        int n = 0;
        for (Die d : dice) {
            if (!d.isResolved() && d.getDieFace() == face) n++;
        }
        return n;
    }

    // Getters
    public ArrayList<Die> getDice() {
        return dice;
    }
}
