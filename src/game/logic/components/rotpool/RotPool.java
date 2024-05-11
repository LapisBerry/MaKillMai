package game.logic.components.rotpool;

import game.config.CONFIG;
import game.logic.components.bases.BaseCharacter;
import game.logic.components.players.Player;
import game.logic.controller.GameController;

/**
 * The {@code RotPool} class represents the rot pool in the game.
 * <p>
 *     When the player tosses the dice and gets the rot power symbol, the player takes one rot power from the rot pool.
 * <p>
 *     When the rot power is out, all the players take one rot power damage and the rot pool is reset.
 */
public class RotPool {
    // Fields
    private int rotPower;


    // Constructor
    public RotPool() {
        rotPower = CONFIG.ROT_POOL;
    }


    // Methods
    public void giveOneRotPower(BaseCharacter baseCharacter) {
        --rotPower;
        baseCharacter.setRotPower(baseCharacter.getRotPower() + 1);
        if (rotPower <= 0) resetRotPower();
    }

    public void takeOneRotPower(BaseCharacter baseCharacter) {
        ++rotPower;
        baseCharacter.setRotPower(baseCharacter.getRotPower() - 1);
    }

    public void clearRotPower(BaseCharacter baseCharacter) {
        rotPower += baseCharacter.getRotPower();
        baseCharacter.setRotPower(0);
    }

    public void resetRotPower() {
        for (Player player : GameController.getInstance().getBoard().getCircleOfPlayers()) {
            BaseCharacter character = player.getCharacter();
            character.takeRotPowerDamage();
            clearRotPower(character);
        }
        rotPower = CONFIG.ROT_POOL;
    }


    // Getter Setter
    public int getRotPower() {
        return rotPower;
    }
}
