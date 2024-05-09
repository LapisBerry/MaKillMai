package game.logic.components.boards;

import java.util.ArrayList;

import game.logic.components.players.Player;

/**
 * The {@code Board} class
 * <p>
 * This class can tell who is in the board, where are they and the distance between those players.
 *
 * @author LapisBerry
 */
public class Board {
    // Fields
    private final ArrayList<Player> circleOfPlayers;


    // Constructors
    public Board(ArrayList<Player> circleOfPlayers) {
        this.circleOfPlayers = circleOfPlayers;
    }

    // Methods
    public int indexOf(Player player) {
        return circleOfPlayers.indexOf(player);
    }

    /**
     * This method returns the distance between two players.
     * <p>
     * It calculates the "SHORTEST" distance between two players in the circle of players.
     *
     * @param player1 any player in the board
     * @param player2 any player in the board
     * @return the shortest distance between two players, or -1 if any of the player is not in the board.
     */
    public int distanceBetween(Player player1, Player player2) {
        final int indexOfPlayer1 = circleOfPlayers.indexOf(player1);
        final int indexOfPlayer2 = circleOfPlayers.indexOf(player2);

        if (indexOfPlayer1 == -1 || indexOfPlayer2 == -1) return -1;

        final int maxMinusMin = Math.max(indexOfPlayer1, indexOfPlayer2) - Math.min(indexOfPlayer1, indexOfPlayer2);

        return (maxMinusMin > circleOfPlayers.size() / 2) ? circleOfPlayers.size() - maxMinusMin : maxMinusMin;
    }

    public boolean add(Player player) {
        return circleOfPlayers.add(player);
    }

    public boolean remove(Player player) {
        return circleOfPlayers.remove(player);
    }

    public Player remove(int index) {
        return circleOfPlayers.remove(index);
    }

    public int size() {
        return circleOfPlayers.size();
    }


    // Getters Setters
    public ArrayList<Player> getCircleOfPlayers() {
        return circleOfPlayers;
    }
}
