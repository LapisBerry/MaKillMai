package components.dices;

import java.util.Random;

import config.enums.DiceFace;

public class Dice {
    // Fields
    private DiceFace diceFace;


    // Constructors
    public Dice() {
        rollDice();
    }

    public Dice(DiceFace diceFace) {
        setDiceFace(diceFace);
    }


    // Methods
    public void rollDice() {    
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
