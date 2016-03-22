package ma.com.eclipsecalculator.model;

import ma.com.eclipsecalculator.binding.Int;
import ma.com.eclipsecalculator.utilsss.L;
import ma.com.eclipsecalculator.utilsss.RandomUtils;

public class Ship {

    private ShipType type;

    private boolean isAttacker;

    private Int count;
    private int currentCount;

    private Int ion;
    private Int plasma;
    private Int soliton;
    private Int antiMaterial;
    private Int rift;

    private Int ionMissile;
    private Int plasmaMissile;
    private Int solitonMissile;
    private Int antiMaterialMissile;

    private Int computer;
    private Int shield;

    private Int hull;
    private int currentHull;

    private Int regeneration;

    private Int initiative;

    private boolean struck;

    public Ship(boolean isAttacker, int type, int count, int initiative, int computer, int shield, int hull,
                int ion, int plasma, int soliton, int antiMaterial, int rift,
                int ionMissile, int plasmaMissile, int solitonMissile, int antiMaterialMissile, int regeneration) {
        this.isAttacker = isAttacker;
        this.ion = new Int(ion);
        this.plasma = new Int(plasma);
        this.soliton = new Int(soliton);
        this.antiMaterial = new Int(antiMaterial);
        this.computer = new Int(computer);
        this.shield = new Int(shield);
        this.hull = new Int(hull);
        this.regeneration = new Int(regeneration);
        this.initiative = new Int(initiative);
        this.count = new Int(count);
        this.type = new ShipType(type);
        this.antiMaterial = new Int(antiMaterial);
        this.ionMissile = new Int(ionMissile);
        this.plasmaMissile = new Int(plasmaMissile);
        this.solitonMissile = new Int(solitonMissile);
        this.antiMaterialMissile = new Int(antiMaterialMissile);
        this.rift = new Int(rift);
    }

    public static Ship getShip(boolean isAttacker, int type) {
        switch (type) {
            case ShipType.INTERCEPTOR:
                return new Ship(isAttacker, type, 1, 3, 0, 0, 0,
                        1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0);
            case ShipType.CRUISER:
                return new Ship(isAttacker, type, 1, 2, 1, 0, 1,
                        1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0);
            case ShipType.DREADNOUGHT:
                return new Ship(isAttacker, type, 1, 1, 1, 0, 2,
                        2, 0, 0, 0, 0,
                        0, 0, 0, 0, 0);
            case ShipType.STAR_BASE:
                return new Ship(isAttacker, type, 1, 4, 1, 0, 2,
                        1, 0, 0, 0, 0,
                        0, 0, 0, 0, 0);
            case ShipType.ORBITAL:
                return new Ship(isAttacker, type, 1, 0, 0, 0, 3,
                        2, 0, 0, 0, 0,
                        0, 0, 0, 0, 0);
            case ShipType.DEATH_MOON:
                return new Ship(isAttacker, type, 1, 1, 1, 0, 4,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 0);
            case ShipType.ANCIENT:
                return new Ship(isAttacker, type, 1, 2, 1, 0, 1,
                        2, 0, 0, 0, 0,
                        0, 0, 0, 0, 0);
            case ShipType.CENTER:
                return new Ship(isAttacker, type, 1, 0, 1, 0, 7,
                        4, 0, 0, 0, 0,
                        0, 0, 0, 0, 0);
            case ShipType.ANOMALY:
                return new Ship(isAttacker, type, 1, 0, 0, 0, 6,
                        0, 0, 0, 0, 3,
                        0, 0, 0, 0, 0);

        }
        return null;
    }

    public Strike strike() {
        Strike strike = new Strike();
        for (int i = 0; i < ion.get(); i++) {
            strike.add(new Die(1, RandomUtils.roll()));
        }
        for (int i = 0; i < plasma.get(); i++) {
            strike.add(new Die(2, RandomUtils.roll()));
        }
        for (int i = 0; i < soliton.get(); i++) {
            strike.add(new Die(3, RandomUtils.roll()));
        }
        for (int i = 0; i < antiMaterial.get(); i++) {
            strike.add(new Die(4, RandomUtils.roll()));
        }
        return strike;
    }

    public void hit(int damage) {
        currentHull -= damage;
        L.b(isAttacker() + " " + getTypeString() + " damage: " + damage + " currentHull:" + currentHull);
        if (currentHull < 0) {
            die();
        }
    }

    public void clear() {
        currentCount = count.get();
        currentHull = hull.get();
        struck = false;
    }

    public void nextRound() {
        struck = false;
        currentHull += regeneration.get();
        if (currentHull > hull.get()) {
            currentHull = hull.get();
        }
    }

    public ShipType getType() {
        return type;
    }

    public String getTypeString() {
        return type.toString();
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public Int getAntiMaterial() {
        return antiMaterial;
    }

    public Int getComputer() {
        return computer;
    }

    public Int getCount() {
        return count;
    }

    public Int getHull() {
        return hull;
    }

    public Int getIon() {
        return ion;
    }

    public Int getPlasma() {
        return plasma;
    }

    public Int getRift() {
        return rift;
    }

    public Int getAntiMaterialMissile() {
        return antiMaterialMissile;
    }

    public Int getIonMissile() {
        return ionMissile;
    }

    public Int getPlasmaMissile() {
        return plasmaMissile;
    }

    public Int getSolitonMissile() {
        return solitonMissile;
    }

    public boolean isStruck() {
        return struck;
    }

    public void setIsStruck(boolean struck) {
        this.struck = struck;
    }

    public Int getRegeneration() {
        return regeneration;
    }


    public Int getShield() {
        return shield;
    }

    public Int getSoliton() {
        return soliton;
    }

    public boolean isAttacker() {
        return isAttacker;
    }

    public void setIsAttacker(boolean isAttacker) {
        this.isAttacker = isAttacker;
    }

    public double getInitiativeValue() {
        return initiative.get() + (isAttacker ? 0 : 0.1);
    }

    public Int getInitiative() {
        return initiative;
    }

    private void die() {
        currentHull = hull.get();
        currentCount--;
        L.b(isAttacker() + " " + getTypeString() + " die. Current count:" + currentCount);
    }
}
