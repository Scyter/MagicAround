package ma.com.eclipsecalculator.model;

import java.util.Comparator;

public enum ShipType {
    INTERCEPTOR (1),
    CRUISER (2),
    DREADNOUGHT (3),
    STAR_BASE (4),
    ANCIENT (5),
    CENTER (6),
    UNKNOWN (0);

    private final int value;

    ShipType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Comparator<ShipType> comparator = new Comparator<ShipType>() {
        public int compare(ShipType type1, ShipType type2) {
            return type1.getValue() - type2.getValue();
        }
    };
}
