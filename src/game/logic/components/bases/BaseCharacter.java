package game.logic.components.bases;

import game.config.GameConfig;
import game.logic.components.dices.Dice;
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
    private final int rollPerTurn;
    private String abilityDescription;
    private final DicePool dicePool;
    private int reRollLeft;
    private boolean isWantToUsePureMagic;
    private int requiredForPureMagic;


    // Constructors
    public BaseCharacter(final String name, final int maxHp, final int hp, final int rotPower, final String abilityDescription) {
        setName(name);
        setMaxHp(maxHp);
        setHp(hp);
        setRotPower(rotPower);
        rollPerTurn = GameConfig.BASE_ROLL_PER_TURN;
        setAbilityDescription(abilityDescription);
        dicePool = new DicePool();
        setReRollLeft(rollPerTurn);
        setRequiredForPureMagic(GameConfig.BASE_REQUIRED_FOR_PURE_MAGIC);
    }


    // Dice Methods
    // roll dice
    public void rollAllUnlockedDices() {
        if (hasReRollLeft()) {
            --reRollLeft;
            getDicePool().rollAllUnlockedDices();
        }
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
            }
        }
    }

    // using dice on baseCharacter
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

    // takeSomething from anotherBaseCharacter
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

    // rollsIntoSomething
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

    public boolean isGetStoneSuppressorPenalty() {
        return countDiceFace(DiceFace.STONE_SUPPRESSOR) >= 3;
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
        // reset reRollLeft
        setReRollLeft(rollPerTurn);
        // reset all dice
        getDicePool().resetAllDices();
    }

    public void endOfTurn() {
    }

    // is able to use something
    public boolean isAbleToUseAttack1On(Player characterOwner, Player otherPlayer) {
        return GameController.getInstance().getBoard().distanceBetween(characterOwner, otherPlayer) == 1;
    }

    public boolean isAbleToUseAttack1() {
        // normally you can use attack 1
        return true;
    }

    public boolean isAbleToUseAttack2On(Player characterOwner, Player otherPlayer) {
        if (GameController.getInstance().getBoard().getCircleOfPlayers().size() <= 3)
            return isAbleToUseAttack1On(characterOwner, otherPlayer);
        return GameController.getInstance().getBoard().distanceBetween(characterOwner, otherPlayer) == 2;
    }

    public boolean isAbleToUseAttack2() {
        // normally you cannot use attack 2
        return true;
    }

    public boolean isAbleToUseHealthPotionOn(Player player) {
        // normally you can use health potion on anyone
        return true;
    }

    public boolean isAbleToUseHealthPotion() {
        // normally you cannot use health potion
        return true;
    }

    public boolean isAbleToUseRotPower() {
        // normally you cannot use rot power
        return false;
    }

    public boolean isAbleToUsePureMagic() {
        // to use pure magic you have to have at least 3 pure magic dice
        return countDiceFace(DiceFace.PURE_MAGIC) >= requiredForPureMagic;
    }

    public boolean isAbleToUseStoneSuppressor() {
        // normally you cannot use stone suppressor
        return false;
    }

    public boolean hasReRollLeft() {
        return getReRollLeft() > 0;
    }

    public boolean isAbleToResolveAction() {
        for (int i = 0; i < getDicePool().getDiceArray().length; ++i) {
            // if the dice is already aim to someone, it's okay
            if (getDicePool().getPlayerTargetedByDiceAt(i) != null) continue;

            boolean isAbleToUseThisDice;
            DiceFace diceFace = getDicePool().getDiceArray()[i].getDiceFace();
            switch (diceFace) {
                case ATTACK_1 -> isAbleToUseThisDice = isAbleToUseAttack1();
                case ATTACK_2 -> isAbleToUseThisDice = isAbleToUseAttack2();
                case HEALTH_POTION -> isAbleToUseThisDice = isAbleToUseHealthPotion();
                case ROT_POWER -> isAbleToUseThisDice = isAbleToUseRotPower();
                case PURE_MAGIC -> isAbleToUseThisDice = isAbleToUsePureMagic();
                case STONE_SUPPRESSOR -> isAbleToUseThisDice = isAbleToUseStoneSuppressor();
                default -> isAbleToUseThisDice = false;
            }
            if (isAbleToUseThisDice) return false;
        }
        return true;
    }

    public void resolveDiceAction() {
        for (int i = 0; i < getDicePool().getDiceArray().length; ++i) {
            if (getDicePool().getPlayerTargetedByDiceAt(i) != null) {
                DiceFace diceFace = getDicePool().getDiceArray()[i].getDiceFace();
                switch (diceFace) {
                    case ATTACK_1 -> useAttack1(getDicePool().getPlayerTargetedByDiceAt(i).getCharacter());
                    case ATTACK_2 -> useAttack2(getDicePool().getPlayerTargetedByDiceAt(i).getCharacter());
                    case HEALTH_POTION -> useHealthPotion(getDicePool().getPlayerTargetedByDiceAt(i).getCharacter());
                    case ROT_POWER -> useRotPower(); // normally this should not happen
                    case PURE_MAGIC -> usePureMagic(); // to usePureMagic, it's controlled by isWantToUsePureMagic variable
                    case STONE_SUPPRESSOR -> useStoneSuppressor(); // normally this should not happen
                }
            }
        }
        if (isWantToUsePureMagic) usePureMagic();
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

    public int getRollPerTurn() {
        return rollPerTurn;
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

    public int getReRollLeft() {
        return reRollLeft;
    }

    public void setReRollLeft(int reRollLeft) {
        this.reRollLeft = reRollLeft;
    }

    public boolean isWantToUsePureMagic() {
        return isWantToUsePureMagic;
    }

    public void setWantToUsePureMagic(boolean wantToUsePureMagic) {
        isWantToUsePureMagic = wantToUsePureMagic;
    }

    public int getRequiredForPureMagic() {
        return requiredForPureMagic;
    }

    public void setRequiredForPureMagic(int requiredForPureMagic) {
        this.requiredForPureMagic = requiredForPureMagic;
    }
}
