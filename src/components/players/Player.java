package components.players;

import components.bases.BaseCharacter;

public class Player {
    // Fields
    private String name;
    private BaseCharacter character;
    

    // Constructors
    public Player() {
        setName("");
        setCharacter(new BaseCharacter());
    }

    public Player(String name, BaseCharacter character) {
        setName(name);
        setCharacter(character);
    }

    // Getter Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseCharacter getCharacter() {
        return character;
    }

    public void setCharacter(BaseCharacter character) {
        this.character = character;
    }
}
