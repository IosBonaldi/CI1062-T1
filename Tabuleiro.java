import java.util.ArrayList;
import java.util.Random;

public class Tabuleiro {
    private Setor[][] setores;
    final private int altura;
    final private int largura;

    public Tabuleiro(int altura, int largura) {
        if (altura <= 0 || largura <= 0)
            throw new IllegalArgumentException("Altura e/ou largura invalidas!");

        this.altura = altura;
        this.largura = largura;
        this.setores = new Setor[largura][altura];

        /* Aloca os setores */
        for (int y = 0; y < altura; y++)
            for (int x = 0; x < largura; x++)
                setores[x][y] = new Setor(new Coordenada(x, y));

        gerarFonte();
        gerarPortas();
    }

    public Setor[][] getSetores() {
        return setores;
    }

    private void gerarFonte() {
        Random rand = new Random();
        Coordenada coordVirus = new Coordenada(rand.nextInt(largura), rand.nextInt(altura));

        /*
         * Gera novas coordenadas enquanto a coordenada sorteada coincidir com o meio do
         * tabuleiro
         */
        while (coordVirus.getLinha() == largura / 2 && coordVirus.getColuna() == altura / 2) {
            coordVirus.setLinha(rand.nextInt(largura));
            coordVirus.setColuna(rand.nextInt(altura));
        }

        setores[coordVirus.getLinha()][coordVirus.getColuna()].setFonte(true);
    }

    public Setor getSetor(int x, int y) {
        return this.getSetores()[x][y];
    }

    public void setSetores(Setor[][] setores) {
        this.setores = setores;
    }

    private void gerarPortas() {
        ArrayList<Coordenada> setoresVisitados = new ArrayList<Coordenada>();
        acharVirus(new Coordenada(altura / 2, largura / 2), setoresVisitados, false);
        abrirCaminho(setoresVisitados);
    }

    /**
     * 
     * @param coord            Coordenada inicial do jogador
     * @param setoresVisitados Lista de setores ja visitados
     * @param virusAchado      Boolean para saber se o virus da foi achado
     * @return
     */
    private boolean acharVirus(Coordenada coord, ArrayList<Coordenada> setoresVisitados, boolean virusAchado) {
        if (virusAchado == true)
            return virusAchado;
        if (setores[coord.getLinha()][coord.getColuna()].isFonte()) {
            setoresVisitados.add(coord);
            return true;
        }

        setoresVisitados.add(coord);

        ArrayList<Coordenada> coordAleatorias = coordenadasAleatoriasValidas(coord, setoresVisitados);

        /*
         * Se coordAleatorias esta vazio, eh porque nenhum caminho valido foi encontrado
         */
        if (!coordAleatorias.isEmpty()) {
            /* Chama o backtracking paras os movimentos possiveis */
            for (int i = 0; i < coordAleatorias.size() && !virusAchado; i++) {
                virusAchado = acharVirus(coordAleatorias.get(i), setoresVisitados, virusAchado);

                /* Empilha a volta somente quando o virus nao foi achado */
                if (!virusAchado)
                    setoresVisitados.add(coord);
            }
        }

        return virusAchado;
    }

    /**
     * Retorna uma lista de todas as possiveis validas novas coordenadas a partir da
     * coordenada recebida
     * setores que ja foram visitado nao sao considerados validos
     * 
     * @param coord            Coordenada atual
     * @param setoresVisitados
     * @return Lista com possiveis novas coordenadas validas
     */
    private ArrayList<Coordenada> coordenadasAleatoriasValidas(Coordenada coord,
            ArrayList<Coordenada> setoresVisitados) {
        ArrayList<Coordenada> coordAleatorias = new ArrayList<Coordenada>();
        Random rand = new Random();

        Coordenada coord1 = new Coordenada(coord.getLinha() + 1, coord.getColuna());
        Coordenada coord2 = new Coordenada(coord.getLinha() - 1, coord.getColuna());
        Coordenada coord3 = new Coordenada(coord.getLinha(), coord.getColuna() + 1);
        Coordenada coord4 = new Coordenada(coord.getLinha(), coord.getColuna() - 1);

        if (coordenadaEhValida(coord1) && !setorFoiVisitado(coord1, setoresVisitados))
            coordAleatorias.add(coord1);
        if (coordenadaEhValida(coord2) && !setorFoiVisitado(coord2, setoresVisitados))
            coordAleatorias.add(coord2);
        if (coordenadaEhValida(coord3) && !setorFoiVisitado(coord3, setoresVisitados))
            coordAleatorias.add(coord3);
        if (coordenadaEhValida(coord4) && !setorFoiVisitado(coord4, setoresVisitados))
            coordAleatorias.add(coord4);

        /* Embaralha as coordenadas */
        if (coordAleatorias.size() > 1) {
            for (int i = 0; i < coordAleatorias.size(); i++) {
                int randIndex = rand.nextInt(coordAleatorias.size() - 1);

                Coordenada auxAtual = coordAleatorias.get(i);
                Coordenada auxRand = coordAleatorias.get(randIndex);

                coordAleatorias.set(i, auxRand);
                coordAleatorias.set(randIndex, auxAtual);
            }
        }

        return coordAleatorias;
    }

