import java.util.ArrayList;
import java.util.Random;

public class Tabuleiro {
    private Setor[][] setores;
    final private int altura;
    final private int largura;

    public Tabuleiro(int altura, int largura) {
        if (5 > altura || altura > 20 || 5 > largura || largura > 20)
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

    public int getAltura() {
        return altura;
    }

    public int getLargura() {
        return largura;
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

        int quantidadePortasAbertas = 0;
        int caminhosEncontrados = 0;

        /*
         * A quantidade de portas aberta "2*altura*largura" foi definido testando
         * diferentes possibilidades
         * e esse valor foi suficiente para construir um mapa com varias possibilidades
         * de caminhos
         */
        while (quantidadePortasAbertas < 2 * altura * largura || caminhosEncontrados < 3) {
            acharVirus(new Coordenada(altura / 2, largura / 2), setoresVisitados, false);
            abrirCaminho(setoresVisitados);
            quantidadePortasAbertas += setoresVisitados.size() - 1; // A ultima coordenada nao conta, porque e a fonte,
                                                                    // por isso o -1
            caminhosEncontrados += 1;
            setoresVisitados.clear();
        }
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
                    abrirPorta(setores[coordSetorAtual.getLinha()][coordSetorAtual.getColuna()], Direcao.CIMA);
                    abrirPorta(setores[coordSetorSeguinte.getLinha()][coordSetorSeguinte.getColuna()], Direcao.BAIXO);
                    break;
                case DIREITA:
                    abrirPorta(setores[coordSetorAtual.getLinha()][coordSetorAtual.getColuna()], Direcao.DIREITA);
                    abrirPorta(setores[coordSetorSeguinte.getLinha()][coordSetorSeguinte.getColuna()],
                            Direcao.ESQUERDA);
                    break;
                case BAIXO:
                    abrirPorta(setores[coordSetorAtual.getLinha()][coordSetorAtual.getColuna()], Direcao.BAIXO);
                    abrirPorta(setores[coordSetorSeguinte.getLinha()][coordSetorSeguinte.getColuna()], Direcao.CIMA);
                    break;
                case ESQUERDA:
                    abrirPorta(setores[coordSetorAtual.getLinha()][coordSetorAtual.getColuna()], Direcao.ESQUERDA);
                    abrirPorta(setores[coordSetorSeguinte.getLinha()][coordSetorSeguinte.getColuna()], Direcao.DIREITA);
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
        if ((destino.getColuna() - origem.getColuna()) == 1)
            return Direcao.DIREITA;
        else if ((destino.getColuna() - origem.getColuna()) == -1)
            return Direcao.ESQUERDA;
        else if ((destino.getLinha() - origem.getLinha()) == 1)
            return Direcao.BAIXO;
        else if ((destino.getLinha() - origem.getLinha()) == -1)
            return Direcao.CIMA;

        return null;
    }

    /**
     * Fornece texto correspondente ao interior de um Setor específico.
     *
     * @param p uma ArrayList contendo os dois Jogadores existentes no jogo.
     * @param s o Setor cujo interior deseja-se visualizar.
     * @return uma String contendo o interior do Setor, mostrando o(s) jogador(es)
     *         (P1, P2 ou P12), a fonte ( X ) ou vazio( ).
     */
    private String strSectionBody(ArrayList<Jogador> p, Setor s) {
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
     * Movimenta o Jogador através dos Setores do Tabuleiro, atualizando seu
     * atributo Setor.
     *
     * @param j o Jogador que será movimentado.
     * @param d a Direção para a qual deseja-se mover o Jogador.
     * @return true se o jogador tiver sido movido, false caso o movimento seja
     *         inválido
     */
    public boolean movimentar(Jogador j, Direcao d) {
        Setor curSection = j.getSetor();
        Setor newSection = this.bordererSection(curSection, d);
        ArrayList<Construcao> constructions = curSection.getConstrucoes();

        // Checa se não há mais inimigos no Setor, se o novo Setor é válido e se há uma
        // PORTA para o Jogador passar
        if (curSection.isThereEnemyAlive() && newSection != null
                && constructions.get(d.ordinal()) == Construcao.PORTA) {
            j.setSetor(newSection);
            newSection.setVisitado(true);
            return true;
        }

        return false;
    }

    /**
     * Fornece o Setor fronteiriço (vizinho) à um Setor específico.
     *
     * @param s o Setor cujo vizinho deseja-se obter.
     * @param d a Direção do vizinho desejado.
     * @return o Setor fronteiriço ao Setor fornecido na Direção fornecida.
     */
    private Setor bordererSection(Setor s, Direcao d) {
        int curLine = s.getCoordenada().getLinha();
        int curColumn = s.getCoordenada().getColuna();
        switch (d) {
            case CIMA:
                return ((curLine > 0) ? this.getSetores()[curLine - 1][curColumn] : null);
            case DIREITA:
                return ((curColumn < (this.getLargura() - 1)) ? this.getSetores()[curLine][curColumn + 1] : null);
            case BAIXO:
                return ((curLine < (this.getAltura() - 1)) ? this.getSetores()[curLine + 1][curColumn] : null);
            case ESQUERDA:
                return ((curColumn > 0) ? this.getSetores()[curLine][curColumn - 1] : null);
            default:
                return null;
        }
    }

    /**
     * Informa se uma Construção é visível ou não.
     *
     * @param s o Setor da Construção cuja visibilidade deseja-se conhecer.
     * @param d a Direção do construção no referido Setor.
     * @return true se a Construção é visível, false caso contrário.
     */
    private boolean constructionVisibility(Setor s, Direcao d) {
        int curLine = s.getCoordenada().getLinha();
        int curColumn = s.getCoordenada().getColuna();

        switch (d) {
            case CIMA:
                return (curLine > 0 && (s.isVisitado()
                        || this.bordererSection(s, Direcao.CIMA).isVisitado()));
            case DIREITA:
                return (curColumn < (this.getLargura() - 1) && (s.isVisitado()
                        || this.bordererSection(s, Direcao.DIREITA).isVisitado()));
            case BAIXO:
                return (curLine < (this.getAltura() - 1) && (s.isVisitado()
                        || this.bordererSection(s, Direcao.BAIXO).isVisitado()));
            case ESQUERDA:
                return (curColumn > 0 && (s.isVisitado()
                        || this.bordererSection(s, Direcao.ESQUERDA).isVisitado()));
            default:
                return false;
        }
    }

    /**
     * Fornece o texto correspondente à uma construção específica de um setor
     * específico
     *
     * @param s o Setor ao qual a Construção que deseja-se visualizar pertence.
     * @param d a Direção correspondente ao Setor desejado dentro de seu setor.
     * @return uma String correspondente a Construção solicitada, seja ela PORTA (*)
     *         ou PAREDE(| ou -)
     */
    private String strConstruction(Setor s, Direcao d) {
        switch (d) {
            case CIMA:
            case BAIXO:
                return ((s.getConstrucoes().get(d.ordinal()) == Construcao.PORTA && this.constructionVisibility(s, d))
                        ? "*"
                        : "-");

            case DIREITA:
            case ESQUERDA:
                return ((s.getConstrucoes().get(d.ordinal()) == Construcao.PORTA && this.constructionVisibility(s, d))
                        ? "*"
                        : "|");

            default:
                return "\\";
        }
    }

    /**
     * Fornece o texto correspondente aos interiores e construções verticais de uma
     * linha de Setores específica.
     *
     * @param p uma ArrayList contendo os dois Jogadores existentes no jogo.
     * @param s um array contendo a linha de Setores a qual deseja-se visualizar.
     * @return uma String correspondente à linha se Setores solicitada no formato
     *         | Setor Construção ... Construção Setor |
     * @see inconsistencia em caso de Construções inconsistentes horizontalmente,
     *      prevalecerá a Construção DIREITA do Setor ESQUERDO em detrimento da
     *      Construção ESQUERDA do Setor DIREITO.
     */
    private String strBoardVConstructions(ArrayList<Jogador> p, Setor[] s) {
        String l = new String();

        l += "|";
        for (int i = 0; i < this.getLargura() - 1; i++) {
            l += strSectionBody(p, s[i]) + strConstruction(s[i], Direcao.DIREITA);
        }
        l += strSectionBody(p, s[this.getLargura() - 1]) + "|";

        return l;
    }

    /**
     * Fornece o texto correspondente às construções horizontais inferiores de uma
     * linha de Setores específica.
     *
     * @param s um array contendo a linha de Setores cujas bases desejam-se
     *          visualizar.
     * @return uma String correspondente à linha de construções inferiores
     *         solicitada no formato |-Construção-|...|-Construção-|
     * @see inconsistencia em caso de Construções inconsistentes verticalmente,
     *      prevalecerá a Construção INFERIOR do Setor SUPERIOR em detrimento da
     *      Construção SUPERIOR do Setor INFERIOR.
     */
    private String strBoardHConstructions(Setor[] s) {
        // String que armazenará o retorno
        String l = new String();

        // Texto correspondente aos Setores, inserido Setor por Setor
        for (int i = 0; i < this.getLargura(); i++) {
            l += "|-" + strConstruction(s[i], Direcao.BAIXO) + "-";
        }
        l += "|";

        return l;
    }

    /**
     * Fornece o texto correspondente ao zoom das construções horizontais de um
     * Setor específico
     *
     * @param s um Setor cujas construções horizontais deseja-se visualizar.
     * @return uma String correspondente ao zoom das construções solicitadas no
     *         formato |------Construção------|.
     */
    private String strZoomHConstruction(Setor s, Direcao d) {
        return "|------" + strConstruction(s, d) + "------|";
    }

    /**
     * Fornece o texto correspondente ao zoom das construções verticais de um
     * Setor específico
     *
     * @param s um Setor cujas construções verticais deseja-se visualizar em zoom.
     * @return uma String correspondente ao zoom das Construções solicitadas no
     *         formato Construção Construção
     */
    private String strZoomVConstructions(Setor s) {
        return strConstruction(s, Direcao.ESQUERDA) + "             " + strConstruction(s, Direcao.DIREITA);
    }

    /**
     * Fornece o texto correspondente ao conjunto de Inimigos de um Setor
     * específico.
     *
     * @param s o Setor cujos Inimigos desejam-se visualizar.
     * @return uma String correspondente aos Inimigos solicitados no formato
     *         | ATK/DEF ATK/DEF ATK/DEF |
     */
    private String strEnemiesAttrs(Setor s) {
        // String que armazenará o retorno
        String r = new String();
        boolean sIsHidden = (s.getTipo() == SetorTipos.OCULTO);

        // Texto correspondente aos Inimigos, inserido Inimigo a Inimigo
        r += "| ";
        for (int i = 0; i < 3; i++) {
            Inimigo e = s.getInimigo(i);
            boolean eIsNull = (e == null);
            r += (eIsNull || sIsHidden) ? "    " : e.getAtk() + "/" + e.getDef() + " ";
        }
        r += "|";

        return r;
    }

    /**
     * Fornece o texto correspondente ao(s) nome(s) do(s) Jogador(es) de um zoom de
     * Setor específico
     * específico.
     *
     * @param p lista de Jogadores cujos atributos desejam-se visualizar.
     * @param i número do zoom de setor a ser impresso.
     * @return uma String correspondente aos nomes solicitados no formato
     *         | P1 P2 |
     */
    private String strPlayersName(ArrayList<Jogador> p, int i) {
        // Variáveis auxiliares para legibilidade
        Jogador p1 = p.get(0);
        Jogador p2 = p.get(1);
        if (p1.getSetor() == p2.getSetor()) {
            return "|   P1    P2  |";
        } else {
            if (i == 1) {
                return "|   P1        |";
            } else if (i == 2) {
                return "|   P2        |";
            }
        }
        return "";
    }

    /**
     * Fornece o texto correspondente aos atributos ATK/DEF do(s) Jogador(es) de um
     * zoom de Setor específico
     *
     * @param p lista de Jogadores cujos atributos desejam-se visualizar.
     * @param i número do zoom de setor a ser impresso.
     * @return uma String correspondente aos atributos solicitados no formato
     *         | ATK/DEF ATK/DEF |
     */
    private String strZoomPlayersAttrs(ArrayList<Jogador> p, int i) {
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
     * Fornece o texto correspondente ao cabeçalho do Tabuleiro
     *
     * @return uma String correspondente o cabeçalho do Tabuleiro
     */
    private String strBoardHeader() {
        String r = new String();
        r += "   ";
        for (int i = 1; i <= this.getLargura(); i++) {
            if (i <= 10) {
                r += "   " + i;
            } else {
                r += "  " + i;
            }
        }
        r += "\n";
        r += "    |";
        for (int i = 1; i <= this.getLargura(); i++) {
            r += "---|";
        }
        r += "\n";
        return r;
    }

    /**
     * Fornece o texto correspondente às Coordenadas de um Jogador específico.
     *
     * @param p o Jogador cujas Coordenadas desejam-se visualizar.
     * @return uma String correspondente à Coordenada solicitada no formado Setor
     *         [X, Y].
     */
    private String strPlayerCoordinates(Jogador p) {
        return "Setor [" + p.getSetor().getCoordenada().getLinha() + "," + p.getSetor().getCoordenada().getColuna()
                + "]";
    }

    /**
     * Fornece uma string de tamanho fixado a partir de uma String base maior ou
     * menor.
     *
     * @param s      a String base.
     * @param length o tamanho da String desejada.
     * @return a String base concatenada à whitespaces caso seja menor que o tamanho
     *         fixado ou a String base cortada caso seja maior que o tamanho fixado
     */
    private String sizedString(String s, int length) {
        return (s.length() < length) ? String.format("%-" + length + "." + length + "s", s)
                : s.substring(0, length);
    }

    /**
     * Fornece o texto correspondente ao Tabuleiro completo do jogo.
     *
     * @param p uma ArrayList contendo os dois Jogadores existentes no jogo.
     * @return uma String correspondente ao Tabuleiro.
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
        String sp1Type = sizedString(sp1.getTipo().name().toLowerCase(), 7);
        String sp2Type = sizedString(sp2.getTipo().name().toLowerCase(), 7);

        // Texto correspondente ao tabuleiro, inserido linha a linha até a quinta linha
        // do tabuleiro
        r += "-----------------------------\n";
        r += "|   Antivírus por um dia    |\n";
        r += "-----------------------------\n";
        r += strBoardHeader();
        r += "1   " + strBoardVConstructions(p, setores[0]) + "       " + zoomPC + "\n";
        r += "    " + strBoardHConstructions(setores[0]) + "       " + sp1Type + "           " + sp2Type + "\n";
        r += "2   " + strBoardVConstructions(p, setores[1]) + "     " + zoomHCT + "\n";
        r += "    " + strBoardHConstructions(setores[1]) + "     " + zoomEA + "\n";
        r += "3   " + strBoardVConstructions(p, setores[2]) + "     |             |   |             |\n";
        r += "    " + strBoardHConstructions(setores[2]) + "     " + zoomVC + "\n";
        r += "4   " + strBoardVConstructions(p, setores[3]) + "     " + zoomPN + "\n";
        r += "    " + strBoardHConstructions(setores[3]) + "     " + zoomPA + "\n";
        r += "5   " + strBoardVConstructions(p, setores[4]) + "     " + zoomHCB + "\n";
        r += "    " + strBoardHConstructions(setores[4]) + "\n";
        // Linhas posteriores a quinta linha do tabuleiro
        for (int i = 5; i < this.getAltura(); i++) {
            r += sizedString(Integer.toString(i + 1), 2) + "  " + strBoardVConstructions(p, setores[i]) + "\n";
            r += "    " + strBoardHConstructions(setores[i]) + "\n";
        }

        return r;
    }

}
