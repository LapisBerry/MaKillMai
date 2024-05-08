package game.logic.controller;

import game.logic.components.boards.Board;
import game.logic.components.players.Player;
import game.logic.components.players.Role;
import game.logic.components.rotpool.RotPool;

import java.util.ArrayList;

/**
 * The {@code GameController} class keep the game instance.
 * <p>
 *     It has a board and a rotPool.
 * <p>
 *     It has methods to remove a player, check if the game is over, end the game, and count the number of players with a certain role.
 */
public class GameController {
    // Field
    private static GameController instance = null; // Instance
    private Board board;
    private RotPool rotPool;


    // Constructor
    private GameController() {
        initGame(LobbyController.getInstance().getReadyPlayers());
    }

    private void initGame(ArrayList<Player> players) {
        board = new Board(players);
        rotPool = new RotPool();
    }


    // Methods
    private void removePlayer(Player player) {
        board.remove(player);
    }

    public boolean isGameStart() {
        return instance != null;
    }

    public boolean isGameOver() {
        return emperorCount() == 0 || rebelCount() + spyCount() == 0 || board.size() <= 1;
    }

    public void endGame() {
        instance = null;
    }

    public int emperorCount() {
        return (int)board.getCircleOfPlayers().stream().filter(player -> player.getRole() == Role.EMPEROR).count();
    }

    public int rebelCount() {
        return (int)board.getCircleOfPlayers().stream().filter(player -> player.getRole() == Role.REBEL).count();
    }

    public int royalistCount() {
        return (int)board.getCircleOfPlayers().stream().filter(player -> player.getRole() == Role.ROYALIST).count();
    }

    public int spyCount() {
        return (int)board.getCircleOfPlayers().stream().filter(player -> player.getRole() == Role.SPY).count();
    }


    // Getter Setter
    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public Board getBoard() {
        return board;
    }

    public RotPool getRotPool() {
        return rotPool;
    }
}
