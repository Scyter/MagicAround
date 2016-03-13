package ma.com.eclipsecalculator.model;

import ma.com.eclipsecalculator.Utils.RandomUtils;

public class Ship {

    private ShipType type;

    private boolean isAttacker;

    private int count;
    private int currentCount;

    private int ion;
    private int plasma;
    private int soliton;
    private int antiMaterial;
    private int rift;

    private int ionMissile;
    private int plasmaMissile;
    private int antiMaterialMissile;

    private int computer;
    private int shield;

    private int hull;
    private int currentHull;

    private int regeneration;

    private int initiative;

    private boolean struck;

    public Ship(boolean isAttacker, ShipType type, int count, int ion, int plasma, int soliton, int antiMaterial,
                int computer, int shield, int hull, int regeneration, int initiative) {
        this.isAttacker = isAttacker;
        this.ion = ion;
        this.plasma = plasma;
        this.soliton = soliton;
        this.antiMaterial = antiMaterial;
        this.computer = computer;
        this.shield = shield;
        this.hull = hull;
        this.regeneration = regeneration;
        this.initiative = initiative;
        this.count = count;
        this.type = type;
    }

    public Strike strike() {
        Strike strike = new Strike();
        for (int i = 0; i < ion; i++) {
            strike.add(new Die(1, RandomUtils.roll()));
        }
        for (int i = 0; i < plasma; i++) {
            strike.add(new Die(2, RandomUtils.roll()));
        }
        for (int i = 0; i < soliton; i++) {
            strike.add(new Die(3, RandomUtils.roll()));
        }
        for (int i = 0; i < antiMaterial; i++) {
            strike.add(new Die(4, RandomUtils.roll()));
        }
        return strike;
    }

    public void hit(int damage) {
        currentHull -= damage;
        if (currentHull <= 0) {
            die();
        }
    }

    public void clear() {
        currentCount = count;
        currentHull = hull;
        struck = false;
    }

    public void nextRound() {
        struck = false;
        currentHull += regeneration;
        if (currentHull > hull) {
            currentHull = hull;
        }
    }

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public int getAntiMaterial() {
        return antiMaterial;
    }

    public void setAntiMaterial(int antiMaterial) {
        this.antiMaterial = antiMaterial;
    }

    public int getComputer() {
        return computer;
    }

    public void setComputer(int computer) {
        this.computer = computer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getHull() {
        return hull;
    }

    public void setHull(int hull) {
        this.hull = hull;
    }

    public int getIon() {
        return ion;
    }

    public void setIon(int ion) {
        this.ion = ion;
    }

    public int getPlasma() {
        return plasma;
    }

    public void setPlasma(int plasma) {
        this.plasma = plasma;
    }

    public boolean isStruck() {
        return struck;
    }

    public void setIsAttacked(boolean isAttacked) {
        this.struck = isAttacked;
    }

    public int getRegeneration() {
        return regeneration;
    }

    public void setRegeneration(int regeneration) {
        this.regeneration = regeneration;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public int getSoliton() {
        return soliton;
    }

    public void setSoliton(int soliton) {
        this.soliton = soliton;
    }

    public boolean isAttacker() {
        return isAttacker;
    }

    public void setIsAttacker(boolean isAttacker) {
        this.isAttacker = isAttacker;
    }

    public double getInitiative() {
        return initiative + (isAttacker ? 0 : 0.1);
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    private void die() {
        currentHull = hull;
        currentCount--;
    }
}
