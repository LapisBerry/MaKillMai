package game.logic.components.players;

import game.logic.components.bases.BaseCharacter;

/**
 * The {@code Player} class represents a player in the game.
 * <p>
 *     It has a name, a character, and a role.
 * <p>
 *     It is used to store the player's information.
 * <p>
 *     The {@code Player} class is the class that will be in the board's circle of players.
 */
public class Player {
    // Fields
    private String name;
    private BaseCharacter character;
    private Role role;


    // Constructors
    public Player(String name) {
        setName(name);
    }

    public Player(String name, BaseCharacter character, Role role) {
        setName(name);
        setCharacter(character);
        setRole(role);
    }


    // Methods
    public String fullInfo() {
        return name + " | " + character.getName() + " | " + role;
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
        if (character != null) this.character.setOwner(this);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
