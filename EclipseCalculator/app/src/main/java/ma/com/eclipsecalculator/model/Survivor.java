package ma.com.eclipsecalculator.model;

import android.support.annotation.NonNull;

public class Survivor implements Comparable{
    private boolean isAttacker;
    private ShipType type;
    private int count;

    public Survivor(boolean isAttacker, ShipType type, int count) {
        this.isAttacker = isAttacker;
        this.type = type;
        this.count = count;
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
    public int compareTo(@NonNull Object another) {
        Survivor that = (Survivor) another;
        if (isAttacker && !that.isAttacker) {
            return 1;
        }
        if (!isAttacker && that.isAttacker) {
            return -1;
        }
        return ShipType.comparator.compare(this.type, ((Survivor)another).type);
    }
}
