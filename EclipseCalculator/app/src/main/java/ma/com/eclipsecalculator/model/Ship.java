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

    public Ship(boolean isAttacker, int type, int count, int initiative,
                int ion, int plasma, int soliton, int antiMaterial,
                int computer, int shield, int hull, int regeneration) {
        this(isAttacker, type, count, initiative, ion, plasma, soliton, antiMaterial,
                0, 0, 0, 0, computer, shield, hull, regeneration);
    }

    public Ship(boolean isAttacker, int type, int count, int initiative,
                int ion, int plasma, int soliton, int antiMaterial,
                int ionMissile, int plasmaMissile, int solitonMissile, int antiMaterialMissile,
                int computer, int shield, int hull, int regeneration) {
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

    public boolean isStruck() {
        return struck;
    }

    public void setIsStruck(boolean struck) {
        this.struck = struck;
    }

    public Int getRegeneration() {
        return regeneration;
    }


    public int getShield() {
        return shield.get();
    }

    public int getSoliton() {
        return soliton.get();
    }

    public boolean isAttacker() {
        return isAttacker;
    }

    public void setIsAttacker(boolean isAttacker) {
        this.isAttacker = isAttacker;
    }

    public double getInitiative() {
        return initiative.get() + (isAttacker ? 0 : 0.1);
    }

    private void die() {
        currentHull = hull.get();
        currentCount--;
        L.b(isAttacker() + " " + getTypeString() + " die. Current count:" + currentCount);
    }
}
