import java.util.Random;

public class Jogador extends Personagem {
    protected int pontuacao;
    protected Setor setor;
    protected int vidaMaxima;

    public Jogador(Setor setor) {
        this(2, 6, setor);
    }

    public Jogador(int atk, int def, Setor setor) {
        super(atk, def);
        this.setPontuacao(0);
        this.setSetor(setor);
        this.setVidaMaxima(def);
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

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    /**
     * Realiza o ataque de um jogador à um inimigo.
     *
     * @param alvo o inimigo que será atacado.
     */
    public boolean atacar(Personagem alvo) {
        // Gera uma constante de ataque aleatória entre 0 e 1
        Random r = new Random();
        int acao = r.nextInt(2);
        // Ataca se o setor não for oculto ou se a constante gerada for 1 (50/100 de
        // chance de ataque)
        if (this.getSetor().getTipo() != SetorTipos.OCULTO || acao == 1) {
            if (alvo.isVivo()) {
                alvo.setDef((alvo.getDef() > this.getAtk()) ? (alvo.getDef() - this.getAtk()) : 0);
            }

            // Se eliminar o alvo haverá atualização do atributo vivo do alvo e
            // incremento da pontuação do jogador que matou o vírus;
            if (!alvo.isVivo()) {
                this.setPontuacao(this.getPontuacao() + alvo.getAtk() * 10);
            }
            return true;
        }
        return false;
    }

    /**
     * Realiza a procura no setor, podendo aleatorimente incorrer em incremento de
     * vida ao jogador ou decremento dos inimigos.
     */
    public void procurar() {
        // Impede ação em setores privados
        if (this.getSetor().getTipo() != SetorTipos.PRIVADO) {
            // Gera uma constante de procura aleatória entre 1 e 6
            Random r = new Random();
            int achado = r.nextInt(6) + 1;
            // Executa recuperação ou decréscimo de DEFs conforme constante gerada
            switch (achado) {
                case 4:
                    this.setDef(this.getDef() + 1);
                    this.setVidaMaxima(this.getVidaMaxima() + 1);
                    break;

                case 5:
                    this.setDef(this.getDef() + 2);
                    this.setVidaMaxima(this.getVidaMaxima() + 2);
                    break;

                case 6:
                    for (Inimigo i : this.getSetor().getInimigos()) {
                        // Verifica se inimigo está vivo, caso estiver diminui sua vida e
                        // analisa novamente se ele está vivo ou não, caso morrer atualiza
                        // a pontuação do jogador que realizou a ação de procura;
                        if (i.isVivo()) {
                            i.setDef(i.getDef() - 1);
                            if (!i.isVivo()) {
                                this.setPontuacao(this.getPontuacao() + i.getAtk() * 10);
                            }
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
