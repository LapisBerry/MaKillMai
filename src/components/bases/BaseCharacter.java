package components.bases;

import java.util.ArrayList;

public class BaseCharacter {
    // Field
    private int hp;
    private int maxHp;
    private ArrayList<BaseCard> cards;

    // Constructor
    public BaseCharacter(int hp, int maxHp, ArrayList<BaseCard> startingHands) {
        // TODO: 
    }
    
    // Getter Setter
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
    public ArrayList<BaseCard> getCards() {
        return cards;
    }
    public void setCards(ArrayList<BaseCard> cards) {
        this.cards = cards;
    }
}