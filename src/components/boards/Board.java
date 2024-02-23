package components.boards;

import java.util.ArrayList;

import components.players.Player;

public class Board {
    // Fields
    private ArrayList<Player> circleOfPlayers;


    // Constructors
    public Board() {
        // TODO: implement constructor
    }

    // Methods
    public int indexOf(Player player) {
        return circleOfPlayers.indexOf(player);
    }

    public void setCircleOfPlayers(ArrayList<Player> circleOfPlayers) {
        this.circleOfPlayers = circleOfPlayers;
    }

    public int distanceBetween(Player player1, Player player2) {
        int indexOfPlayer1 = circleOfPlayers.indexOf(player1);
        int indexOfPlayer2 = circleOfPlayers.indexOf(player2);
        
        int maxMinuxMin = Math.max(indexOfPlayer1, indexOfPlayer2) - Math.min(indexOfPlayer1, indexOfPlayer2);

        if (!(maxMinuxMin <= circleOfPlayers.size() / 2)) {
            return circleOfPlayers.size() - maxMinuxMin;
        }
        return maxMinuxMin;
    }
    
    public ArrayList<Player> getCircleOfPlayers() {
        return circleOfPlayers;
    }
}
