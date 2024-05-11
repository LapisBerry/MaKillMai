package game.logic.controller;

import game.config.RoleCount;
import game.logic.components.players.Player;
import game.logic.components.players.Role;
import utils.Randomizer;

import java.util.ArrayList;

/**
 * The {@code LobbyController} class keeps instance which player is in the lobby.
 * <p>
 *     This class will manage the players in the lobby.
 * <p>
 *     This class will do role assignment, character picking, player removal and player addition.
 */
public class LobbyController {
    // Fields
    private static LobbyController instance = null;
    private final ArrayList<Player> players; // only username is available when using this property
    private final ArrayList<Player> readyPlayers; // after role picking and shuffled position this variable is ready to use and will be sent to GameController to be used in Board


    // Constructors
    private LobbyController() {
        players = new ArrayList<>();
        readyPlayers = new ArrayList<>();
    }


    // Methods
    public void setUpReadyPlayers() {
        readyPlayers.clear();
        readyPlayers.addAll(players);

        // shuffle readyPlayers first to assign the role in random to any player
        Randomizer.shuffleArrayList(readyPlayers);
        // assign role
        assignRole();
        // shuffle readyPlayers again to make the position random
        Randomizer.shuffleArrayList(readyPlayers);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    private void assignRole() {
        int size = readyPlayers.size();
        int royalistCount = RoleCount.getRoyalists(size);
        int rebelCount = RoleCount.getRebels(size);
        int spyCount = RoleCount.getSpies(size);

        readyPlayers.getFirst().setRole(Role.EMPEROR);
        for (int i = 1; i < royalistCount + 1; ++i) {
            readyPlayers.get(i).setRole(Role.ROYALIST);
        }
        for (int i = royalistCount + 1; i < royalistCount + rebelCount + 1; ++i) {
            readyPlayers.get(i).setRole(Role.REBEL);
        }
        for (int i = royalistCount + rebelCount + 1; i < royalistCount + rebelCount + spyCount + 1; ++i) {
            readyPlayers.get(i).setRole(Role.SPY);
        }
    }


    // Getter Setter
    public static LobbyController getInstance() {
        if (instance == null) {
            instance = new LobbyController();
        }
        return instance;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> getReadyPlayers() {
        return readyPlayers;
    }
}
