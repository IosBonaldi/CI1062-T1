public class Suporte extends Jogador {

    public Suporte(Setor setor) {
        super(1, 7, setor, 7);
    }

    public Suporte(int atk, int def, int pontuacao, Setor setor) {
        super(atk, def, pontuacao, setor);
    }

    /**
     * Recupera a defesa de um jogador.
     *
     * @param alvo o jogador que terá sua defesa recuperada.
     */
    public void curar(Jogador alvo) {
        // Garante que a recuperação só ocorra se o alvo estiver no mesmo setor do
        // suporte (ou seja o próprio)
        if (this.getSetor() == alvo.getSetor() && alvo.isVivo()) {
            if(alvo.getVidaMaxima() - alvo.getDef() > 1){
                alvo.setDef(alvo.getDef() + 2);
            }else if(alvo.getVidaMaxima() - alvo.getDef() == 1){
                alvo.setDef(alvo.getDef() + 1);
            }
        }
    }
}
