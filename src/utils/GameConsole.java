package utils;

import game.logic.controller.GameController;

/**
 * The {@code GameConsole} class is a utility class to print messages to the console.
 */
public class GameConsole {
    public static void printStartGame() {
        horizontalBreakLine();
        System.out.println("Starting game...");
        System.out.println("Welcome to \"MaKillMai\"");
        horizontalBreakLine();
    }

    public static void printStartLobby() {
        horizontalBreakLine();
        System.out.println("In lobby");
        horizontalBreakLine();
        System.out.println("What do you want to do? (Type the number)");
        horizontalBreakLine();
        System.out.println("<1> Add player");
        System.out.println("<2> Remove player");
        System.out.println("<3> Show All players in lobby");
        System.out.println("<4> Start game (minimum 4 players, maximum 10 players)");
        // TODO: prompt user to input number
    }

    public static void printPlayersInLobby() {
        horizontalBreakLine();
        System.out.println("Players in lobby:");
        for (int i = 0; i < GameController.getInstance().getBoard().getCircleOfPlayers().size(); ++i) {
            System.out.println("index: " + i + " - " + GameController.getInstance().getBoard().getCircleOfPlayers().get(i).getName());
        }
        horizontalBreakLine();
    }

    public static void printPlayersInBoard() {
        System.out.println("Players in board:");
        for (int i = 0; i < GameController.getInstance().getBoard().getCircleOfPlayers().size(); ++i) {
            System.out.println("index: " + i + " - " + GameController.getInstance().getBoard().getCircleOfPlayers().get(i).getName());
        }
        horizontalBreakLine();
    }

    public static void printPlayerTurn() {
        System.out.println("Start turn of player index: " + GameController.getInstance().getIndexOfCurrentPlayerTurn());
        System.out.println("Player name: " + GameController.getInstance().getBoard().getCircleOfPlayers().get(GameController.getInstance().getIndexOfCurrentPlayerTurn()).getName());
        horizontalBreakLine();
    }

    private static void horizontalBreakLine() {
        System.out.println("--------------------------------------------------");
    }
}
