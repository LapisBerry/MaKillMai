package com.lapisberry.net.packets;

import javafx.util.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code LobbyPacket} class is the packet sending from server to client to update the lobby.
 */
public class LobbyPacket extends ServerPacket implements Serializable {
    @Serial
    private static final long serialVersionUID = 5601782103675952467L;
    // Fields
    private final ArrayList<Pair<Integer, String>> players;

    // Constructors
    public LobbyPacket(ArrayList<Pair<Integer, String>> players) {
        // deep copy
        this.players = new ArrayList<>(players);
    }

    // Methods
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Pair<Integer, String> player : players) {
            stringBuilder.append(player.getKey()).append("=").append(player.getValue()).append(", ");
        }
        return "LobbyPacket{lobbyController=" + stringBuilder + "}";
    }

    // Getters
    public ArrayList<Pair<Integer, String>> getPlayers() {
        return players;
    }
}
