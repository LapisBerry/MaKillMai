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
    private int indexOfCurrentPlayerTurn;
    private boolean isGameStart;


    // Constructor
    private GameController() {
        // get readyPlayers from LobbyController instance
        initGame(LobbyController.getInstance().getReadyPlayers());
    }

    private void initGame(ArrayList<Player> players) {
        board = new Board(players);
        rotPool = new RotPool();

        // give one maxHp to The Emperor and the starting player is The Emperor
        for (int i = 0; i < board.getCircleOfPlayers().size(); ++i) {
            Player player = board.getCircleOfPlayers().get(i);
            if (player.getRole() == Role.EMPEROR) {
                indexOfCurrentPlayerTurn = i;
                player.getCharacter().setMaxHp(player.getCharacter().getMaxHp() + 1);
                player.getCharacter().setHp(player.getCharacter().getMaxHp() + 1);
                break;
            }
        }
    }


    // Methods
    public boolean isGameStart() {
        return isGameStart;
    }

    public boolean isGameOver() {
        return emperorCount() == 0 || rebelCount() + spyCount() == 0 || board.size() <= 1;
    }

    public void endGame() {
        instance = null;
    }

    private void startGame() {
        isGameStart = true;
        // TODO: implement this method
    }

    private void checkDeadPlayer() {
        for (int i = 0; i < board.size(); ++i) {
            Player player = board.getCircleOfPlayers().get(i);
            if (player.getCharacter().getHp() <= 0) {
                // remove player from board
                removePlayer(i);
                --i;
            }
        }
    }

    private void removePlayer(Player player) {
        removePlayer(board.indexOf(player));
    }

    private void removePlayer(int index) {
        board.remove(index);
        // re-index of current player turn
        if (index < indexOfCurrentPlayerTurn) --indexOfCurrentPlayerTurn;
        else indexOfCurrentPlayerTurn = indexOfCurrentPlayerTurn % board.size();
    }

    // when player ends turn by themselves, not by dying
    private void playerEndsTurn() {
        indexOfCurrentPlayerTurn = (indexOfCurrentPlayerTurn + 1) % board.size();
    }

    // count remaining roles
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

    public int getIndexOfCurrentPlayerTurn() {
        return indexOfCurrentPlayerTurn;
    }

    public void setIndexOfCurrentPlayerTurn(int indexOfCurrentPlayerTurn) {
        this.indexOfCurrentPlayerTurn = indexOfCurrentPlayerTurn;
    }
}
