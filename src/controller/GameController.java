package controller;

import components.bases.BaseCharacter;
import components.boards.Board;
import components.players.Player;
import components.players.Role;
import components.rotpool.RotPool;

import java.util.ArrayList;

public class GameController {
    // Field
    private static GameController instance = null; // Instance
    private Board board;
    private RotPool rotPool;


    // Constructor
    private GameController() {
        initGame();
    }

    private void initGame() {
        // TODO: mockPlayers
        ArrayList<Player> mockPlayers = new ArrayList<>();
        mockPlayers.add(new Player("Player E", new BaseCharacter(), Role.EMPEROR));
        mockPlayers.add(new Player("Player B", new BaseCharacter(), Role.REBEL));
        mockPlayers.add(new Player("Player B", new BaseCharacter(), Role.REBEL));
        mockPlayers.add(new Player("Player G", new BaseCharacter(), Role.ROYALTY));
        mockPlayers.add(new Player("Player S", new BaseCharacter(), Role.SPY));
        // TODO: mockPlayers
        board = new Board(mockPlayers);
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

    public int royaltyCount() {
        return (int)board.getCircleOfPlayers().stream().filter(player -> player.getRole() == Role.ROYALTY).count();
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
