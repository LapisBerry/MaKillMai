package gameTest.componentsTest.playersTest;

import game.logic.components.characters.Dummy;
import game.logic.components.players.Player;
import game.logic.components.players.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player1;
    Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1");
        player2 = new Player("Player2", new Dummy(), Role.EMPEROR);
    }

    @Test
    void constructorsTest() {
        player1 = new Player("Player1");
        player2 = new Player("Player2", new Dummy(), Role.EMPEROR);

        assertEquals("Player1", player1.getName());
        assertNull(player1.getCharacter());
        assertNull(player1.getRole());

        assertEquals("Player2", player2.getName());
        assertEquals("Dummy", player2.getCharacter().getName());
        assertEquals(Role.EMPEROR, player2.getRole());
    }

    @Test
    void fullInfo() {
        assertThrows(NullPointerException.class, () -> player1.fullInfo());
        assertEquals("Player2 | Dummy | EMPEROR", player2.fullInfo());
    }

    @Test
    void getName() {
        assertEquals("Player1", player1.getName());
        assertEquals("Player2", player2.getName());
    }

    @Test
    void setName() {
        player1.setName("Player1New");
        player2.setName("Player2New");

        assertEquals("Player1New", player1.getName());
        assertEquals("Player2New", player2.getName());
    }

    @Test
    void getCharacter() {
        assertNull(player1.getCharacter());
        assertEquals("Dummy", player2.getCharacter().getName());
    }

    @Test
    void setCharacter() {
        player1.setCharacter(new Dummy());
        player2.setCharacter(null);

        assertEquals("Dummy", player1.getCharacter().getName());
        assertNull(player2.getCharacter());
    }

    @Test
    void getRole() {
        assertNull(player1.getRole());
        assertEquals(Role.EMPEROR, player2.getRole());
    }

    @Test
    void setRole() {
        player1.setRole(Role.EMPEROR);
        player2.setRole(null);

        assertEquals(Role.EMPEROR, player1.getRole());
        assertNull(player2.getRole());
    }
}