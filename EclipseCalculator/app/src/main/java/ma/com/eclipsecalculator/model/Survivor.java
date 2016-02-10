package ma.com.eclipsecalculator.model;

public class Survivor {
    private boolean isAttacker;
    private ShipType type;
    private int count;

    public Survivor(boolean isAttacker, ShipType type, int count) {
        this.isAttacker = isAttacker;
        this.type = type;
        this.count = count;
    }

    public Survivor(boolean isAttacker, Ship ship) {
        this.isAttacker = isAttacker;
        this.type = ship.getType();
        this.count = ship.getCurrentCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Survivor that = (Survivor) o;

        return isAttacker == that.isAttacker
                && type == that.type
                && count == that.count;
    }

    @Override
    public int hashCode() {
        return type.getValue() * (1 + (isAttacker ? 1 : 0)) * count;
    }
}
