public abstract class Character {
    protected int atk;
    protected int def;

    public Character(int atk, int def) {
        this.setAtk(atk);
        this.setDef(def);
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public boolean isAlive() {
        return (this.getDef() > 0);
    }

    public abstract boolean attack(Character target);
}