    /**
     * Retora TRUE se a coordenada ja foi visitado e FALSE do contrario
     * 
     * @param coord
     * @param setoresVisitados
     * @return
     */
    private boolean setorFoiVisitado(Coordenada coord, ArrayList<Coordenada> setoresVisitados) {
        for (int i = 0; i < setoresVisitados.size(); i++) {
            Coordenada aux = setoresVisitados.get(i);
            if ((aux.getLinha() == coord.getLinha()) && (aux.getColuna() == coord.getColuna()))
                return true;
        }
        return false;
    }

    /**
     * Retorna TRUE se a coordenada eh valida baseando se nos limites do Tabuleiro
     * do contrario retorna FALSE
     * 
     * @param coord
     * @return
     */
    private boolean coordenadaEhValida(Coordenada coord) {
        if (coord.getLinha() >= altura || coord.getLinha() < 0)
            return false;
        if (coord.getColuna() >= largura || coord.getColuna() < 0)
            return false;

        return true;
    }

    /**
     * Abre o caminho em setores[][] baseando-se na lista de coordenadas recebida
     * 
     * @param setoresVisitados
     */
    private void abrirCaminho(ArrayList<Coordenada> setoresVisitados) {
        for (int i = 0; i < setoresVisitados.size() - 1; i++) {
            Coordenada coordSetorAtual = setoresVisitados.get(i);
            Coordenada coordSetorSeguinte = setoresVisitados.get(i + 1);

            Direcao dir = calcularDirecao(coordSetorAtual, coordSetorSeguinte);

            if (dir == null)
                throw new NullPointerException("Nenhuma direcao valida encontrada!");

            switch (dir) {
                case CIMA:
                    abrirPorta(setores[coordSetorAtual.getColuna()][coordSetorAtual.getLinha()], Direcao.CIMA);
                    abrirPorta(setores[coordSetorAtual.getColuna()][coordSetorAtual.getLinha()], Direcao.BAIXO);
                    break;
                case DIREITA:
                    abrirPorta(setores[coordSetorAtual.getColuna()][coordSetorAtual.getLinha()], Direcao.DIREITA);
                    abrirPorta(setores[coordSetorAtual.getColuna()][coordSetorAtual.getLinha()], Direcao.ESQUERDA);
                    break;
                case BAIXO:
                    abrirPorta(setores[coordSetorAtual.getColuna()][coordSetorAtual.getLinha()], Direcao.BAIXO);
                    abrirPorta(setores[coordSetorAtual.getColuna()][coordSetorAtual.getLinha()], Direcao.CIMA);
                    break;
                case ESQUERDA:
                    abrirPorta(setores[coordSetorAtual.getColuna()][coordSetorAtual.getLinha()], Direcao.ESQUERDA);
                    abrirPorta(setores[coordSetorAtual.getColuna()][coordSetorAtual.getLinha()], Direcao.DIREITA);
                    break;
            }
        }
    }

    /**
     * 
     * @param setor
     * @param direcao
     */
    private void abrirPorta(Setor setor, Direcao direcao) {
        setor.setPorta(direcao);
    }

    /**
     * Retorna a direcao da origem para o destino
     * 
     * @param origem
     * @param destino
     * @return
     */
    private Direcao calcularDirecao(Coordenada origem, Coordenada destino) {
        if ((destino.getLinha() - origem.getLinha()) == 1)
            return Direcao.DIREITA;
        else if ((destino.getLinha() - origem.getLinha()) == -1)
            return Direcao.ESQUERDA;
        else if ((destino.getColuna() - origem.getColuna()) == 1)
            return Direcao.BAIXO;
        else if ((destino.getColuna() - origem.getColuna()) == -1)
            return Direcao.CIMA;

        return null;
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

        if (p1.getSetor() == s && p2.getSetor() == s) {
            return "P12";
        } else if (p1.getSetor() == s) {
            return "P1 ";
        } else if (p2.getSetor() == s) {
            return "P2 ";
        } else if (s.isFonte()) {
            return " X ";
        }

        return "   ";
    }

