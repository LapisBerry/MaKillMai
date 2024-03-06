package components.dices;

import interfaces.Rollable;

import java.util.Random;

/**
 * The {@code Dice} class
 * <p>
 * This class can roll dice and show what is the diceFace of the dice.
 * @author LapisBerry
 */
public class Dice implements Rollable {
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
        Random rand = new Random();
        int index = rand.nextInt(6);
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
