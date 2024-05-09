package gameTest.componentsTest.boardsTest;

import game.logic.components.boards.Board;
import game.logic.components.characters.Dummy;
import game.logic.components.players.Player;
import game.logic.components.players.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {
    Board board;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player player5;
    ArrayList<Player> players;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1", new Dummy(), Role.EMPEROR);
        player2 = new Player("Player2", new Dummy(), Role.REBEL);
        player3 = new Player("Player3", new Dummy(), Role.REBEL);
        player4 = new Player("Player4", new Dummy(), Role.SPY);
        player5 = new Player("Player5", new Dummy(), Role.ROYALIST);

        players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4, player5));
        board = new Board(players);
    }

    @Test
    void indexOfPlayerInBoard() {
        assertEquals(0, board.indexOf(player1));
        assertEquals(1, board.indexOf(player2));
        assertEquals(2, board.indexOf(player3));
        assertEquals(3, board.indexOf(player4));
        assertEquals(4, board.indexOf(player5));
    }

    @Test
    void indexOfPlayerNotInBoard() {
        assertEquals(-1, board.indexOf(new Player("Player6", new Dummy(), Role.EMPEROR)));
        assertEquals(-1, board.indexOf(new Player("Player7")));
    }

    @Test
    void distanceBetweenPlayersInBoard() {
        assertEquals(1, board.distanceBetween(player1, player2));
        assertEquals(1, board.distanceBetween(player2, player3));
        assertEquals(1, board.distanceBetween(player3, player4));
        assertEquals(1, board.distanceBetween(player4, player5));
        assertEquals(1, board.distanceBetween(player5, player1));

        assertEquals(2, board.distanceBetween(player1, player3));
        assertEquals(2, board.distanceBetween(player2, player4));
        assertEquals(2, board.distanceBetween(player3, player5));
        assertEquals(2, board.distanceBetween(player4, player1));
        assertEquals(2, board.distanceBetween(player5, player2));

        assertEquals(2, board.distanceBetween(player1, player4));
        assertEquals(2, board.distanceBetween(player2, player5));
        assertEquals(2, board.distanceBetween(player3, player1));
        assertEquals(2, board.distanceBetween(player4, player2));
        assertEquals(2, board.distanceBetween(player5, player3));

        assertEquals(1, board.distanceBetween(player1, player5));
        assertEquals(1, board.distanceBetween(player2, player1));
        assertEquals(1, board.distanceBetween(player3, player2));
        assertEquals(1, board.distanceBetween(player4, player3));
        assertEquals(1, board.distanceBetween(player5, player4));
    }

    @Test
    void distanceBetweenPlayerInBoardAndPlayerNotInBoard() {
        assertEquals(-1, board.distanceBetween(player1, new Player("Player6", new Dummy(), Role.EMPEROR)));
        assertEquals(-1, board.distanceBetween(player2, new Player("Player7")));
    }

    @Test
    void addPlayerToBoard() {
        Player player6 = new Player("Player6", new Dummy(), Role.REBEL);
        board.add(player6);
        assertEquals(6, board.size());

        assertEquals(1, board.distanceBetween(player1, player2));
        assertEquals(1, board.distanceBetween(player2, player3));
        assertEquals(1, board.distanceBetween(player3, player4));
        assertEquals(1, board.distanceBetween(player4, player5));
        assertEquals(1, board.distanceBetween(player5, player6));
        assertEquals(1, board.distanceBetween(player6, player1));

        assertEquals(2, board.distanceBetween(player1, player3));
        assertEquals(2, board.distanceBetween(player2, player4));
        assertEquals(2, board.distanceBetween(player3, player5));
        assertEquals(2, board.distanceBetween(player4, player6));
        assertEquals(2, board.distanceBetween(player5, player1));
        assertEquals(2, board.distanceBetween(player6, player2));

        assertEquals(3, board.distanceBetween(player1, player4));
        assertEquals(3, board.distanceBetween(player2, player5));
        assertEquals(3, board.distanceBetween(player3, player6));
        assertEquals(3, board.distanceBetween(player4, player1));
        assertEquals(3, board.distanceBetween(player5, player2));
        assertEquals(3, board.distanceBetween(player6, player3));
    }

    @Test
    void removePlayerFromBoard() {
        board.remove(player1);
        assertEquals(4, board.size());

        assertEquals(1, board.distanceBetween(player2, player3));
        assertEquals(1, board.distanceBetween(player3, player4));
        assertEquals(1, board.distanceBetween(player4, player5));
        assertEquals(1, board.distanceBetween(player5, player2));

        assertEquals(2, board.distanceBetween(player2, player4));
        assertEquals(2, board.distanceBetween(player3, player5));
        assertEquals(2, board.distanceBetween(player4, player2));
        assertEquals(2, board.distanceBetween(player5, player3));

        assertEquals(1, board.distanceBetween(player2, player5));
        assertEquals(1, board.distanceBetween(player3, player2));
        assertEquals(1, board.distanceBetween(player4, player3));
        assertEquals(1, board.distanceBetween(player5, player4));
    }

    @Test
    void sizeOfBoard() {
        assertEquals(5, board.size());
    }

    @Test
    void getCircleOfPlayers() {
        assertEquals(players, board.getCircleOfPlayers());
    }
}