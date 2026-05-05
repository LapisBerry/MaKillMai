package com.lapisberry.game.entities.characters;

import com.lapisberry.game.entities.dice.DieFace;

import java.io.Serial;

/** "You can use (1) and (2) results to hit players at one further distance than normal." */
public final class RoseDoolan extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public RoseDoolan() {
        super("Rose Doolan",
                "ATTACK_1 and ATTACK_2 dice may hit one extra distance.",
                9);
    }

    @Override
    public boolean canHitAtDistance(DieFace face, int distance) {
        if (face == DieFace.ATTACK_1) return distance == 1 || distance == 2;
        if (face == DieFace.ATTACK_2) return distance == 2 || distance == 3;
        return false;
    }
}
