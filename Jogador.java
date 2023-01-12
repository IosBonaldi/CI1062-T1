public class Jogador extends Personagem {
    protected int pontuacao;
    protected Setor setor;

    public Jogador(boolean vivo) {
        super(0, 0, vivo);
    }
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

    public void atacar(Personagem alvo) {

    }

    public void procurar() {

    }

    public void movimentar(Tabuleiro T, Direcao D) {
        
    }
}
