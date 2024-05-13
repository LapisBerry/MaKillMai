package utils;

import game.logic.components.players.Player;
import game.logic.controller.GameController;
import game.logic.controller.LobbyController;

import java.util.ArrayList;

/**
 * The {@code GameConsole} class is a utility class to print messages to the console.
 */
public class GameConsole {
    public static void printStartGame() {
        horizontalBreakLine();
        System.out.println("Welcome to \"MaKillMai\"");
    }

    public static void printStartLobby() {
        horizontalBreakLine();
        System.out.println("In lobby");
        horizontalBreakLine();
    }

    public static void printPlayersInLobby() {
        System.out.println("Players in lobby:");
        ArrayList<Player> players = LobbyController.getInstance().getPlayers();
        for (int i = 0; i < players.size(); ++i) {
            System.out.println("index: " + i + " - " + players.get(i).getName());
        }
        horizontalBreakLine();
    }

    public static void printPlayersInBoard() {
        System.out.println("Players in board:");
        for (int i = 0; i < GameController.getInstance().getBoard().getCircleOfPlayers().size(); ++i) {
            System.out.println("index: " + i + " - " + GameController.getInstance().getBoard().getCircleOfPlayers().get(i).fullInfo());
        }
        horizontalBreakLine();
    }

    public static void printPlayerTurn() {
        System.out.println("Start turn of player index: " + GameController.getInstance().getIndexOfCurrentPlayerTurn());
        System.out.println("Player name: " + GameController.getInstance().getBoard().getCircleOfPlayers().get(GameController.getInstance().getIndexOfCurrentPlayerTurn()).getName());
        horizontalBreakLine();
    }

    public static void printChooseAction(String... actions) {
        System.out.println("What do you want to do? (Type the number)");
        horizontalBreakLine();
        for (int i = 0; i < actions.length; ++i) {
            System.out.println("<" + (i + 1) + "> " + actions[i]);
        }
    }

    public static void printPlayerDicePool(Player player) {
        for (int i = 0; i < player.getCharacter().getDicePool().getDiceArray().length; ++i) {
            System.out.println("Dice " + i + ": " + player.getCharacter().getDicePool().getDiceArray()[i].getDiceFace() + ((player.getCharacter().getDicePool().isDiceLockedAt(i)) ? " (locked)" : ""));
        }
        horizontalBreakLine();
    }

    private static void horizontalBreakLine() {
        System.out.println("--------------------------------------------------");
    }
}
