package utils;

import game.logic.components.characters.Dummy;
import game.logic.components.players.Player;
import game.logic.components.players.Role;
import game.logic.controller.GameController;
import game.logic.controller.LobbyController;

import java.util.Scanner;

import static utils.GameConsole.printChooseAction;
import static utils.GameConsole.printPlayerDicePool;

/**
 * The {@code MainBareBone} class runs game only logic without any user interface by using console only.
 */
public class MainBareBone {
    // Fields
    private static LobbyController lc;
    private static GameController gc;
    private static final Scanner scanner = new Scanner(System.in);
    static {
        scanner.useDelimiter("\n");
    }

    public static void main(String[] args) {
        GameConsole.printStartGame();
        startLobby();
    }

    private static void startLobby() {
        lc = LobbyController.getInstance();
        while (true) {
            GameConsole.printStartLobby();
            printChooseAction("Add player", "Remove player", "Show All players in lobby", "Start game", "Exit game");
            int choice = inputCheck(1, 5);
            switch (choice) {
                case 1:
                    System.out.println("Enter player name:");
                    lc.addPlayer(new Player(scanner.next(), new Dummy(), Role.EMPEROR));
                    break;
                case 2:
                    GameConsole.printPlayersInLobby();
                    if (lc.getPlayers().isEmpty()) {
                        System.out.println("No player to remove");
                        break;
                    }
                    System.out.println("Enter player index to remove:");
                    lc.removePlayer(inputCheck(0, lc.getPlayers().size() - 1));
                    break;
                case 3:
                    GameConsole.printPlayersInLobby();
                    break;
                case 4:
                    if (lc.getPlayers().size() < 4 || lc.getPlayers().size() > 10) {
                        System.out.println("Minimum 4 players, maximum 10 players");
                    } else {
                        lc.setUpReadyPlayers();
                        startGame();
                    }
                    break;
            }
            if (choice == 5) break;
        }
        System.out.println("Exiting game...");
    }

    private static void startTurn(int indexOfCurrentPlayerTurn) {
    }

    private static void startGame() {
        gc = GameController.getInstance();
        Player turnOwner;
        while (!gc.isGameOver()) {
            GameConsole.printPlayersInBoard();
            GameConsole.printPlayerTurn();
            turnOwner = gc.getBoard().getCircleOfPlayers().get(gc.getIndexOfCurrentPlayerTurn());
            turnOwner.getCharacter().startOfTurn();

            printChooseAction("Roll dice");
            inputCheck(1, 1);
            turnOwner.getCharacter().getDicePool().rollAllUnlockedDices();
            turnOwner.getCharacter().setReRollLeft(turnOwner.getCharacter().getReRollLeft() - 1);

            turnOwner.getCharacter().resolveRolledDice();
            gc.checkAndClearDeadPlayer();
            if (gc.isGameOver()) break;
            if (turnOwner.getCharacter().getHp() <= 0) continue;

            while (turnOwner.getCharacter().getReRollLeft() > 0) {
                printPlayerDicePool(turnOwner);
                printChooseAction("Roll dice", "Lock dice", "Unlock dice", "Stop rolling dice");
                int choice = inputCheck(1, 4);
                switch (choice) {
                    case 1:
                        turnOwner.getCharacter().getDicePool().rollAllUnlockedDices();
                        turnOwner.getCharacter().setReRollLeft(turnOwner.getCharacter().getReRollLeft() - 1);
                        turnOwner.getCharacter().resolveRolledDice();
                        break;
                    case 2:
                        System.out.println("Enter dice index to lock:");
                        turnOwner.getCharacter().getDicePool().lockDiceAt(inputCheck(0, turnOwner.getCharacter().getDicePool().getDiceArray().length - 1));
                        break;
                    case 3:
                        System.out.println("Enter dice index to unlock:");
                        turnOwner.getCharacter().getDicePool().unlockDiceAt(inputCheck(0, turnOwner.getCharacter().getDicePool().getDiceArray().length - 1));
                        break;
                }
                if (choice == 4) break;
            }
        }
        /*
        while gameOn:
            turnOwner(indexOfCurrentPlayerTurn)
            chooseAction(RollDice) // force roll dices
            resolveRolledDice()

            if checkDeadPlayer():
                revealRole()
                removePlayerFromBoard()
                if isGameOver():
                    break
                elif isTurnOwnerDead():
                    continue // skip to next player turn

            while hasReRoll():
                chooseAction(RollDice, lockDice, unlockDice, stopRollDice)
                if stopRollDice:
                    break
                rollUnlockedDice()
                resolveRolledDice()
                if checkDeadPlayer():
                revealRole()
                removePlayerFromBoard()
                    if isGameOver():
                        hardBreak
                    elif isTurnOwnerDead():
                        continue // skip to next player turn

            chooseAction(chooseTargetAction) // choose all dice target action
            resolveAction()

            if checkDeadPlayer():
                revealRole()
                removePlayerFromBoard()
                if isGameOver():
                    break
                elif isTurnOwnerDead():
                    continue // skip to next player turn
            chooseAction(endTurn)
        endGame()
         */
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
