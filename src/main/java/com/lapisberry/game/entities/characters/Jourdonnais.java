package com.lapisberry.game.entities.characters;

import java.io.Serial;

/** "You never lose more than 1 HP from an Indian Attack." */
public final class Jourdonnais extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public Jourdonnais() {
        super("Jourdonnais",
                "You never lose more than 1 HP from an Indian Attack.",
                7);
    }

    @Override
    public int onRotAttackDamage(int rotHeld) {
        return Math.min(rotHeld, 1);
    }
}
