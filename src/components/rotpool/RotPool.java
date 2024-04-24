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
    public void giveARotPower(BaseCharacter baseCharacter) {
        rotPower--;
        baseCharacter.setRotPower(baseCharacter.getRotPower() + 1);
        if (rotPower <= 0)
            resetRotPower();
    }

    public void resetRotPower() {
        for (Player player : GameController.getInstance().getBoard().getCircleOfPlayers()) {
            BaseCharacter character = player.getCharacter();
            character.takeRotPowerDamage();
        }
        rotPower = CONFIG.ROT_POOL;
    }
}
