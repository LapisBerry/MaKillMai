package com.lapisberry.game.entities.characters;

import java.io.Serial;

/** "You may re-roll the Dynamite (unless you roll 3 or more, which ends your turn as usual)." */
public final class BlackJack extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public BlackJack() {
        super("Black Jack",
                "You may re-roll the Dynamite (unless you roll 3+, which ends your turn).",
                8);
    }

    @Override
    public boolean canRerollStoneSuppressor() {
        return true;
    }
}
