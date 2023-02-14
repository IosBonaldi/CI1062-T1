import java.time.LocalDateTime;
import java.util.Random;

public class Player extends Character {
    protected Score score;
    protected Section section;
    protected int maxDef;

    public Player(Section section) {
        this(2, 6, section);
    }

    public Player(int atk, int def, Section section) {
        super(atk, def);
        this.score = new Score(0, LocalDateTime.now());
        this.setSection(section);
        this.setMaxDef(def);
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        if (0 > score.getPoints() || score.getPoints() < this.score.getPoints())
            throw new IllegalArgumentException();
        this.score = score;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getMaxDef() {
        return maxDef;
    }

    public void setMaxDef(int maxDef) {
        if (maxDef < this.def)
            throw new IllegalArgumentException();
        this.maxDef = maxDef;
    }

    /**
     * Realiza o ataque de um jogador à um inimigo.
     *
     * @param target o inimigo que será atacado.
     */
    public boolean attack(Character target) {
        // Gera uma constante de ataque aleatória entre 0 e 1
        Random r = new Random();
        int action = r.nextInt(2);
        // Ataca se o setor não for oculto ou se a constante gerada for 1 (50/100 de
        // chance de ataque)
        if (this.getSection().getType() != SectionType.HIDDEN || action == 1) {
            if (target.isAlive()) {
                target.setDef((target.getDef() > this.getAtk()) ? (target.getDef() - this.getAtk()) : 0);
            }

            // Se eliminar o alvo haverá atualização do atributo vivo do alvo e
            // incremento da pontuação do jogador que matou o vírus;
            if (!target.isAlive()) {
                this.getScore().setPoints(this.getScore().getPoints() + target.getAtk() * 10);
            }
            return true;
        }
        return false;
    }

    /**
     * Realiza a procura no setor, podendo aleatorimente incorrer em incremento de
     * vida ao jogador ou decremento dos inimigos.
     */
    public int search() {
        // Impede ação em setores privados
        if (this.getSection().getType() != SectionType.PRIVATE) {
            // Gera uma constante de procura aleatória entre 1 e 6
            Random r = new Random();
            int found = r.nextInt(6) + 1;
            // Executa recuperação ou decréscimo de DEFs conforme constante gerada
            switch (found) {
                case 4:
                    this.setDef(this.getDef() + 1);
                    this.setMaxDef(this.getMaxDef() + 1);
                    return 1;

                case 5:
                    this.setDef(this.getDef() + 2);
                    this.setMaxDef(this.getMaxDef() + 2);
                    return 2;

                case 6:
                    for (Enemy e : this.getSection().getEnemies()) {
                        // Verifica se inimigo está vivo, caso estiver diminui sua vida e
                        // analisa novamente se ele está vivo ou não, caso morrer atualiza
                        // a pontuação do jogador que realizou a ação de procura;
                        if (e.isAlive()) {
                            e.setDef(e.getDef() - 1);
                            if (!e.isAlive()) {
                                this.getScore().setPoints(this.getScore().getPoints() + e.getAtk() * 10);
                            }
                        }
                    }
                    return 3;

                default:
                    return 0;
            }
        }

        return 0;
    }
}