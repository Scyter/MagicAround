package ma.com.eclipsecalculator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Battle {
    private static final int ITERATIONS = 100000;

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
        for (int i = 0; i < ITERATIONS; i ++) {
            clearBattle();
            while (battleNotEnded()) {
                Ship strikingShip = getStriker();
                if (strikingShip == null) {
                    nextRound();
                } else {
                    calculateStrike(strikingShip);
                }
            }
        }
    }

    private void clearBattle() {
        for (Ship ship : attackers) {
            ship.clear();
        }
        for (Ship ship: defenders) {
            ship.clear();
        }
    }

    private void nextRound() {
        for (Ship ship : attackers) {
            ship.nextRound();
        }
        for (Ship ship: defenders) {
            ship.nextRound();
        }
    }

    private boolean battleNotEnded() {
        for (Ship ship : attackers) {
            if (ship.getCurrentCount() > 0) {
                return true;
            }
        }
        for (Ship ship: defenders) {
            if (ship.getCurrentCount() > 0) {
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
        Strike strike = ship.strike();
        if (ship.isAttacker()) {
            for (Ship target : defenders) {
                if (target.getCurrentCount() == 0) {
                    continue;
                }

                for (Map.Entry<Integer,Integer> entry : strike.entrySet()) {
                    int roll = entry.getValue();
                    int hit = entry.getKey();
                    if (roll > 1 && roll < 6) {
                        if (roll + ship.getComputer() - target.getShield() >= 6) {
                            target.hit(hit);
                            strike.remove()
                        }
                    } else if (roll == 6) {
                        target.hit(hit);
                    }
                }
            }
        }
    }
}
