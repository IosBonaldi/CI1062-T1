import java.util.ArrayList;
import java.util.Random;

public class Board {
    private Section[][] section;
    final private int height;
    final private int width;

    public Board(int height, int width) {
        if (5 > height || height > 20 || 5 > width || width > 20)
            throw new IllegalArgumentException();

        this.height = height;
        this.width = width;
        this.section = new Section[width][height];

        /* Aloca os setores */
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                section[x][y] = new Section(new Coordinate(x, y));

        createSource();
        createDoors();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Section[][] getSection() {
        return section;
    }

    private void createSource() {
        Random rand = new Random();
        Coordinate coordVirus = new Coordinate(rand.nextInt(width), rand.nextInt(height));

        /*
         * Gera novas coordenadas enquanto a coordenada sorteada coincidir com o meio do
         * tabuleiro
         */
        while (coordVirus.getRow() == width / 2 && coordVirus.getColumn() == height / 2) {
            coordVirus.setRow(rand.nextInt(width));
            coordVirus.setColumn(rand.nextInt(height));
        }

        section[coordVirus.getRow()][coordVirus.getColumn()].setSource(true);
    }

    public Section getSection(int row, int column) {
        return this.getSection()[row][column];
    }

    public void setSection(Section[][] sections) {
        this.section = sections;
    }

    private void createDoors() {
        ArrayList<Coordinate> visitedSections = new ArrayList<Coordinate>();

        int openedDoorsQuantity = 0;
        int foundPaths = 0;

        /*
         * A quantidade de portas aberta "2*altura*largura" foi definido testando
         * diferentes possibilidades
         * e esse valor foi suficiente para construir um mapa com varias possibilidades
         * de caminhos
         */
        while (openedDoorsQuantity < 2 * height * width || foundPaths < 3) {
            findVirus(new Coordinate(height / 2, width / 2), visitedSections, false);
            openPath(visitedSections);
            openedDoorsQuantity += visitedSections.size() - 1; // A ultima coordenada nao conta, porque e a fonte,
                                                               // por isso o -1
            foundPaths += 1;
            visitedSections.clear();
        }
    }

    /**
     * 
     * @param coord           Coordenada inicial do jogador
     * @param visitedSections Lista de setores ja visitados
     * @param foundVirus      Boolean para saber se o virus da foi achado
     * @return
     */
    private boolean findVirus(Coordinate coord, ArrayList<Coordinate> visitedSections, boolean foundVirus) {
        if (foundVirus == true)
            return foundVirus;
        if (section[coord.getRow()][coord.getColumn()].isSource()) {
            visitedSections.add(coord);
            return true;
        }

        visitedSections.add(coord);

        ArrayList<Coordinate> randomCoordinates = validRandomCoordinates(coord, visitedSections);

        /*
         * Se coordAleatorias esta vazio, eh porque nenhum caminho valido foi encontrado
         */
        if (!randomCoordinates.isEmpty()) {
            /* Chama o backtracking paras os movimentos possiveis */
            for (int i = 0; i < randomCoordinates.size() && !foundVirus; i++) {
                foundVirus = findVirus(randomCoordinates.get(i), visitedSections, foundVirus);

                /* Empilha a volta somente quando o virus nao foi achado */
                if (!foundVirus)
                    visitedSections.add(coord);
            }
        }

        return foundVirus;
    }

    /**
     * Retorna uma lista de todas as possiveis validas novas coordenadas a partir da
     * coordenada recebida
     * setores que ja foram visitado nao sao considerados validos
     * 
     * @param coord           Coordenada atual
     * @param visitedSections
     * @return Lista com possiveis novas coordenadas validas
     */
    private ArrayList<Coordinate> validRandomCoordinates(Coordinate coord,
            ArrayList<Coordinate> visitedSections) {
        ArrayList<Coordinate> randomCoordinates = new ArrayList<Coordinate>();
        Random rand = new Random();
        
        int row = coord.getRow();
        int column = coord.getColumn();

        /* Gera as coordenadas */
        if (isCoordinateValid(row + 1, column) && !isSectionVisited(row + 1, column, visitedSections))
            randomCoordinates.add(new Coordinate(row + 1, column));
        if (isCoordinateValid(row - 1, column) && !isSectionVisited(row - 1, column, visitedSections))
            randomCoordinates.add(new Coordinate(row - 1, column));
        if (isCoordinateValid(row, column + 1) && !isSectionVisited(row, column + 1, visitedSections))
            randomCoordinates.add(new Coordinate(row, column + 1));
        if (isCoordinateValid(row, column - 1) && !isSectionVisited(row, column - 1, visitedSections))
            randomCoordinates.add(new Coordinate(row, column - 1));

        /* Embaralha as coordenadas */
        if (randomCoordinates.size() > 1) {
            for (int i = 0; i < randomCoordinates.size(); i++) {
                int randIndex = rand.nextInt(randomCoordinates.size() - 1);

                Coordinate curAux = randomCoordinates.get(i);
                Coordinate randAux = randomCoordinates.get(randIndex);

                randomCoordinates.set(i, randAux);
                randomCoordinates.set(randIndex, curAux);
            }
        }

        return randomCoordinates;
    }

    /**
     * Retora TRUE se a coordenada ja foi visitado e FALSE do contrario
     * 
     * @param coord
     * @param visitedSections
     * @return
     */
    private boolean isSectionVisited(int row, int column, ArrayList<Coordinate> visitedSections) {
        for (int i = 0; i < visitedSections.size(); i++) {
            Coordinate aux = visitedSections.get(i);
            if ((aux.getRow() == row) && (aux.getColumn() == column))
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
    private boolean isCoordinateValid(int row, int column) {
        if (row >= height || row < 0)
            return false;
        if (column >= width || column < 0)
            return false;

        return true;
    }

    /**
     * Abre o caminho em setores[][] baseando-se na lista de coordenadas recebida
     * 
     * @param visitedSection
     */
    private void openPath(ArrayList<Coordinate> visitedSection) {
        for (int i = 0; i < visitedSection.size() - 1; i++) {
            Coordinate curSectionCoordinate = visitedSection.get(i);
            Coordinate nextSectionCoordinate = visitedSection.get(i + 1);

            Direction dir = calculateDirection(curSectionCoordinate, nextSectionCoordinate);

            if (dir == null)
                throw new NullPointerException();

            switch (dir) {
                case UP:
                    openDoor(section[curSectionCoordinate.getRow()][curSectionCoordinate.getColumn()], Direction.UP);
                    openDoor(section[nextSectionCoordinate.getRow()][nextSectionCoordinate.getColumn()],
                            Direction.DOWN);
                    break;
                case RIGHT:
                    openDoor(section[curSectionCoordinate.getRow()][curSectionCoordinate.getColumn()],
                            Direction.RIGHT);
                    openDoor(section[nextSectionCoordinate.getRow()][nextSectionCoordinate.getColumn()],
                            Direction.LEFT);
                    break;
                case DOWN:
                    openDoor(section[curSectionCoordinate.getRow()][curSectionCoordinate.getColumn()],
                            Direction.DOWN);
                    openDoor(section[nextSectionCoordinate.getRow()][nextSectionCoordinate.getColumn()],
                            Direction.UP);
                    break;
                case LEFT:
                    openDoor(section[curSectionCoordinate.getRow()][curSectionCoordinate.getColumn()],
                            Direction.LEFT);
                    openDoor(section[nextSectionCoordinate.getRow()][nextSectionCoordinate.getColumn()],
                            Direction.RIGHT);
                    break;
            }
        }
    }

    /**
     * 
     * @param section
     * @param direction
     */
    private void openDoor(Section section, Direction direction) {
        section.setDoor(direction);
    }

    /**
     * Retorna a direcao da origem para o destino
     * 
     * @param origin
     * @param destiny
     * @return
     */
    private Direction calculateDirection(Coordinate origin, Coordinate destiny) {
        if ((destiny.getColumn() - origin.getColumn()) == 1)
            return Direction.RIGHT;
        else if ((destiny.getColumn() - origin.getColumn()) == -1)
            return Direction.LEFT;
        else if ((destiny.getRow() - origin.getRow()) == 1)
            return Direction.DOWN;
        else if ((destiny.getRow() - origin.getRow()) == -1)
            return Direction.UP;

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
    private String strSectionBody(ArrayList<Player> p, Section s) {
        Player p1 = p.get(0);
        Player p2 = p.get(1);

        if (p1.getSection() == s && p2.getSection() == s) {
            return "P12";
        } else if (p1.getSection() == s) {
            return "P1 ";
        } else if (p2.getSection() == s) {
            return "P2 ";
        } else if (s.isSource()) {
            return " X ";
        }
        return "   ";
    }

    /**
     * Movimenta o Jogador através dos Setores do Tabuleiro, atualizando seu
     * atributo Setor.
     *
     * @param p o Jogador que será movimentado.
     * @param d a Direção para a qual deseja-se mover o Jogador.
     * @return true se o jogador tiver sido movido, false caso o movimento seja
     *         inválido
     */
    public boolean movePlayer(Player p, Direction d) {
        Section curSection = p.getSection();
        Section newSection = this.bordererSection(curSection, d);
        ArrayList<Construction> constructions = curSection.getConstructions();

        // Checa se não há mais inimigos no Setor, se o novo Setor é válido e se há uma
        // PORTA para o Jogador passar
        if (!curSection.existAnEnemyAlive() && newSection != null
                && constructions.get(d.ordinal()) == Construction.DOOR) {
            p.setSection(newSection);
            newSection.setVisited(true);
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
    private Section bordererSection(Section s, Direction d) {
        int curLine = s.getCoordinate().getRow();
        int curColumn = s.getCoordinate().getColumn();
        switch (d) {
            case UP:
                return ((curLine > 0) ? this.getSection()[curLine - 1][curColumn] : null);
            case RIGHT:
                return ((curColumn < (this.getWidth() - 1)) ? this.getSection()[curLine][curColumn + 1] : null);
            case DOWN:
                return ((curLine < (this.getHeight() - 1)) ? this.getSection()[curLine + 1][curColumn] : null);
            case LEFT:
                return ((curColumn > 0) ? this.getSection()[curLine][curColumn - 1] : null);
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
    private boolean constructionVisibility(Section s, Direction d) {
        int curLine = s.getCoordinate().getRow();
        int curColumn = s.getCoordinate().getColumn();

        switch (d) {
            case UP:
                return (curLine > 0 && (s.isVisited()
                        || this.bordererSection(s, Direction.UP).isVisited()));
            case RIGHT:
                return (curColumn < (this.getWidth() - 1) && (s.isVisited()
                        || this.bordererSection(s, Direction.RIGHT).isVisited()));
            case DOWN:
                return (curLine < (this.getHeight() - 1) && (s.isVisited()
                        || this.bordererSection(s, Direction.DOWN).isVisited()));
            case LEFT:
                return (curColumn > 0 && (s.isVisited()
                        || this.bordererSection(s, Direction.LEFT).isVisited()));
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
    private String strConstruction(Section s, Direction d) {
        switch (d) {
            case UP:
            case DOWN:
                return ((s.getConstructions().get(d.ordinal()) == Construction.DOOR
                        && this.constructionVisibility(s, d)) ? "*" : "-");

            case RIGHT:
            case LEFT:
                return ((s.getConstructions().get(d.ordinal()) == Construction.DOOR
                        && this.constructionVisibility(s, d)) ? "*" : "|");

            default:
                return "\\";
        }
    }

    /**
     * Fornece o texto correspondente aos interiores e construções verticais de uma
     * linha de Setores específica.
     *
     * @param p    uma ArrayList contendo os dois Jogadores existentes no jogo.
     * @param sRow um array contendo a linha de Setores a qual deseja-se visualizar.
     * @return uma String correspondente à linha se Setores solicitada no formato
     *         | Setor Construção ... Construção Setor |
     * @see inconsistencia em caso de Construções inconsistentes horizontalmente,
     *      prevalecerá a Construção DIREITA do Setor ESQUERDO em detrimento da
     *      Construção ESQUERDA do Setor DIREITO.
     */
    private String strBoardVConstructions(ArrayList<Player> p, Section[] sRow) {
        String res = new String();

        res += "|";
        for (int i = 0; i < this.getWidth() - 1; i++) {
            res += strSectionBody(p, sRow[i]) + strConstruction(sRow[i], Direction.RIGHT);
        }
        res += strSectionBody(p, sRow[this.getWidth() - 1]) + "|";

        return res;
    }

    /**
     * Fornece o texto correspondente às construções horizontais inferiores de uma
     * linha de Setores específica.
     *
     * @param sRow um array contendo a linha de Setores cujas bases desejam-se
     *             visualizar.
     * @return uma String correspondente à linha de construções inferiores
     *         solicitada no formato |-Construção-|...|-Construção-|
     * @see inconsistencia em caso de Construções inconsistentes verticalmente,
     *      prevalecerá a Construção INFERIOR do Setor SUPERIOR em detrimento da
     *      Construção SUPERIOR do Setor INFERIOR.
     */
    private String strBoardHConstructions(Section[] sRow) {
        // String que armazenará o retorno
        String res = new String();

        // Texto correspondente aos Setores, inserido Setor por Setor
        for (int i = 0; i < this.getWidth(); i++) {
            res += "|-" + strConstruction(sRow[i], Direction.DOWN) + "-";
        }
        res += "|";

        return res;
    }

    /**
     * Fornece o texto correspondente ao zoom das construções horizontais de um
     * Setor específico
     *
     * @param s um Setor cujas construções horizontais deseja-se visualizar.
     * @return uma String correspondente ao zoom das construções solicitadas no
     *         formato |------Construção------|.
     */
    private String strZoomHConstruction(Section s, Direction d) {
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
    private String strZoomVConstructions(Section s) {
        return strConstruction(s, Direction.LEFT) + "             " + strConstruction(s, Direction.RIGHT);
    }

    /**
     * Fornece o texto correspondente ao conjunto de Inimigos de um Setor
     * específico.
     *
     * @param s o Setor cujos Inimigos desejam-se visualizar.
     * @return uma String correspondente aos Inimigos solicitados no formato
     *         | ATK/DEF ATK/DEF ATK/DEF |
     */
    private String strEnemiesAttrs(Section s) {
        // String que armazenará o retorno
        String res = new String();
        boolean sIsHidden = (s.getType() == SectionType.HIDDEN);

        // Texto correspondente aos Inimigos, inserido Inimigo a Inimigo
        res += "| ";
        for (int i = 0; i < 3; i++) {
            Enemy e = s.getInimigo(i);
            boolean eIsNull = (e == null);
            res += (eIsNull || sIsHidden) ? "    " : e.getAtk() + "/" + e.getDef() + " ";
        }
        res += "|";

        return res;
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
    private String strPlayersName(ArrayList<Player> p, int i) {
        // Variáveis auxiliares para legibilidade
        Player p1 = p.get(0);
        Player p2 = p.get(1);
        boolean arePlayersTogether = (p1.getSection() == p2.getSection());
        return "|   " + (arePlayersTogether ? "P1    P2" : "P" + i + "      ") + "  |";
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
    private String strZoomPlayersAttrs(ArrayList<Player> p, int i) {
        // Variáveis auxiliares para legibilidade
        Player p1 = p.get(0);
        Player p2 = p.get(1);
        boolean arePlayersTogether = (p1.getSection() == p2.getSection());
        String pAttr[] = {
                (p1.getAtk() + "/" + sizedString(Integer.toString(p1.getDef()), 3)),
                (p2.getAtk() + "/" + sizedString(Integer.toString(p2.getDef()), 3))
        };

        return "|  " + (arePlayersTogether ? pAttr[0] + " " + pAttr[1] : pAttr[i - 1] + "      ") + "|";
    }

    /**
     * Fornece o texto correspondente ao cabeçalho do Tabuleiro
     *
     * @return uma String correspondente o cabeçalho do Tabuleiro
     */
    private String strBoardHeader() {
        String res = new String();
        res += "   ";
        for (int i = 1; i <= this.getWidth(); i++) {
            if (i <= 10) {
                res += "   " + i;
            } else {
                res += "  " + i;
            }
        }
        res += "\n";
        res += "    |";
        for (int i = 1; i <= this.getWidth(); i++) {
            res += "---|";
        }
        res += "\n";
        return res;
    }

    /**
     * Fornece o texto correspondente às Coordenadas de um Jogador específico.
     *
     * @param p o Jogador cujas Coordenadas desejam-se visualizar.
     * @return uma String correspondente à Coordenada solicitada no formado Setor
     *         [X, Y].
     */
    private String strPlayerCoordinates(Player p) {
        return "Setor [" + p.getSection().getCoordinate().getRow() + "," + p.getSection().getCoordinate().getColumn()
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
    public String strTabuleiro(ArrayList<Player> p) {
        // String que armazenará o retorno
        String res = new String();
        // Variáveis auxiliares para legibilidade
        Player p1 = p.get(0);
        Player p2 = p.get(1);
        Section sp1 = p1.getSection();
        Section sp2 = p2.getSection();
        // Coordenadas dos players no zoom
        String zoomPC = strPlayerCoordinates(p1) + "       " + strPlayerCoordinates(p2);
        // Construções horizontais superiores no zoom
        String zoomHCT = strZoomHConstruction(sp1, Direction.UP) + "   " + strZoomHConstruction(sp2, Direction.UP);
        // Construções horizontais inferiores no zoom
        String zoomHCB = strZoomHConstruction(sp1, Direction.DOWN) + "   "
                + strZoomHConstruction(sp2, Direction.DOWN);
        // Construções verticais superiores no zoom
        String zoomVC = strZoomVConstructions(sp1) + "   " + strZoomVConstructions(sp2);
        // Inimigos no zoom
        String zoomEA = strEnemiesAttrs(sp1) + "   " + strEnemiesAttrs(sp2);
        // Atributos ATK/DEF dos players no zoom
        String zoomPA = strZoomPlayersAttrs(p, 1) + "   " + strZoomPlayersAttrs(p, 2);
        // Nome dos players no zoom
        String zoomPN = strPlayersName(p, 1) + "   " + strPlayersName(p, 2);
        String sp1Type = sizedString(sp1.getType().name().toLowerCase(), 7);
        String sp2Type = sizedString(sp2.getType().name().toLowerCase(), 7);

        // Texto correspondente ao tabuleiro, inserido linha a linha até a quinta linha
        // do tabuleiro
        res += "-----------------------------\n";
        res += "|   Antivírus por um dia    |\n";
        res += "-----------------------------\n";
        res += strBoardHeader();
        res += "1   " + strBoardVConstructions(p, section[0]) + "       " + zoomPC + "\n";
        res += "    " + strBoardHConstructions(section[0]) + "       " + sp1Type + "           " + sp2Type + "\n";
        res += "2   " + strBoardVConstructions(p, section[1]) + "     " + zoomHCT + "\n";
        res += "    " + strBoardHConstructions(section[1]) + "     " + zoomEA + "\n";
        res += "3   " + strBoardVConstructions(p, section[2]) + "     |             |   |             |\n";
        res += "    " + strBoardHConstructions(section[2]) + "     " + zoomVC + "\n";
        res += "4   " + strBoardVConstructions(p, section[3]) + "     " + zoomPN + "\n";
        res += "    " + strBoardHConstructions(section[3]) + "     " + zoomPA + "\n";
        res += "5   " + strBoardVConstructions(p, section[4]) + "     " + zoomHCB + "\n";
        res += "    " + strBoardHConstructions(section[4]) + "\n";
        // Linhas posteriores a quinta linha do tabuleiro
        for (int i = 5; i < this.getHeight(); i++) {
            res += sizedString(Integer.toString(i + 1), 2) + "  " + strBoardVConstructions(p, section[i]) + "\n";
            res += "    " + strBoardHConstructions(section[i]) + "\n";
        }

        return res;
    }

}
