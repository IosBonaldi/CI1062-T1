import java.util.Random;

public class Jogador extends Personagem {
    protected int pontuacao;
    protected Setor setor;

    public Jogador(int aTK, int dEF, boolean vivo, int pontuacao, Setor setor) {
        super(aTK, dEF, vivo);
        this.pontuacao = pontuacao;
        this.setor = setor;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    /**
     * Realiza o ataque de um jogador à um inimigo.
     *
     * @param alvo o inimigo que será atacado.
     */
    public void atacar(Personagem alvo) {
        // Gera uma constante de ataque aleatória entre 0 e 1
        Random r = new Random();
        int acao = r.nextInt(2);
        // Ataca se o setor não for oculto ou se a constante gerada for 1 (50/100 de
        // chance de ataque)
        if (this.setor.getTipo() != SetorTipos.OCULTO || acao == 1) {
            alvo.DEF -= this.ATK;
        }
    }

    /**
     * Realiza a procura no setor, podendo aleatorimente incorrer em incremento de
     * vida ao jogador ou decremento dos inimigos.
     */
    public void procurar() {
        // Impede ação em setores privados
        if (this.setor.getTipo() != SetorTipos.PRIVADO) {
            // Gera uma constante de procura aleatória entre 1 e 6
            Random r = new Random();
            int achado = r.nextInt(6) + 1;
            // Executa recuperação ou decréscimo de DEFs conforme constante gerada
            switch (achado) {
                case 4:
                    this.DEF += 1;
                    break;

                case 5:
                    this.DEF += 2;
                    break;

                case 6:
                    for (Inimigo i : this.setor.getInimigos()) {
                        i.DEF--;
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Movimenta o jogador através dos setores do tabuleiro atualizando seu atributo
     * setor.
     *
     * @param t o tabuleiro no qual o jogador está inserido.
     * @param d a direção para a qual deseja-se mover o jogador.
     */
    public void movimentar(Tabuleiro t, Direcao d) {
        // Impede movimentação em setores que ainda possuam inimigos
        if (this.setor.getInimigos().isEmpty()) {
            // Atualiza o atributo setor de acordo com a direção recebida, checando
            // eventuais colisões com as bordas
            switch (d) {
                case CIMA:
                    if (this.setor.getCoordenada().getY() > 0) {
                        this.setor = t.getSetores()[this.setor.getCoordenada().getX()][this.setor.getCoordenada().getY()
                                - 1];
                    }
                    break;
                case DIREITA:
                    if (this.setor.getCoordenada().getX() < 4) {
                        this.setor = t.getSetores()[this.setor.getCoordenada().getX() + 1][this.setor.getCoordenada()
                                .getY()];
                    }
                    break;
                case BAIXO:
                    if (this.setor.getCoordenada().getY() < 4) {
                        this.setor = t.getSetores()[this.setor.getCoordenada().getX()][this.setor.getCoordenada().getY()
                                + 1];
                    }
                    break;
                case ESQUERDA:
                    if (this.setor.getCoordenada().getX() > 0) {
                        this.setor = t.getSetores()[this.setor.getCoordenada().getX() - 1][this.setor.getCoordenada()
                                .getY()];
                    }
                    break;
            }
        }
    }
}
