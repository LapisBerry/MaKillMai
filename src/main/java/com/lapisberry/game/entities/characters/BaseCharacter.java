package com.lapisberry.game.entities.characters;

import com.lapisberry.utils.Config;

import java.io.Serializable;

public abstract class BaseCharacter implements Serializable {
    // Fields
    private final String name;
    private final String abilityDescription;
    private final int rollPerTurn;
    private final int diceRequiredForPureMagic;
    private int hp;
    private int maxHp;
    private int rotPower;
    private int reRollLeft;
    private boolean isWantToUsePureMagic;

    // Constructor
    public BaseCharacter(String name, String abilityDescription, int hp) {
        this.name = name;
        this.abilityDescription = abilityDescription;
        this.rollPerTurn = Config.DEFAULT_ROLL_PER_TURN;
        this.diceRequiredForPureMagic = Config.DEFAULT_DICE_REQUIRED_FOR_PURE_MAGIC;
        this.hp = hp;
        this.maxHp = hp;
        this.rotPower = Config.DEFAULT_ROT_POWER;
    }

    // Getters Setters
    public String getName() {
        return name;
    }

    public String getAbilityDescription() {
        return abilityDescription;
    }

    public boolean isWantToUsePureMagic() {
        return isWantToUsePureMagic;
    }

    public void setWantToUsePureMagic(boolean wantToUsePureMagic) {
        isWantToUsePureMagic = wantToUsePureMagic;
    }

    public int getReRollLeft() {
        return reRollLeft;
    }

    public void setReRollLeft(int reRollLeft) {
        this.reRollLeft = reRollLeft;
    }

    public int getDiceRequiredForPureMagic() {
        return diceRequiredForPureMagic;
    }

    public int getRotPower() {
        return rotPower;
    }

    public void setRotPower(int rotPower) {
        this.rotPower = rotPower;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    public int getRollPerTurn() {
        return rollPerTurn;
    }
}
