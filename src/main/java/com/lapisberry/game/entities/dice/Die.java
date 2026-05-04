package com.lapisberry.game.entities.dice;

import com.lapisberry.utils.Randomizer;

import java.io.Serial;
import java.io.Serializable;

/**
 * A single six-faced die. {@code isLocked} means the player chose to keep this
 * face on the next reroll. {@code isUnlockable} is {@code false} for faces
 * the player is forbidden from rerolling (notably STONE_SUPPRESSOR).
 */
public class Die implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Fields
    private DieFace dieFace;
    private boolean locked;
    private boolean unlockable;
    private boolean resolved;

    // Constructors
    public Die() {
        roll();
        this.locked = false;
        this.unlockable = true;
        this.resolved = false;
    }

    // Copy constructor
    public Die(Die other) {
        this.dieFace = other.dieFace;
        this.locked = other.locked;
        this.unlockable = other.unlockable;
        this.resolved = other.resolved;
    }

    // Methods
    public void roll() {
        int index = Randomizer.getRandomInt(DieFace.values().length);
        this.dieFace = DieFace.values()[index];
    }

    // Getters Setters
    public DieFace getDieFace() {
        return dieFace;
    }

    public void setDieFace(DieFace dieFace) {
        this.dieFace = dieFace;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        if (!locked && !unlockable) return;
        this.locked = locked;
    }

    public boolean isUnlockable() {
        return unlockable;
    }

    public void setUnlockable(boolean unlockable) {
        this.unlockable = unlockable;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
