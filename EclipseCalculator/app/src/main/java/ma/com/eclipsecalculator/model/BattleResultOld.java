package ma.com.eclipsecalculator.model;

import java.util.Set;

public class BattleResultOld {
    public Set<Survivor> survivors;

    public BattleResultOld(Set<Survivor> survivors) {
        this.survivors = survivors;
    }

    public boolean add(Survivor survivor) {
        return survivors.add(survivor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BattleResultOld that = (BattleResultOld) o;

        return this.equals(that);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Survivor survivor : survivors) {
            hashCode += survivor.hashCode();
        }
        return hashCode;
    }
}
