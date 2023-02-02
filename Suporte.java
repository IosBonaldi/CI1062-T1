public class Suporte extends Jogador {

    public Suporte(Setor setor) {
        super(1, 7);
        this.setSetor(setor);
    }

    public Suporte(int atk, int def, int pontuacao, Setor setor) {
        super(atk, def, pontuacao, setor);
    }

    /**
     * Recupera a defesa de um jogador em dois pontos.
     *
     * @param alvo o jogador que terá sua defesa recuperada.
     */
    public void curar(Jogador alvo) {
        // Garante que a recuperação só ocorra se o alvo estiver no mesmo setor do
        // suporte (ou seja o próprio)
        if (this.getSetor() == alvo.getSetor() && alvo.isVivo()) {
            alvo.setDef(alvo.getDef() + 2);
        }
    }
}
