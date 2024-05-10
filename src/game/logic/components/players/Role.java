package game.logic.components.players;

/**
 * The {@code Role} enum represents the role of the player in the game.
 * <p>
 *     It has EMPEROR, ROYALIST, REBEL, and SPY.
 * <p>
 *     It is used to determine the player's role in the game.
 * <p>
 *     EMPEROR has to eliminate all the rebels and spies to win.
 * <p>
 *     ROYALIST has the same goal as EMPEROR.
 * <p>
 *     REBEL has to eliminate the EMPEROR to win.
 * <p>
 *     SPY has to be the last player standing to win.
 */
public enum Role {
    EMPEROR,
    ROYALIST,
    REBEL,
    SPY
}
