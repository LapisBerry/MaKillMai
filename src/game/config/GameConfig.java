package game.config;

/**
 * The {@code GameConfig} class
 * <p>
 * This class contains only static variable.
 *
 * <p>
 * They should be used in a constructor for default configuration
 * @author LapisBerry
 */
public final class GameConfig {
    // Character
    public static final int BASE_ATTACK_DAMAGE = 1;
    public static final int BASE_STARTING_HEALTH = 10;
    public static final int BASE_STARTING_ROT_POWER = 0;
    public static final int BASE_REQUIRED_FOR_PURE_MAGIC = 3;
    public static final int BASE_ROLL_PER_TURN = 3;

    // Dice
    public static final int DICE_POOL_LENGTH = 5;
    
    // Whole game
    public static final int ROT_POOL = 9;
}
