package ma.com.eclipsecalculator.model;

import java.util.List;

public class BattleResult {

    private long result;

    public BattleResult(boolean isAttackerWin, List<Ship> ships) {
        long temp = 0;
        for (Ship ship : ships) {
            temp += Math.pow(10, ship.getType().getValue()) * ship.getCurrentCount();
        }
        result = temp * (isAttackerWin ? 1 : -1);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BattleResult) {
            BattleResult that = (BattleResult) o;
            return that.result == this.result;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) result;
    }

    public long getResult() {
        return result;
    }
}

