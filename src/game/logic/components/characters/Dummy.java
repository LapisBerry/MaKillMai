package game.logic.components.characters;

import game.config.CONFIG;
import game.logic.components.bases.BaseCharacter;

public class Dummy extends BaseCharacter {
    public Dummy() {
        super("Dummy", CONFIG.BASE_STARTING_HEALTH, CONFIG.BASE_STARTING_HEALTH, CONFIG.BASE_STARTING_ROT_POWER, "I'm a dummy, I have no ability.");
    }
}
