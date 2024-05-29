package com.lapisberry.game.controllers;

import com.lapisberry.game.entities.characters.CharacterEnum;
import com.lapisberry.game.entities.players.Player;
import com.lapisberry.game.entities.players.Role;
import com.lapisberry.utils.Randomizer;
import com.lapisberry.utils.RoleCountHelper;
import javafx.util.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class LobbyController implements Serializable {
    @Serial
    private static final long serialVersionUID = -5453436562134546375L;
    // Fields
    private ArrayList<Pair<Integer, String>> players; // Pair<clientId, playerName>
    private ArrayList<Player> shuffledPlayers;
    private ArrayList<CharacterEnum> shuffledCharacters;

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

    public void setupShuffledPlayersAndShuffledCharacters() {
        shuffledPlayers = new ArrayList<>();
        shuffledCharacters = new ArrayList<>(Arrays.asList(CharacterEnum.values()));
        for (Pair<Integer, String> player : players) {
            shuffledPlayers.add(new Player(player.getKey(), player.getValue()));
        }
        Randomizer.shuffleArrayList(shuffledPlayers);
        assignRole(shuffledPlayers);
        Randomizer.shuffleArrayList(shuffledPlayers);
        Randomizer.shuffleArrayList(shuffledCharacters);
    }

    private void assignRole(ArrayList<Player> shuffledPlayers) {
        int size = shuffledPlayers.size();
        int emCount = RoleCountHelper.getEmperors(size);
        int roCount = RoleCountHelper.getRoyalists(size);
        int reCount = RoleCountHelper.getRebels(size);
        int spCount = RoleCountHelper.getSpies(size);

        for (int i = 0; i < emCount; ++i) {
            shuffledPlayers.get(i).setRole(Role.EMPEROR);
        }
        for (int i = emCount; i < roCount + emCount; ++i) {
            shuffledPlayers.get(i).setRole(Role.ROYALIST);
        }
        for (int i = roCount + emCount; i < roCount + reCount + emCount; ++i) {
            shuffledPlayers.get(i).setRole(Role.REBEL);
        }
        for (int i = roCount + reCount + emCount; i < roCount + reCount + spCount + emCount; ++i) {
            shuffledPlayers.get(i).setRole(Role.SPY);
        }
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

    public ArrayList<Player> getShuffledPlayers() {
        return shuffledPlayers;
    }

    public void setShuffledPlayers(ArrayList<Player> shuffledPlayers) {
        this.shuffledPlayers = shuffledPlayers;
    }

    public ArrayList<CharacterEnum> getShuffledCharacters() {
        return shuffledCharacters;
    }

    public void setShuffledCharacters(ArrayList<CharacterEnum> shuffledCharacters) {
        this.shuffledCharacters = shuffledCharacters;
    }
}
