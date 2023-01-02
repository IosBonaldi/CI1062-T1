public abstract class Personagem {
    protected int ATK;
    protected int DEF;
    protected boolean vivo;

    public Personagem(int aTK, int dEF, boolean vivo) {
        ATK = aTK;
        DEF = dEF;
        this.vivo = vivo;
    }

    public int getATK() {
        return ATK;
    }

    public void setATK(int aTK) {
        ATK = aTK;
    }

    public int getDEF() {
        return DEF;
    }

    public void setDEF(int dEF) {
        DEF = dEF;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public abstract void atacar(Personagem alvo);
}
