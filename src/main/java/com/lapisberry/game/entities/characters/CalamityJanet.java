package com.lapisberry.game.entities.characters;

import com.lapisberry.game.entities.dice.DieFace;

import java.io.Serial;

/** "You may use (1) results as (2) and vice versa." */
public final class CalamityJanet extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public CalamityJanet() {
        super("Calamity Janet",
                "ATTACK_1 and ATTACK_2 dice may hit either distance 1 or 2.",
                8);
    }

    @Override
    public boolean canHitAtDistance(DieFace face, int distance) {
        if (face == DieFace.ATTACK_1 || face == DieFace.ATTACK_2) {
            return distance == 1 || distance == 2;
        }
        return false;
    }
}
