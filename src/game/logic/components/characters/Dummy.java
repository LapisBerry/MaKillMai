package game.logic.components.characters;

import game.config.GameConfig;
import game.logic.components.bases.BaseCharacter;

public class Dummy extends BaseCharacter {
    public Dummy() {
        super("Dummy", GameConfig.BASE_STARTING_HEALTH, GameConfig.BASE_STARTING_HEALTH, GameConfig.BASE_STARTING_ROT_POWER, "I'm a dummy, I have no ability.");
    }
}
