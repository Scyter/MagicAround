package ma.com.eclipsecalculator.model;

import java.util.List;

public class BattleResult {

    private long result;

    public void setResult(boolean isAttackerWin, List<Ship> ships) {
        long temp = 0;
        for (Ship ship : ships) {
            temp += ship.getType().getValue() * ship.getCurrentCount();
        }
        result = temp * (isAttackerWin ? 1 : -1);
    }


}

