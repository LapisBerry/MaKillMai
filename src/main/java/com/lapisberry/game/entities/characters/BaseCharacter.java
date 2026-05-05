package com.lapisberry.game.entities.characters;

import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.entities.dice.DieFace;
import com.lapisberry.game.entities.players.Player;
import com.lapisberry.utils.Config;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseCharacter implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Fields
    private final String name;
    private final String abilityDescription;
    private int hp;
    private int maxHp;
    private int rotPower;

    // Constructor
    public BaseCharacter(String name, String abilityDescription, int hp) {
        this.name = name;
        this.abilityDescription = abilityDescription;
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

    // ---- Hooks (defaults match the standard rules) -------------------------

    /** Standard: number of rolls a player gets per turn. */
    public int getRollPerTurn() {
        return Config.DEFAULT_ROLL_PER_TURN;
    }

    /** Standard: 3+ PURE_MAGIC dice are needed to fire it. */
    public int getDiceRequiredForPureMagic() {
        return Config.DEFAULT_DICE_REQUIRED_FOR_PURE_MAGIC;
    }

    /** When true, STONE_SUPPRESSOR dice stay unlockable (Black Jack). */
    public boolean canRerollStoneSuppressor() {
        return false;
    }

    /** Cap on damage taken from an Indian Attack triggered by the rot pool emptying. */
    public int onRotAttackDamage(int rotHeld) {
        return rotHeld;
    }

    /** Damage taken from an opponent's Pure Magic (Paul Regret returns 0). */
    public int onPureMagicDamage(int amount) {
        return amount;
    }

    /** Heal applied when this character is the target of a HEALTH_POTION. */
    public int onHealReceived(boolean fromSelf, int amount) {
        return amount;
    }

    /** Whether an ATTACK die of {@code face} originating from this character can hit at the given distance. */
    public boolean canHitAtDistance(DieFace face, int distance) {
        if (face == DieFace.ATTACK_1) return distance == 1;
        if (face == DieFace.ATTACK_2) return distance == 2;
        return false;
    }

    /** Fires after rolling phase, before attack/heal resolution begins. */
    public void onAfterRollingPhase(GameController gc, Player self) {
    }

    /** Fires when this character takes damage from an opponent's ATTACK_1/ATTACK_2 die. */
    public void onTakeAttackDamage(GameController gc, Player self, Player source) {
    }

    /** Fires when any other player is eliminated. */
    public void onOtherPlayerEliminated(GameController gc, Player self, Player dead) {
    }

    /** Fires at the start of this character's turn. Used to clear once-per-turn flags. */
    public void onTurnStart(GameController gc, Player self) {
    }

    // ---- Getters / Setters --------------------------------------------------

    public String getName() {
        return name;
    }

    public String getAbilityDescription() {
        return abilityDescription;
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
}
