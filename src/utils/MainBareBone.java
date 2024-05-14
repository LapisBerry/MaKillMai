package utils;

import game.logic.components.bases.BaseCharacter;
import game.logic.components.characters.Dummy;
import game.logic.components.dices.DicePool;
import game.logic.components.players.Player;
import game.logic.components.players.Role;
import game.logic.controller.GameController;
import game.logic.controller.LobbyController;

import java.util.Scanner;

import static utils.GameConsole.*;

/**
 * The {@code MainBareBone} class runs game only logic without any user interface by using console only.
 */
public class MainBareBone {
    // Fields
    private static LobbyController lc;
    private static GameController gc;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        GameConsole.printStartGame();
        startLobby();
    }

    private static void startLobby() {
        lc = LobbyController.getInstance();
        GameConsole.printStartLobby();
        inLobby();
        System.out.println("Exiting game...");
    }

    private static void inLobby() {
        while (true) {
            printChooseAction("Add player", "Remove player", "Show All players in lobby", "Start game", "Exit game");
            int choice = inputCheck(1, 5);
            switch (choice) {
                case 1 -> addNewPlayerInLobby();
                case 2 -> removingPlayerInLobby();
                case 3 -> printPlayersInLobby();
                case 4 -> startGame();
                case 5 -> {
                    return;
                }
            }
        }
    }

    private static void addNewPlayerInLobby() {
        System.out.println("Enter player name:");
        scanner.skip("\n");
        lc.addPlayer(new Player(scanner.nextLine(), new Dummy(), Role.EMPEROR));
    }

    private static void removingPlayerInLobby() {
        printPlayersInLobby();
        if (lc.getPlayers().isEmpty()) {
            System.out.println("No player to remove");
            return;
        }
        System.out.println("Enter player index to remove:");
        lc.removePlayer(inputCheck(0, lc.getPlayers().size() - 1));
    }

    private static void startGame() {
        if (lc.getPlayers().size() < 4 || lc.getPlayers().size() > 10) {
            System.out.println("Minimum 4 players, maximum 10 players");
            return;
        }
        gc = GameController.getInstance();
        while (!gc.isGameOver()) {
            startTurn(gc.getIndexOfCurrentPlayerTurn());
        }
    }

    private static void startTurn(int indexOfCurrentPlayerTurn) {
        // this is turnOwner
        Player turnOwner = gc.getBoard().getCircleOfPlayers().get(gc.getIndexOfCurrentPlayerTurn());
        BaseCharacter character = turnOwner.getCharacter();
        character.startOfTurn();
        // print start turn
        printPlayersInBoard();
        printPlayerTurn();

        // state roll dice
        turnStateRollDice(turnOwner);

        // if isGameOver, end game or if player is dead, skip to next player
        if (gc.isGameOver() || character.getHp() <= 0) return;

        // state resolve action
        turnStateResolveAction(turnOwner);

        if (gc.isGameOver() || character.getHp() <= 0) return;

        // state end turn
        turnStateEndTurn(turnOwner);
    }

    private static void turnStateRollDice(Player turnOwner) {
        BaseCharacter character = turnOwner.getCharacter();
        while (character.hasReRollLeft()) {
            printPlayerDicePool(turnOwner);
            int choice;
            if (character.getReRollLeft() == character.getRollPerTurn()) {
                printChooseAction("Roll dice");
                choice = inputCheck(1, 1);
            } else {
                printChooseAction("Roll dice", "Lock dice", "Unlock dice", "Stop rolling dice");
                choice = inputCheck(1, 4);
            }
            switch (choice) {
                case 1 -> playerChoosesRollDice(turnOwner);
                case 2 -> playerChoosesLockDice(turnOwner);
                case 3 -> playerChoosesUnlockDice(turnOwner);
                case 4 -> {
                    return;
                }
            }
            // after resolve rolled dice, players can die
            gc.checkAndClearDeadPlayer();
            if (gc.isGameOver() || character.getHp() <= 0) return;
        }
    }

    private static void playerChoosesRollDice(Player turnOwner) {
        BaseCharacter character = turnOwner.getCharacter();
        character.rollAllUnlockedDices();
        character.resolveRolledDice();
    }

    private static void playerChoosesLockDice(Player turnOwner) {
        DicePool dicePool = turnOwner.getCharacter().getDicePool();
        System.out.println("Enter dice index to lock: (type " + dicePool.getDiceArray().length + " to go back)");
        printChooseAction();
        int index = inputCheck(0, dicePool.getDiceArray().length);
        if (index == dicePool.getDiceArray().length) return;
        dicePool.lockDiceAt(index);
    }

    private static void playerChoosesUnlockDice(Player turnOwner) {
        DicePool dicePool = turnOwner.getCharacter().getDicePool();
        System.out.println("Enter dice index to unlock: (type " + dicePool.getDiceArray().length + " to go back)");
        printChooseAction();
        int index = inputCheck(0, dicePool.getDiceArray().length);
        if (index == dicePool.getDiceArray().length) return;
        if (dicePool.isUnlockableAt(index)) dicePool.unlockDiceAt(index);
        else System.out.println("Dice can't be unlocked");
    }

    private static void turnStateResolveAction(Player turnOwner) {
        printPlayersInBoard();
        printPlayerDicePool(turnOwner);
        while (true) {
            /*
            using which dice
            if allDice has been choose target
             */
        }
    }

    private static void turnStateEndTurn(Player turnOwner) {
        printPlayersInBoard();
        printChooseAction("End turn");
        inputCheck(1, 1);
        turnOwner.getCharacter().endOfTurn();
        gc.playerEndsTurn();
    }

    private static int inputCheck(int lowestInput, int highestInput) {
        int choice = scanner.nextInt();
        while (choice < lowestInput || choice > highestInput) {
            System.out.println("Invalid choice");
            choice = scanner.nextInt();
        }
        return choice;
    }
}
