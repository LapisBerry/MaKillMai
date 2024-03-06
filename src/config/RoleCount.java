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
    // Fields
    private static final int emperor = 1;
    private static int royalist;
    private static int rebel;
    private static int spy;

    // Methods
    public static void updateRoleCount(final int players) {
        if (4 <= players && players <= 10) {
            spy = TABLE[players - 4][0];
            rebel = TABLE[players - 4][1];
            royalist = TABLE[players - 4][2];
        } else {
            System.out.println("Available players are 4 - 10 players");
            throw new RuntimeException("Available players are 4 - 10 players");
        }
    }

    // Getter
    public static int getEmperor() {
        return emperor;
    }
    public static int getRoyalist() {
        return royalist;
    }

    public static int getRebel() {
        return rebel;
    }

    public static int getSpy() {
        return spy;
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
