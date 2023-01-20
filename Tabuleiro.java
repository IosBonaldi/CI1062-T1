import java.util.ArrayList;
import java.util.Random;

public class Tabuleiro {
    private Setor[][] setores;
    final private int altura;
    final private int largura;

    public Tabuleiro(int altura, int largura) {
        if(altura <= 0 || largura <= 0)
            throw new IllegalArgumentException("Altura e/ou largura invalidas!");

        this.altura = altura;
        this.largura = largura;
        this.setores = new Setor[largura][altura];

        /* Aloca os setores */
        for(int y = 0; y < altura; y++)
            for(int x = 0; x < largura; x++)
                setores[x][y] = new Setor(new Coordenada(x, y));

        gerarFonte();
        gerarPortas();
    }

    public Setor[][] getSetores() {
        return setores;
    }

    public void setSetores(Setor[][] setores) {
        this.setores = setores;
    }

    private void gerarFonte(){
        Random rand = new Random();
        Coordenada coordVirus = new Coordenada(rand.nextInt(largura), rand.nextInt(altura));

        /* Gera novas coordenadas enquanto a coordenada sorteada coincidir com o meio do tabuleiro */
        while(coordVirus.getX() == largura/2 && coordVirus.getY() == altura/2){
            coordVirus.setX(rand.nextInt(largura));
            coordVirus.setY(rand.nextInt(altura));
        }

        setores[coordVirus.getX()][coordVirus.getY()].setFonte(true);
    }

    private void gerarPortas() {

    }

    /**
     * Retorna o texto correspondente ao interior de um setor específico.
     *
     * @param p uma ArrayList contendo os dois Jogadores existentes no jogo.
     * @param s o Setor cujo interior deseja-se visualizar.
     */
    public String strCorpoSetor(ArrayList<Jogador> p, Setor s) {
        Jogador p1 = p.get(0);
        Jogador p2 = p.get(1);

        if (p1.setor == s && p2.setor == s) {
            return "P12";
        } else if (p1.setor == s) {
            return "P1 ";
        } else if (p2.setor == s) {
            return "P2 ";
        } else if (s.isFonte()) {
            return " X ";
        }

        return "   ";
    }

    /**
     * Retorna o texto correspondente à uma construção específica de um setor
     * específico
     *
     * @param s o Setor ao qual a Construção que deseja-se visualizar pertence.
     * @param d a Direção correspondente ao Setor desejado dentro de seu setor.
     */
    public String strConstrucao(Setor s, Direcao d) {
        switch (d) {
            case CIMA:
                return ((s.getConstrucoes().get(0) == Construcao.PORTA && s.isVisitado()) ? "*" : "-");

            case DIREITA:
                return ((s.getConstrucoes().get(1) == Construcao.PORTA && s.isVisitado()) ? "*" : "|");

            case BAIXO:
                return ((s.getConstrucoes().get(2) == Construcao.PORTA && s.isVisitado()) ? "*" : "-");

            case ESQUERDA:
                return ((s.getConstrucoes().get(3) == Construcao.PORTA && s.isVisitado()) ? "*" : "|");

            default:
                return "\\";
        }
    }

    /**
     * Retorna o texto correspondente aos interiores e construções verticais de uma
     * linha de Setores específica.
     *
     * @param p uma ArrayList contendo os dois Jogadores existentes no jogo.
     * @param s um array contendo a linha de Setores a qual deseja-se visualizar.
     * @see inconsistencia em caso de Construções inconsistentes horizontalmente,
     *      prevalecerá a Construção ESQUERDA do Setor DIREITO em detrimento da
     *      Construção DIREITA do Setor ESQUERDO.
     */
    public String strLinhaSetores(ArrayList<Jogador> p, Setor[] s) {
        String l = new String();

        for (int i = 0; i < 5; i++) {
            l += strConstrucao(s[i], Direcao.ESQUERDA) + strCorpoSetor(p, s[i]);
        }
        l += strConstrucao(s[4], Direcao.DIREITA);

        return l;
    }

