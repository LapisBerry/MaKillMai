package com.lapisberry.net.packets;

import com.lapisberry.game.controllers.LobbyController;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code LobbyPacket} class is the packet sending from server to client to update the lobby.
 */
public class LobbyPacket extends ServerPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 5601782103675952467L;
    // Fields
    private final LobbyController lobbyController;

    // Constructors
    public LobbyPacket(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    // Methods
    @Override
    public String toString() {
        return "LobbyPacket{lobbyController=" + lobbyController + "}";
    }

    // Getters
    public LobbyController getLobbyController() {
        return lobbyController;
    }
}
