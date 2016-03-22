package ma.com.eclipsecalculator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.com.eclipsecalculator.utilsss.L;

public class Battle {
    private static final int ITERATIONS = 1000;

    private List<Ship> attackers;
    private List<Ship> defenders;

    private Map<BattleResult, Integer> results;

    public Battle() {
        attackers = new ArrayList<>();
        defenders = new ArrayList<>();
        results = new HashMap<>();
    }

    public void addShip(Ship ship) {
        if (ship.isAttacker()) {
            attackers.add(ship);
        } else {
            defenders.add(ship);
        }
    }

    public void calculate() {
        results.clear();
        for (int i = 0; i < ITERATIONS; i++) {
            if (i % 100 == 0) {
                L.a(i + " iteration");
            }
            clearBattle();
            while (battleNotEnded()) {
                Ship strikingShip = getStriker();
                if (strikingShip == null) {
                    nextRound();
                } else {
                    calculateStrike(strikingShip);
                    strikingShip.setIsStruck(true);
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

    public void clearShips() {
        attackers.clear();
        defenders.clear();
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
        L.b("next round");
        for (Ship ship : attackers) {
            ship.nextRound();
        }
        for (Ship ship : defenders) {
            ship.nextRound();
        }
    }

    private boolean battleNotEnded() {
        boolean livedAttacker = false, livedDefender = false;
        for (Ship ship : attackers) {
            if (ship.getCurrentCount() > 0) {
                livedAttacker = true;
            }
        }
        for (Ship ship : defenders) {
            if (ship.getCurrentCount() > 0) {
                livedDefender = true;
            }
        }
        L.b("battleNotEnded: " + livedAttacker + " " + livedDefender);
        return livedAttacker && livedDefender;
    }

    private Ship getStriker() {
        Ship striker = null;

        for (Ship ship : attackers) {
            if (!ship.isStruck() && (striker == null || striker.getInitiativeValue() < ship.getInitiativeValue())) {
                striker = ship;
            }
        }

        for (Ship ship : defenders) {
            if (!ship.isStruck() && (striker == null || striker.getInitiativeValue() < ship.getInitiativeValue())) {
                striker = ship;
            }
        }

        return striker;
    }

    private void calculateStrike(Ship ship) {
        Strike strike = ship.strike();

        L.b(ship.isAttacker() + " " + ship.getTypeString() + " " + strike.size());
        for (Die die : strike) {
            for (Ship target : ship.isAttacker() ? defenders : attackers) {
                if (target.getCurrentCount() == 0) {
                    L.b("no target");
                    continue;
                }
                int roll = die.getRoll();
                int damage = die.getDamage();
                L.b("roll:" + roll + " damage: " + damage);
                if (roll > 1 && roll < 6) {
                    if (roll + ship.getComputer().get() - target.getShield().get() >= 6) {
                        target.hit(damage);
                    }
                } else if (roll == 6) {
                    target.hit(damage);
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
