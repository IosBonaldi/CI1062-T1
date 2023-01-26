public class Suporte extends Jogador {

    public Suporte(int atk, int def, boolean vivo, int pontuacao, Setor setor) {
        super(atk, def, vivo, pontuacao, setor);
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
