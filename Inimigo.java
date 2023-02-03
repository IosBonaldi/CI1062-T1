import java.util.ArrayList;
import java.util.Random;

public class Inimigo extends Personagem {
    private int posicao;

    public Inimigo(int atk, int def, int posicao) {
        super(atk, def);
        this.setPosicao(posicao);
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        if (1 <= posicao && posicao <= 3) {
            this.posicao = posicao;
        }
    }

    /**
     * Realiza o ataque de um inimigo à um único jogador.
     *
     * @param alvo o jogador que será atacado.
     */
    public boolean atacar(Personagem alvo) {
        // Gera uma constante de ataque aleatória entre 1 e 6
        Random r = new Random();
        int acao = r.nextInt(6) + 1;
        // Ataca se a constante gerada for par
        if (acao % 2 == 0 && alvo.isVivo()) {
            alvo.setDef((alvo.getDef() > this.getAtk()) ? (alvo.getDef() - this.getAtk()) : 0);
        }
        return true;
    }

    /**
     * Realiza o ataque de um inimigo à um conjunto de jogadores.
     *
     * @param alvos uma lista com os jogadores que serão atacados.
     */
    public void atacar(ArrayList<Jogador> alvos) {
        // Itera a lista de jogadores
        for (Jogador j : alvos) {
            // Gera uma constante de ataque aleatória entre 1 e 6
            Random r = new Random();
            int acao = r.nextInt(6) + 1;
            // Ataca se a constante gerada for par
            if (acao % 2 == 0 && j.isVivo()) {
                j.setDef((j.getDef() > this.getAtk()) ? (j.getDef() - this.getAtk()) : 0);
            }
        }
    }
}
