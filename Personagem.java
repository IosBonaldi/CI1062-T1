public abstract class Personagem {
    protected int atk;
    protected int def;
    protected boolean vivo;

    public Personagem(int atk, int def, boolean vivo) {
        this.atk = atk;
        this.def = def;
        this.vivo = vivo;
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
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public abstract void atacar(Personagem alvo);
}
