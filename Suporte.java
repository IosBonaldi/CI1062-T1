public class Suporte extends Jogador {

    public Suporte(boolean vivo) {
        super(vivo);
    }

    public Suporte(int aTK, int dEF, boolean vivo, int pontuacao, Setor setor) {
        super(aTK, dEF, vivo, pontuacao, setor);
    }

    public void curar(Jogador jogador) {

    }
}
