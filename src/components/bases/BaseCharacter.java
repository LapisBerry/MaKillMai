package components.bases;

import components.dices.DicePool;
import components.players.Player;

/**
 * The {@code BaseCharacter} class
 * <p>
 * This class is the base for character. When {@code BaseCharacter} interacts with something include another {@code BaseCharacter}, another {@code BaseCharacter} will be the one who resolve the interaction.
 * <p>
 * When we make a character class, that character class will extend this class.
 *
 * @author LapisBerry
 */
public class BaseCharacter {
    // Fields
    private String name;
    private int hp;
    private int maxHp;
    private int rotPower;
    private String abilityDescription;
    private final DicePool dicePool;


    // Constructor
    public BaseCharacter() {
        setName("Dummy");
        setMaxHp(10);
        setHp(10);
        setRotPower(0);
        dicePool = new DicePool();
        abilityDescription = "This is a dummy character.";
    }

    public BaseCharacter(final String name, final int maxHp, final int hp, final int rotPower, final String abilityDescription) {
        setName(name);
        setMaxHp(maxHp);
        setHp(hp);
        setRotPower(rotPower);
        setAbilityDescription(abilityDescription);
        dicePool = new DicePool();
    }


    // Dice Methods
    public void useAttack1(BaseCharacter anotherBaseCharacter) {
        // TODO
    }

    public void useAttack2(BaseCharacter anotherBaseCharacter) {
        // TODO
    }

    public void useHealthPotion(BaseCharacter baseCharacter) {
        // TODO
    }

    public void useRotPower() {/*blank*/}

    public void usePureMagic(int amount) {
        // TODO
    }

    public void useStoneSuppressor() {/*blank*/}

    // getSomething from anotherBaseCharacter
    public void takeAttack1From(BaseCharacter anotherBaseCharacter) {
        // TODO
    }

    public void takeAttack2From(BaseCharacter anotherBaseCharacter) {
        // TODO
    }

    public void takeHealthPotionFrom(BaseCharacter anotherBaseCharacter) {
        // TODO
    }

    public void takeRotPowerFrom(BaseCharacter anotherBaseCharacter) {/*blank*/}

    public void takePureMagicFrom(BaseCharacter anotherBaseCharacter, int amount) {
        // TODO
    }

    // When character rolls and get those dice faces, call these functions, what will happen to them
    public void rollsIntoAttack1() {/*blank*/}

    public void rollsIntoAttack2() {/*blank*/}

    public void rollsIntoHealthPotion() {/*blank*/}

    public void rollsIntoRotPower() {
        // TODO
    }

    public void rollsIntoPureMagic() {/*blank*/}

    public void rollsIntoStoneSuppressor() {
        // TODO
    }

    // getSomething from game system
    public void takeDynamiteDamage() {
        // TODO
    }

    public void takeRotPowerDamage() {
        // TODO
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
}
