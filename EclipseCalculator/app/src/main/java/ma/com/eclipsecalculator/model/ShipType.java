package ma.com.eclipsecalculator.model;

public enum ShipType {
    INTERCEPTOR (2),
    CRUISER (4),
    DREADNOUGHT (5),
    STAR_BASE (3),
    ORBITAL (1),
    DEATH_MOON (6),
    ANCIENT (10),
    CENTER (11);

    private final int value;

    ShipType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
