package components.bases;

import components.dices.DicePool;

public class BaseCharacter {
    // Fields
    private String name;
    private int hp;
    private int maxHp;
    private int rotPower;
    private int requiredForPureMagic;
    private int sumStoneSuppressor;
    private DicePool dicePool;
    

    // Constructor
    public BaseCharacter() {
        setName("Dummy");
        setMaxHp(4);
        setHp(4);
        setRotPower(0);
        setRequiredForPureMagic(3);
        setSumStoneSuppressor(0);
        dicePool = new DicePool();
    }

    public BaseCharacter(String name, int hp, int maxHp, int rotPower, int requiredForPureMagic, int sumStoneSuppressor) {
        setName(name);
        setMaxHp(maxHp);
        setHp(hp);
        setRotPower(rotPower);
        setRequiredForPureMagic(requiredForPureMagic);
        setSumStoneSuppressor(sumStoneSuppressor);
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
        this.maxHp = maxHp;
    }

    public int getRotPower() {
        return rotPower;
    }

    public void setRotPower(int rotPower) {
        this.rotPower = rotPower;
    }

    public int getRequiredForPureMagic() {
        return requiredForPureMagic;
    }

    public void setRequiredForPureMagic(int requiredForPureMagic) {
        this.requiredForPureMagic = Math.max(0, requiredForPureMagic);
    }

    public int getSumStoneSuppressor() {
        return sumStoneSuppressor;
    }

    public void setSumStoneSuppressor(int sumStoneSuppressor) {
        this.sumStoneSuppressor = Math.max(0, sumStoneSuppressor);
    }

    public DicePool getDicePool() {
        return dicePool;
    }

    public void setDicePool(DicePool dicePool) {
        this.dicePool = dicePool;
    }
}
