public class Support extends Player {

    public Support(Section section) {
        super(1, 7, section);
    }

    /**
     * Recupera a defesa de um jogador.
     *
     * @param target o jogador que terá sua defesa recuperada.
     */
    public void heal(Player target) {
        // Garante que a recuperação só ocorra se o alvo estiver no mesmo setor do
        // suporte (ou seja o próprio)
        if (this.getSection() == target.getSection() && target.isAlive()) {
            if (target.getMaxDef() - target.getDef() > 1) {
                target.setDef(target.getDef() + 2);
            } else if (target.getMaxDef() - target.getDef() == 1) {
                target.setDef(target.getDef() + 1);
            }
        }
    }
}
