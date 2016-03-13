package ma.com.eclipsecalculator.Utils;

import java.util.Random;

public class RandomUtils {

    private static Random random = new Random();
    private RandomUtils() {
        // empty
    }

    public static int roll() {
        return (random.nextInt(5) + 1);
    }
}
