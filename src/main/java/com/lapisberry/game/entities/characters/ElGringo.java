package com.lapisberry.game.entities.characters;

import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.entities.players.Player;

import java.io.Serial;

/** "When a player makes you lose life points, they must take an Arrow." */
public final class ElGringo extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public ElGringo() {
        super("El Gringo",
                "When a player makes you lose life points, they must take an Arrow.",
                7);
    }

    @Override
    public void onTakeAttackDamage(GameController gc, Player self, Player source) {
        if (source == null || source == self || !source.isAlive()) return;
        gc.giveRot(source, 1);
    }
}
