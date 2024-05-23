package com.lapisberry.game.controllers;

import javafx.util.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class LobbyController implements Serializable {
    @Serial
    private static final long serialVersionUID = -3466527055512798470L;
    // Fields
    private ArrayList<Pair<Integer, String>> players; // Pair<clientId, playerName>

    // Constructors
    public LobbyController() {
        players = new ArrayList<>();
    }

    // Methods
    public void addPlayer(int clientId, String playerName) {
        players.add(new Pair<>(clientId, playerName));
    }

    public void removePlayer(int clientId) {
        players.removeIf(pair -> pair.getKey() == clientId);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Pair<Integer, String> player : players) {
            stringBuilder.append(player.getKey()).append("=").append(player.getValue()).append(", ");
        }
        return "LobbyController{players=" + stringBuilder + "}";
    }

    // Getters Setters
    public ArrayList<Pair<Integer, String>> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Pair<Integer, String>> players) {
        this.players = players;
    }
}
