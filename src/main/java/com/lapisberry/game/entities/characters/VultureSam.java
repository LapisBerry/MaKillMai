package com.lapisberry.game.entities.characters;

import com.lapisberry.game.controllers.GameController;
import com.lapisberry.game.entities.players.Player;

import java.io.Serial;

/** "Whenever another player is eliminated, you gain 2 HP." */
public final class VultureSam extends BaseCharacter {
    @Serial
    private static final long serialVersionUID = 1L;

    public VultureSam() {
        super("Vulture Sam",
                "When any other player is eliminated, gain 2 HP.",
                9);
    }

    @Override
    public void onOtherPlayerEliminated(GameController gc, Player self, Player dead) {
        if (dead == self) return;
        heal(2);
    }
}
