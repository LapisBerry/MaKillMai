package com.lapisberry.net.packets;

import com.lapisberry.game.entities.characters.CharacterEnum;
import com.lapisberry.game.entities.players.Player;

import java.io.Serial;
import java.util.ArrayList;

/**
 * The {@code ServerStartGamePacket} is a packet that is sent to all clients by the server to start the game.
 */
public class ServerStartGamePacket extends ServerPacket {
    @Serial
    private static final long serialVersionUID = -4405052934644343377L;
    // Fields
    private final ArrayList<Player> shuffledPlayers;
    private final ArrayList<CharacterEnum> shuffledCharacters;

    // Constructors
    public ServerStartGamePacket(ArrayList<Player> shuffledPlayers, ArrayList<CharacterEnum> shuffledCharacters) {
        this.shuffledPlayers = new ArrayList<>(shuffledPlayers);
        this.shuffledCharacters = new ArrayList<>(shuffledCharacters);
    }

    // Getter Setters
    public ArrayList<Player> getShuffledPlayers() {
        return shuffledPlayers;
    }

    public ArrayList<CharacterEnum> getShuffledCharacters() {
        return shuffledCharacters;
    }
}
