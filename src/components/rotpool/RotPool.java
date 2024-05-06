package components.rotpool;

import components.bases.BaseCharacter;
import components.players.Player;
import config.CONFIG;
import controller.GameController;

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
