package ma.com.eclipsecalculator.model;

public class Ship {

    private ShipType type;

    private int count;
    private int currentCount;

    private int ion;
    private int plasma;
    private int antiMaterial;
    private int soliton;
    private int rift;

    private int ionMissile;
    private int plasmaMissile;

    private int computer;
    private int shield;

    private int hull;
    private int currentHull;

    private int initiative;

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public int getCurrentCount() {
        return currentCount;
    }
}
