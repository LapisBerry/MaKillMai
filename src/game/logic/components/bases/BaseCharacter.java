package game.logic.components.bases;

import game.config.GameConfig;
import game.logic.components.dices.DiceFace;
import game.logic.components.dices.DicePool;
import game.logic.components.players.Player;
import game.logic.controller.GameController;

/**
 * The {@code BaseCharacter} class
 * <p>
 * This class is the base for character. When {@code BaseCharacter} interacts with something include another {@code BaseCharacter}, another {@code BaseCharacter} will be the one who resolve the interaction.
 * <p>
 * When we make a character class, that character class will extend this class.
 *
 * @author LapisBerry
 */
public abstract class BaseCharacter {
    // Fields
    private String name;
    private int hp;
    private int maxHp;
    private int rotPower;
    private String abilityDescription;
    private final DicePool dicePool;
    private Player owner; // this will automatically be set when the character is assigned to a player
    private int reRollLeft;


    // Constructors
    public BaseCharacter(final String name, final int maxHp, final int hp, final int rotPower, final String abilityDescription) {
        setName(name);
        setMaxHp(maxHp);
        setHp(hp);
        setRotPower(rotPower);
        setAbilityDescription(abilityDescription);
        dicePool = new DicePool();
        setReRollLeft(GameConfig.BASE_ROLL_PER_TURN);
    }


    // Dice Methods
    public void useAttack1(BaseCharacter anotherBaseCharacter) {
        anotherBaseCharacter.takeAttack1From(this);
    }

    public void useAttack2(BaseCharacter anotherBaseCharacter) {
        anotherBaseCharacter.takeAttack2From(this);
    }

    public void useHealthPotion(BaseCharacter baseCharacter) {
        baseCharacter.takeHealthPotionFrom(this);
    }

    public void useRotPower() {/*blank*/}

    public void usePureMagic() {
        // deals another character 1 damage, but should not hit itself
        GameController.getInstance().getBoard().getCircleOfPlayers().forEach(player -> player.getCharacter().takePureMagicFrom(this));
        // cleanse rot power
        GameController.getInstance().getRotPool().clearRotPower(this);
    }

    public void useStoneSuppressor() {/*blank*/}

    // getSomething from anotherBaseCharacter
    public void takeAttack1From(BaseCharacter anotherBaseCharacter) {
        setHp(getHp() - 1);
    }

    public void takeAttack2From(BaseCharacter anotherBaseCharacter) {
        setHp(getHp() - 1);
    }

    public void takeHealthPotionFrom(BaseCharacter anotherBaseCharacter) {
        setHp(getHp() + 1);
    }

    public void takeRotPowerFrom(BaseCharacter anotherBaseCharacter) {/*blank*/}

    public void takePureMagicFrom(BaseCharacter anotherBaseCharacter) {
        // take 1 damage, but cannot hit by itself
        if (anotherBaseCharacter == this) return;
        setHp(getHp() - 1);
    }

    // When character rolls and get those dice faces, call these functions, what will happen to them
    public void resolveRolledDice() {
        for (int i = 0; i < getDicePool().getDiceArray().length; ++i) {
            switch (getDicePool().getDiceArray()[i].getDiceFace()) {
                case ATTACK_1 -> rollsIntoAttack1(i);
                case ATTACK_2 -> rollsIntoAttack2(i);
                case HEALTH_POTION -> rollsIntoHealthPotion(i);
                case ROT_POWER -> rollsIntoRotPower(i);
                case PURE_MAGIC -> rollsIntoPureMagic(i);
                case STONE_SUPPRESSOR -> rollsIntoStoneSuppressor(i);
                default -> {
                }
            }
        }
    }

    public void rollsIntoAttack1(int indexOfDice) {/*blank*/}

    public void rollsIntoAttack2(int indexOfDice) {/*blank*/}

    public void rollsIntoHealthPotion(int indexOfDice) {/*blank*/}

    public void rollsIntoRotPower(int indexOfDice) {
        // say to rot pool that this character get 1 rot power
        GameController.getInstance().getRotPool().giveOneRotPower(this);
    }

    public void rollsIntoPureMagic(int indexOfDice) {/*blank*/}

    public void rollsIntoStoneSuppressor(int indexOfDice) {
        // lock the dice
        getDicePool().lockDiceAt(indexOfDice);
        // make the dice unable to be unlocked
        getDicePool().makeDiceUnableToBeUnlockedAt(indexOfDice);
    }

    // utility
    private int countDiceFace(DiceFace diceFace) {
        int count = 0;
        for (int i = 0; i < getDicePool().getDiceArray().length; ++i) {
            if (getDicePool().getDiceArray()[i].getDiceFace() == diceFace) {
                ++count;
            }
        }
        return count;
    }

    private void resetReRollLeft() {
        setReRollLeft(GameConfig.BASE_ROLL_PER_TURN);
    }

    // getSomething from game system
    public void takeDynamiteDamage() {
        // take damage from dynamite
        setHp(getHp() - 1);
    }

    public void takeRotPowerDamage() {
        // take damage equal to rot power
        setHp(getHp() - getRotPower());
    }

    // start, end of turn
    public void startOfTurn() {
        // clear all dice
        resetReRollLeft();
        getDicePool().makeAllDiceUnlockable();
        getDicePool().unlockAllDices();
    }

    public void endOfTurn() {
    }

    // is able to use something
    public boolean isAbleToUseAttack1On(Player player) {
        return GameController.getInstance().getBoard().distanceBetween(getOwner(), player) == 1;
    }

    public boolean isAbleToUseAttack2On(Player player) {
        if (GameController.getInstance().getBoard().getCircleOfPlayers().size() <= 3) return isAbleToUseAttack1On(player);
        return GameController.getInstance().getBoard().distanceBetween(getOwner(), player) == 2;
    }

    public boolean isAbleToUseHealthPotionOn(Player player) {
        // normally you can use health potion on anyone
        return true;
    }

    public boolean isAbleToUseRotPower() {
        // normally you cannot use rot power
        return false;
    }

    public boolean isAbleToUsePureMagic() {
        // to use pure magic you have to have at least 3 pure magic dice
        return countDiceFace(DiceFace.PURE_MAGIC) >= GameConfig.BASE_REQUIRED_FOR_PURE_MAGIC;
    }

    public boolean isAbleToUseStoneSuppressor() {
        // normally you cannot use stone suppressor
        return false;
    }


    // Getter Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
        this.hp = Math.min(this.hp, maxHp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
    }

    public int getRotPower() {
        return rotPower;
    }

    public void setRotPower(int rotPower) {
        this.rotPower = rotPower;
    }

    public String getAbilityDescription() {
        return abilityDescription;
    }

    public void setAbilityDescription(String abilityDescription) {
        this.abilityDescription = abilityDescription;
    }

    public DicePool getDicePool() {
        return dicePool;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getReRollLeft() {
        return reRollLeft;
    }

    public void setReRollLeft(int reRollLeft) {
        this.reRollLeft = reRollLeft;
    }
}
