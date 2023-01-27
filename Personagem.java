public abstract class Personagem {
    protected int atk;
    protected int def;

    public Personagem(int atk, int def) {
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

    public boolean isVivo() {
        return (this.getDef() > 0);
    }

    public abstract void atacar(Personagem alvo);
}
