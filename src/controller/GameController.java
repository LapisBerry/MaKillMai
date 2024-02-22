package controller;

public class GameController {
    // Field
    private static GameController instance = null; // Instance
    private boolean isGameStart = false;
    // TODO: add more fields

    // Constructor
    public GameController() {
        // TODO: implement constructor
    }


    // Getter Setter
    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public boolean isGameStart() {
        return isGameStart;
    }

    public void setGameStart(boolean isGameStart) {
        this.isGameStart = isGameStart;
    }
}
