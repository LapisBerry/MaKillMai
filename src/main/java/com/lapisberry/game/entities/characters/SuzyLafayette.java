package com.lapisberry.game.entities.characters;

import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.entities.dice.DieFace;
import com.lapisberry.game.entities.players.Player;

import java.io.Serial;

/** "If you didn't roll any (1) or (2) results, you gain 2 HP." */
public final class SuzyLafayette extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public SuzyLafayette() {
        super("Suzy Lafayette",
                "If you rolled no ATTACK_1 or ATTACK_2 dice, gain 2 HP.",
                8);
    }

    @Override
    public void onAfterRollingPhase(GameController gc, Player self) {
        int attacks = gc.getDiePool().countFace(DieFace.ATTACK_1)
                + gc.getDiePool().countFace(DieFace.ATTACK_2);
        if (attacks == 0) heal(2);
    }
}
