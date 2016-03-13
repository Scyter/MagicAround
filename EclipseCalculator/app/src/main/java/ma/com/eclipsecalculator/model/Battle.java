package ma.com.eclipsecalculator.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Battle {
    private static final int ITERATIONS = 10;

    private List<Ship> attackers;
    private List<Ship> defenders;

    private Map<BattleResult, Integer> results;

    public Battle() {
        attackers = new ArrayList<>();
        defenders = new ArrayList<>();
    }

    public void addAttacker(Ship ship) {
        attackers.add(ship);
    }

    public void addDefender(Ship ship) {
        defenders.add(ship);
    }

    public void calculate() {
        for (int i = 0; i < ITERATIONS; i++) {
            Log.d("aaa", i + "");
            clearBattle();
            while (battleNotEnded()) {
                Ship strikingShip = getStriker();
                if (strikingShip == null) {
                    nextRound();
                } else {
                    calculateStrike(strikingShip);
                }
            }
            BattleResult result = getResult();
            Integer currentResultCount = results.get(result);
            if (currentResultCount == null || currentResultCount == 0) {
                results.put(result, 1);
            } else {
                currentResultCount++;
                results.put(result, currentResultCount);
            }
        }
    }

    public Map<BattleResult, Integer> getResults() {
        return results;
    }

    private void clearBattle() {
        for (Ship ship : attackers) {
            ship.clear();
        }
        for (Ship ship : defenders) {
            ship.clear();
        }
    }

    private void nextRound() {
        Log.d("aaa", "nextRound");
        for (Ship ship : attackers) {
            ship.nextRound();
        }
        for (Ship ship : defenders) {
            ship.nextRound();
        }
    }

    private boolean battleNotEnded() {
        for (Ship ship : attackers) {
            if (ship.getCurrentCount() > 0) {
                Log.d("aaa", "ended");
                return true;
            }
        }
        for (Ship ship : defenders) {
            if (ship.getCurrentCount() > 0) {
                Log.d("aaa", "ended");
                return true;
            }
        }
        return false;
    }

    private Ship getStriker() {
        Ship striker = null;

        for (Ship ship : attackers) {
            if (!ship.isStruck() && (striker == null || striker.getInitiative() < ship.getInitiative())) {
                striker = ship;
            }
        }

        for (Ship ship : defenders) {
            if (!ship.isStruck() && (striker == null || striker.getInitiative() < ship.getInitiative())) {
                striker = ship;
            }
        }

        return striker;
    }

    private void calculateStrike(Ship ship) {
        Log.d("aaa", "calculateStrike");
        Strike strike = ship.strike();

        for (Ship target : ship.isAttacker() ? defenders : attackers) {
            if (target.getCurrentCount() == 0) {
                continue;
            }

            for (Die die : strike) {
                int roll = die.getRoll();
                int damage = die.getDamage();
                if (roll > 1 && roll < 6) {
                    if (roll + ship.getComputer() - target.getShield() >= 6) {
                        target.hit(damage);
                        strike.remove(die);
                    }
                } else if (roll == 6) {
                    target.hit(damage);
                    strike.remove(die);
                }
            }
        }
    }

    private BattleResult getResult() {
        for (Ship ship : attackers) {
            if (ship.getCurrentCount() > 0) {
                return new BattleResult(true, attackers);
            }
        }
        return new BattleResult(false, defenders);
    }



}
