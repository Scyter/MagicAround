package ma.com.eclipsecalculator.model;

public class ShipType {
    public static final int INTERCEPTOR = 2;
    public static final  int CRUISER = 4;
    public static final  int DREADNOUGHT = 5;
    public static final  int STAR_BASE = 3;
    public static final  int ORBITAL = 1;
    public static final  int DEATH_MOON = 6;
    public static final  int ANCIENT = 7;
    public static final  int CENTER = 8;
    public static final  int ANOMALY = 9;

    private final int value;

    public ShipType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
