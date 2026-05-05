package com.lapisberry.game.entities.characters;

import java.io.Serial;

/** "You only need 2 Gatling Gun results to activate the Gatling (instead of 3)." */
public final class WillyTheKid extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public WillyTheKid() {
        super("Willy the Kid",
                "Pure Magic activates with 2 dice instead of 3.",
                8);
    }

    @Override
    public int getDiceRequiredForPureMagic() {
        return 2;
    }
}
