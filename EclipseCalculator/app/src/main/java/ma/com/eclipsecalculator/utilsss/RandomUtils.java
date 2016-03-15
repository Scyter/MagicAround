package ma.com.eclipsecalculator.utilsss;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomUtils {

    private static RandomUtils instanceRandomUtils;
    private static Context context;

    private static Random random = new Random();
    private static HashMap<Integer, Integer> rolls;

    private RandomUtils() {
        rolls = new HashMap<>();
    }

    public static void init(Context context) {
        RandomUtils.context = context;

        if (instanceRandomUtils == null) {
            instanceRandomUtils = new RandomUtils();
        }
    }

    public static int roll() {
        int result = random.nextInt(6) + 1;
        L.c("Roll: " + result);
        Integer value = rolls.get(result);
        if (value == null || value == 0) {
            rolls.put(result, 1);
        } else {
            rolls.put(result, value + 1);
        }
        return result;
    }

    public static String rollsString() {
        StringBuilder resultText = new StringBuilder();
        for (Map.Entry<Integer,Integer> entry : rolls.entrySet()) {
            Integer roll = entry.getKey();
            Integer count = entry.getValue();
            resultText.append(roll).append(" : ").append(count).append("\n");
        }
        return resultText.toString();
    }
}
