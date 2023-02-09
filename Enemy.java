import java.util.Random;

public class Enemy extends Character {
    private int position;

    public Enemy(int atk, int def, int position) {
        super(atk, def);
        this.setPosition(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        if (2 < position || position < 0)
            throw new IllegalArgumentException();
        this.position = position;
    }

    /**
     * Realiza o ataque de um inimigo à um único jogador.
     *
     * @param target o jogador que será atacado.
     */
    public boolean attack(Character target) {
        // Gera uma constante de ataque aleatória entre 1 e 6
        Random r = new Random();
        int action = r.nextInt(6) + 1;
        // Ataca se a constante gerada for par
        if (action % 2 == 0 && target.isAlive()) {
            target.setDef((target.getDef() > this.getAtk()) ? (target.getDef() - this.getAtk()) : 0);
        }
        return true;
    }
}