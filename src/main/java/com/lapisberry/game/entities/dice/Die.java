package com.lapisberry.game.entities.dice;

import com.lapisberry.game.entities.characters.BaseCharacter;
import com.lapisberry.utils.Randomizer;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code Dice} class
 * <p>
 * This class can roll dice and show what is the diceFace of the dice.
 *
 * @author LapisBerry
 */
public class Die implements Serializable {
    @Serial
    private static final long serialVersionUID = -6996645007181018587L;
    // Fields
    private DieFace diceFace;
    private boolean isLocked;
    private boolean isUnlockable;
    private BaseCharacter targetCharacter;

    // Constructors
    public Die() {
        roll();
        isLocked = false;
        isUnlockable = true;
        targetCharacter = null;
    }

    // Copy constructors
    public Die(Die die) {
        this.diceFace = die.diceFace;
        this.isLocked = die.isLocked;
        this.isUnlockable = die.isUnlockable;
        this.targetCharacter = die.targetCharacter;
    }

    // Methods
    public void roll() {
        int index = Randomizer.getRandomInt(DieFace.values().length);
        diceFace = DieFace.values()[index];
    }

    // Getters Setters
    public DieFace getDiceFace() {
        return diceFace;
    }

    public void setDiceFace(DieFace diceFace) {
        this.diceFace = diceFace;
    }

    public BaseCharacter getTargetCharacter() {
        return targetCharacter;
    }

    public void setTargetCharacter(BaseCharacter targetCharacter) {
        this.targetCharacter = targetCharacter;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        if (!locked && !isUnlockable) return;
        isLocked = locked;
    }

    public boolean isUnlockable() {
        return isUnlockable;
    }

    public void setUnlockable(boolean unlockable) {
        isUnlockable = unlockable;
    }
}
