package ma.com.eclipsecalculator.model;

public class Die {
    private int damage;
    private int roll;

    public Die(int damage, int roll) {
        this.damage = damage;
        this.roll = roll;
    }

    public int getDamage() {
        return damage;
    }

    public int getRoll() {
        return roll;
    }
}
