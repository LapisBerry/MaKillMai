package config;

/**
 * The {@code RoleCount} class
 *
 * <p>This class show how many players for every role according to how many players are in the game.
 *
 * <p>Available players are 4 - 10 players.
 *
 * @author LapisBerry
 */
public final class RoleCount {
    public static int getEmperors() {
        return 1;
    }
    public static int getRoyalists(int players) {
        if (players < 4 || players > 10)
            throw new IllegalArgumentException("Invalid player count: " + players + " players must be between 4 and 10. (inclusive)");
        return TABLE[players - 4][2];
    }

    public static int getRebels(int players) {
        if (players < 4 || players > 10)
            throw new IllegalArgumentException("Invalid player count: " + players + " players must be between 4 and 10. (inclusive)");
        return TABLE[players - 4][1];
    }

    public static int getSpies(int players) {
        if (players < 4 || players > 10)
            throw new IllegalArgumentException("Invalid player count: " + players + " players must be between 4 and 10. (inclusive)");
        return TABLE[players - 4][0];
    }

    // Array of role count
    private static final int[][] TABLE = {// sp,rb,ry -> spy, rebel, royalist
                                            {1, 2, 0},// 4 players
                                            {1, 2, 1},// 5 players
                                            {1, 3, 1},// 6 players
                                            {1, 3, 2},// 7 players
                                            {2, 3, 2},// 8 players
                                            {2, 4, 2},// 9 players
                                            {2, 4, 3} // 10 players
    };
}
