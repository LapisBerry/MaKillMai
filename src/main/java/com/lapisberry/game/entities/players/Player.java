package com.lapisberry.game.entities.players;

import com.lapisberry.game.entities.characters.BaseCharacter;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code Player} class represents a participant in the game. It carries
 * the network identity, the assigned secret role, the visible character (with
 * its HP / rot state), the seat position around the table, and a flag that
 * tracks whether the role has already been revealed (e.g. on death).
 */
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Fields
    private final int clientId;
    private final String playerName;
    private Role role;
    private BaseCharacter character;
    private int position;
    private boolean roleRevealed;

    // Constructors
    public Player(int clientId, String playerName) {
        this.clientId = clientId;
        this.playerName = playerName;
    }

    // Methods
    public boolean isAlive() {
        return character != null && character.isAlive();
    }

    // Getter Setters
    public int getClientId() {
        return clientId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BaseCharacter getCharacter() {
        return character;
    }

    public void setCharacter(BaseCharacter character) {
        this.character = character;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isRoleRevealed() {
        return roleRevealed;
    }

    public void setRoleRevealed(boolean roleRevealed) {
        this.roleRevealed = roleRevealed;
    }
}
