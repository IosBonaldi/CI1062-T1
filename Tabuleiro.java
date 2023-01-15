public class Tabuleiro {
    private Setor[][] setores;

    // Ao inves de receber uma matriz como parametro, receber o numero de linhas e
    // colunas da matriz
    public Tabuleiro(Setor[][] setores) {
        this.setores = setores;
    }

    public Setor[][] getSetores() {
        return setores;
    }

    public void setSetores(Setor[][] setores) {
        this.setores = setores;
    }

    private void gerarPortas() {

    }
}
