package components.dices;

import utils.Randomizer;

/**
 * The {@code Dice} class
 * <p>
 * This class can roll dice and show what is the diceFace of the dice.
 * @author LapisBerry
 */
public class Dice {
    // Fields
    private DiceFace diceFace;


    // Constructors
    public Dice() {
        roll();
    }

    public Dice(DiceFace diceFace) {
        setDiceFace(diceFace);
    }


    // Methods
    public void roll() {
        int index = Randomizer.getRandomInt(6);
        diceFace = DiceFace.values()[index];
    }


    // Getter Setter
    public DiceFace getDiceFace() {
        return diceFace;
    }

    public void setDiceFace(DiceFace diceFace) {
        this.diceFace = diceFace;
    }
}