    /**
     * Movimenta o jogador através dos setores do tabuleiro atualizando seu atributo
     * setor.
     *
     * @param t o tabuleiro no qual o jogador está inserido.
     * @param d a direção para a qual deseja-se mover o jogador.
     */
    public void movimentar(Jogador j, Direcao d) {
        // Impede movimentação em setores que ainda possuam inimigos
        if (j.getSetor().getInimigos().isEmpty()) {
            // Atualiza o atributo setor de acordo com a direção recebida, checando
            // eventuais colisões com as bordas
            switch (d) {
                case CIMA:
                    if (j.getSetor().getCoordenada().getLinha() > 0) {
                        j.setSetor(this.getSetor(j.getSetor().getCoordenada().getLinha() - 1,
                                j.getSetor().getCoordenada().getColuna()));
                    }
                    break;
                case DIREITA:
                    if (j.getSetor().getCoordenada().getColuna() < 4) {
                        j.setSetor(this.getSetor(j.getSetor().getCoordenada().getLinha(),
                                j.getSetor().getCoordenada().getColuna() + 1));
                    }
                    break;
                case BAIXO:
                    if (j.getSetor().getCoordenada().getLinha() < 4) {
                        j.setSetor(this.getSetor(j.getSetor().getCoordenada().getLinha() + 1,
                                j.getSetor().getCoordenada().getColuna()));
                    }
                    break;
                case ESQUERDA:
                    if (j.getSetor().getCoordenada().getColuna() > 0) {
                        j.setSetor(this.getSetor(j.getSetor().getCoordenada().getLinha(),
                                j.getSetor().getCoordenada().getColuna() - 1));
                    }
                    break;
            }

            j.getSetor().setVisitado(true);
        }
    }

