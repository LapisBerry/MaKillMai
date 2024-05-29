package com.lapisberry.game.entities.players;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code Player} class is used for user to know a role and position of other players decide which character to pick.
 */
public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = -6540863475427867425L;
    // Fields
    private final int clientId;
    private final String playerName;
    private Role role;

    // Constructors
    public Player(int clientId, String playerName) {
        this.clientId = clientId;
        this.playerName = playerName;
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
}
