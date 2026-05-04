package com.lapisberry.game.entities.characters;

import com.lapisberry.utils.Config;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseCharacter implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Fields
    private final String name;
    private final String abilityDescription;
    private final int rollPerTurn;
    private final int diceRequiredForPureMagic;
    private int hp;
    private int maxHp;
    private int rotPower;

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

    // Mutators
    public void takeDamage(int amount) {
        if (amount <= 0) return;
        setHp(hp - amount);
    }

    public void heal(int amount) {
        if (amount <= 0) return;
        setHp(hp + amount);
    }

    public void addRot(int amount) {
        rotPower = Math.max(0, rotPower + amount);
    }

    public void clearRot() {
        rotPower = 0;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void increaseMaxHp(int delta) {
        maxHp = Math.max(1, maxHp + delta);
        hp = Math.min(hp + delta, maxHp);
        if (hp < 0) hp = 0;
    }

    // Getters Setters
    public String getName() {
        return name;
    }

    public String getAbilityDescription() {
        return abilityDescription;
    }

    public int getDiceRequiredForPureMagic() {
        return diceRequiredForPureMagic;
    }

    public int getRotPower() {
        return rotPower;
    }

    public void setRotPower(int rotPower) {
        this.rotPower = Math.max(0, rotPower);
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
