package components.players;

import components.bases.BaseCharacter;

public class Player {
    // Fields
    private String name;
    private BaseCharacter character;
    private Role role;


    // Constructors
    public Player(String name, BaseCharacter character, Role role) {
        setName(name);
        setCharacter(character);
        setRole(role);
        if (role == Role.EMPEROR) {
            character.setMaxHp(character.getMaxHp() + 1);
            character.setHp(character.getHp() + 1);
        }
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
        // automatically set the owner of the character to this player
        this.character.setOwner(this);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