    /**
     * Retorna o texto correspondente à uma construção específica de um setor
     * específico
     *
     * @param s o Setor ao qual a Construção que deseja-se visualizar pertence.
     * @param d a Direção correspondente ao Setor desejado dentro de seu setor.
     */
    public String strConstruction(Setor s, Direcao d) {
        switch (d) {
            case CIMA:
                return ((s.getConstrucoes().get(0) == Construcao.PORTA && s.isVisitado()
                        && s.getCoordenada().getLinha() > 0) ? "*" : "-");

            case DIREITA:
                return ((s.getConstrucoes().get(1) == Construcao.PORTA && s.isVisitado()
                        && s.getCoordenada().getColuna() < 4) ? "*" : "|");

            case BAIXO:
                return ((s.getConstrucoes().get(2) == Construcao.PORTA && s.isVisitado()
                        && s.getCoordenada().getLinha() < 4) ? "*" : "-");

            case ESQUERDA:
                return ((s.getConstrucoes().get(3) == Construcao.PORTA && s.isVisitado()
                        && s.getCoordenada().getColuna() > 0) ? "*" : "|");

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
     *      prevalecerá a Construção DIREITA do Setor ESQUERDO em detrimento da
     *      Construção ESQUERDA do Setor DIREITO.
     */
    public String strBoardVConstructions(ArrayList<Jogador> p, Setor[] s) {
        String l = new String();

        l += "|";
        for (int i = 0; i < 4; i++) {
            l += strCorpoSetor(p, s[i]) + strConstruction(s[i], Direcao.DIREITA);
        }
        l += strCorpoSetor(p, s[4]) + "|";

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
    public String strBoardHConstructions(Setor[] s) {
        // String que armazenará o retorno
        String l = new String();

        // Texto correspondente aos Setores, inserido Setor por Setor
        for (int i = 0; i < 5; i++) {
            l += "|-" + strConstruction(s[i], Direcao.BAIXO) + "-";
        }
        l += "|";

        return l;
    }

    /**
     * Retorna o texto correspondente ao zoom das construções horizontais de um
     * Setor específico
     *
     * @param s um Setor cujas construções horizontais deseja-se visualizar.
     */
    public String strZoomHConstruction(Setor s, Direcao d) {
        return "|------" + strConstruction(s, d) + "------|";
    }

    /**
     * Retorna o texto correspondente ao zoom das construções verticais de um
     * Setor específico
     *
     * @param s um Setor cujas construções verticais deseja-se visualizar.
     */
    public String strZoomVConstructions(Setor s) {
        return strConstruction(s, Direcao.ESQUERDA) + "             " + strConstruction(s, Direcao.DIREITA);
    }

    /**
     * Retorna o texto correspondente ao conjunto de Inimigos de um Setor
     * específico.
     *
     * @param s o Setor cujos Inimigos desejam-se visualizar.
     */
    public String strEnemiesAttrs(Setor s) {
        // String que armazenará o retorno
        String r = new String();

        // Texto correspondente aos Inimigos, inserido Inimigo a Inimigo
        r += "| ";
        for (int i = 1; i < 3; i++) {
            r += s.getInimigo(i) == null ? "   " : s.getInimigo(i).getAtk() + "/" + s.getInimigo(i).getDef();
            r += " ";
        }
        r += s.getInimigo(3) == null ? "   " : s.getInimigo(3).getAtk() + "/" + s.getInimigo(3).getDef();
        r += " |";

        return r;
    }

    /**
     * Retorna o texto correspondente aos atributos ATK/DEF de um Jogador
     * específico.
     *
     * @param p lista de Jogadores cujos atributos desejam-se visualizar.
     * @param i número do mini-setor a ser impresso.
     */
    public String strPlayersName(ArrayList<Jogador> p, int i) {
        // Variáveis auxiliares para legibilidade
        Jogador p1 = p.get(0);
        Jogador p2 = p.get(1);
        if (p1.getSetor() == p2.getSetor()) {
            return "|   P1    P2  |";
        } else {
            if (i == 1) {
                return "|   P1         |";
            } else if (i == 2) {
                return "|   P2         |";
            }
        }
        return "";
    }

    /**
     * Retorna o texto correspondente aos atributos ATK/DEF de um Jogador
     * específico.
     *
     * @param p lista de Jogadores cujos atributos desejam-se visualizar.
     * @param i número do mini-setor a ser impresso.
     */
    public String strZoomPlayersAttrs(ArrayList<Jogador> p, int i) {
        // Variáveis auxiliares para legibilidade
        Jogador p1 = p.get(0);
        Jogador p2 = p.get(1);
        if (p1.getSetor() == p2.getSetor()) {
            return "|  " + p1.getAtk() + "/" + p1.getDef() + "   " + p2.getAtk() + "/" + p2.getDef() + "  |";
        } else {
            if (i == 1) {
                return "|  " + p1.getAtk() + "/" + p1.getDef() + "        |";
            } else if (i == 2) {
                return "|  " + p2.getAtk() + "/" + p2.getDef() + "        |";
            }
        }
        return "";
    }

    /**
     * Retorna o texto correspondente às Coordenadas de um Jogador específico.
     *
     * @param p o Jogador cujas Coordenadas desejam-se visualizar.
     */
    public String strPlayerCoordinates(Jogador p) {
        return "Setor [" + p.getSetor().getCoordenada().getLinha() + "," + p.getSetor().getCoordenada().getColuna()
                + "]";
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
        Setor sp1 = p1.getSetor();
        Setor sp2 = p2.getSetor();
        // Coordenadas dos players no zoom
        String zoomPC = strPlayerCoordinates(p1) + "       " + strPlayerCoordinates(p2);
        // Construções horizontais superiores no zoom
        String zoomHCT = strZoomHConstruction(sp1, Direcao.CIMA) + "   " + strZoomHConstruction(sp2, Direcao.CIMA);
        // Construções horizontais inferiores no zoom
        String zoomHCB = strZoomHConstruction(sp1, Direcao.BAIXO) + "   " + strZoomHConstruction(sp2, Direcao.BAIXO);
        // Construções verticais superiores no zoom
        String zoomVC = strZoomVConstructions(sp1) + "   " + strZoomVConstructions(sp2);
        // Inimigos no zoom
        String zoomEA = strEnemiesAttrs(sp1) + "   " + strEnemiesAttrs(sp2);
        // Atributos ATK/DEF dos players no zoom
        String zoomPA = strZoomPlayersAttrs(p, 1) + "   " + strZoomPlayersAttrs(p, 2);
        // Nome dos players no zoom
        String zoomPN = strPlayersName(p, 1) + "   " + strPlayersName(p, 2);

        // Texto correspondente ao tabuleiro, inserido linha a linha
        r += "-----------------------------\n";
        r += "|   Antivírus por um dia    |\n";
        r += "-----------------------------\n";
        r += "      1   2   3   4   5\n";
        r += "    |---|---|---|---|---|\n";
        r += "1   " + strBoardVConstructions(p, setores[0]) + "       " + zoomPC + "\n";
        r += "    " + strBoardHConstructions(setores[0]) + "\n";
        r += "2   " + strBoardVConstructions(p, setores[1]) + "     " + zoomHCT + "\n";
        r += "    " + strBoardHConstructions(setores[1]) + "     " + zoomEA + "\n";
        r += "3   " + strBoardVConstructions(p, setores[2]) + "     |             |   |             |\n";
        r += "    " + strBoardHConstructions(setores[2]) + "     " + zoomVC + "\n";
        r += "4   " + strBoardVConstructions(p, setores[3]) + "     " + zoomPN + "\n";
        r += "    " + strBoardHConstructions(setores[3]) + "     " + zoomPA + "\n";
        r += "5   " + strBoardVConstructions(p, setores[4]) + "     " + zoomHCB + "\n";
        r += "    " + strBoardHConstructions(setores[4]) + "\n";

        return r;
    }

}
