package ma.com.eclipsecalculator.model;

import java.util.List;

public class BattleResult {
    public List<Survivor> survivors;

    public BattleResult(List<Survivor> survivors) {
        this.survivors = survivors;
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

//        sort(this.survivors);
//        sort(that.survivors);
        return this.equals(that);
    }
}