    /**
     * Retorna o texto correspondente às construções horizontais inferiores de uma
     * linha de Setores específica.
     *
     * @param s um array contendo a linha de Setores cujas bases desejam-se
     *          visualizar.
     * @see inconsistencia em caso de Construções inconsistentes verticalmente,
     *      prevalecerá a Construção INFERIOR do Setor SUPERIOR em detrimento da
     *      Construção SUPERIOR do Setor INFERIOR.
     */
    public String strBaseSetores(Setor[] s) {
        // String que armazenará o retorno
        String l = new String();

        // Texto correspondente aos Setores, inserido Setor por Setor
        for (int i = 0; i < 5; i++) {
            l += "|-" + strConstrucao(s[i], Direcao.BAIXO) + "-";
        }
        l += "|";

        return l;
    }

    /**
     * Retorna o texto correspondente ao conjunto de Inimigos de um Setor
     * específico.
     *
     * @param s o Setor cujos Inimigos desejam-se visualizar.
     */
    public String strInimigos(Setor s) {
        // String que armazenará o retorno
        String r = new String();

        // Texto correspondente aos Inimigos, inserido Inimigo a Inimigo
        for (int i = 0; i < 2; i++) {
            r += s.getInimigo(i) == null ? "   " : s.getInimigo(i).getATK() + "/" + s.getInimigo(i).getDEF();
            r += " ";
        }
        r += s.getInimigo(2) == null ? "   " : s.getInimigo(2).getATK() + "/" + s.getInimigo(2).getDEF();

        return r;
    }

    /**
     * Retorna o texto correspondente aos atributos ATK/DEF de um Jogador
     * específico.
     *
     * @param p o Jogador cujos atributos desejam-se visualizar.
     */
    public String strATKDEF(Jogador p) {
        return p.getATK() + "/" + p.getDEF();
    }

    /**
     * Retorna o texto correspondente às Coordenadas de um Jogador específico.
     *
     * @param p o Jogador cujas Coordenadas desejam-se visualizar.
     */
    public String strCoordenada(Jogador p) {
        return p.getSetor().getCoordenada().getX() + "," + p.getSetor().getCoordenada().getY();
    }

    /**
     * Retorna o texto correspondente ao Tabuleiro completo do jogo.
     *
     * @param p uma ArrayList contendo os dois Jogadores existentes no jogo.
     */
    public String strTabuleiro(ArrayList<Jogador> p) {
        // String que armazenará o retorno
        String r = new String();
        // Variáveis auxiliares para legibilidade
        Jogador p1 = p.get(0);
        Jogador p2 = p.get(1);

        // Texto correspondente ao tabuleiro, inserido linha a linha
        r += "-----------------------------\n";
        r += "|   Antivírus por um dia    |\n";
        r += "-----------------------------\n";
        r += "      1   2   3   4   5\n";
        r += "    |---|---|---|---|---|\n";
        r += "    " + strLinhaSetores(p, setores[0]) + "       Setor [" + strCoordenada(p1) + "]       Setor ["
                + strCoordenada(p2) + "]\n";
        r += "    " + strBaseSetores(setores[0]) + "\n";
        r += "    " + strLinhaSetores(p, setores[1]) + "     |------*------|   |------*------|\n";
        r += "    " + strBaseSetores(setores[1]) + "     | " + strInimigos(p1.getSetor()) + " |   | "
                + strInimigos(p2.getSetor()) + " |\n";
        r += "    " + strLinhaSetores(p, setores[2]) + "     |             |   |             |\n";
        r += "    " + strBaseSetores(setores[2]) + "     *             |   *             |\n";
        r += "    " + strLinhaSetores(p, setores[3]) + "     |   P1        |   |   P2        |\n";
        r += "    " + strBaseSetores(setores[3]) + "     |   " + strATKDEF(p1) + "       |   |   " + strATKDEF(p2)
                + "       |\n";
        r += "    " + strLinhaSetores(p, setores[4]) + "     |------*------|   |------*------|\n";
        r += "    |---|---|---|---|---|\n";

        return r;
    }
}
