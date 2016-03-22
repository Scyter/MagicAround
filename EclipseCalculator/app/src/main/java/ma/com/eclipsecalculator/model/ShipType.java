package ma.com.eclipsecalculator.model;

import ma.com.eclipsecalculator.R;

public class ShipType {
    public static final int INTERCEPTOR = 2;
    public static final int CRUISER = 4;
    public static final int DREADNOUGHT = 5;
    public static final int STAR_BASE = 3;
    public static final int ORBITAL = 1;
    public static final int DEATH_MOON = 6;
    public static final int ANCIENT = 7;
    public static final int CENTER = 8;
    public static final int ANOMALY = 9;

    private final int value;

    public ShipType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (value) {
            case ORBITAL:
                return "ORBITAL";
            case INTERCEPTOR:
                return "INTERCEPTOR";
            case STAR_BASE:
                return "STAR_BASE";
            case CRUISER:
                return "CRUISER";
            case DREADNOUGHT:
                return "DREADNOUGHT";
            case DEATH_MOON:
                return "DEATH_MOON";
            case ANCIENT:
                return "ANCIENT";
            case CENTER:
                return "CENTER";
            case ANOMALY:
                return "ANOMALY";
            default:
                return "UNKNOWN";
        }
    }

    public int getDrawableId() {
        switch (value) {
            case ORBITAL:
                return R.drawable.orbital;
            case INTERCEPTOR:
                return R.drawable.interceptor;
            case STAR_BASE:
                return R.drawable.starbase;
            case CRUISER:
                return R.drawable.cruiser;
            case DREADNOUGHT:
                return R.drawable.dreadnought;
            case DEATH_MOON:
                return R.drawable.deathmoon;
            case ANCIENT:
                return R.drawable.ancient;
            case CENTER:
                return R.drawable.center;
            case ANOMALY:
                return R.drawable.anomaly;
            default:
                return R.drawable.empty;
        }
    }
}
