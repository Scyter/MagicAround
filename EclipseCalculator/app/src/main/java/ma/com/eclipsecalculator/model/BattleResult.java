package ma.com.eclipsecalculator.model;

import java.util.Set;

public class BattleResult {
    public Set<Survivor> survivors;

    public BattleResult(Set<Survivor> survivors) {
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

        BattleResult that = (BattleResult) o;

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
