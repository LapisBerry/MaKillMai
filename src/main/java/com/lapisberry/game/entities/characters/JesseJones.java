package com.lapisberry.game.entities.characters;

import java.io.Serial;

/** "If you have 4 HP or less, you gain 2 HP when you use a Beer on yourself (instead of 1)." */
public final class JesseJones extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public JesseJones() {
        super("Jesse Jones",
                "If at 4 HP or less, gain 2 HP when using a Beer on yourself.",
                9);
    }

    @Override
    public int onHealReceived(boolean fromSelf, int amount) {
        if (fromSelf && getHp() <= 4) return amount + 1;
        return amount;
    }
}
