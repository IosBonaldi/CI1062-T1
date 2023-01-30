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
    
    private void gerarFonte(){
        Random rand = new Random();
        Coordenada coordVirus = new Coordenada(rand.nextInt(largura), rand.nextInt(altura));

        /* Gera novas coordenadas enquanto a coordenada sorteada coincidir com o meio do tabuleiro */
        while(coordVirus.getLinha() == largura/2 && coordVirus.getColuna() == altura/2){
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
        acharVirus(new Coordenada(altura/2, largura/2), setoresVisitados, false);
        abrirCaminho(setoresVisitados);
    }

    /**
     * 
     * @param coord Coordenada inicial do jogador
     * @param setoresVisitados Lista de setores ja visitados
     * @param virusAchado Boolean para saber se o virus da foi achado
     * @return
    */
    private boolean acharVirus(Coordenada coord, ArrayList<Coordenada> setoresVisitados, boolean virusAchado){
        if(virusAchado == true)
            return virusAchado;
        if(setores[coord.getLinha()][coord.getColuna()].isFonte()){
            setoresVisitados.add(coord); 
            return true;
        }

        setoresVisitados.add(coord);

        ArrayList<Coordenada> coordAleatorias = coordenadasAleatoriasValidas(coord, setoresVisitados);

        /* Se coordAleatorias esta vazio, eh porque nenhum caminho valido foi encontrado */
        if(!coordAleatorias.isEmpty()){
            /* Chama o backtracking paras os movimentos possiveis */
            for(int i = 0; i < coordAleatorias.size() && !virusAchado; i++){
                    virusAchado = acharVirus(coordAleatorias.get(i), setoresVisitados, virusAchado);

                    /* Empilha a volta somente quando o virus nao foi achado */
                    if(!virusAchado)
                        setoresVisitados.add(coord);
            }
        }

        return virusAchado;
    }

    /**
     * Retorna uma lista de todas as possiveis validas novas coordenadas a partir da coordenada recebida
     * setores que ja foram visitado nao sao considerados validos
     * 
     * @param coord Coordenada atual
     * @param setoresVisitados 
     * @return Lista com possiveis novas coordenadas validas
     */
    private ArrayList<Coordenada> coordenadasAleatoriasValidas(Coordenada coord, ArrayList<Coordenada> setoresVisitados){
        ArrayList<Coordenada> coordAleatorias = new ArrayList<Coordenada>();
        Random rand = new Random();

        Coordenada coord1 = new Coordenada(coord.getLinha() + 1, coord.getColuna());
        Coordenada coord2 = new Coordenada(coord.getLinha() - 1, coord.getColuna());
        Coordenada coord3 = new Coordenada(coord.getLinha(), coord.getColuna() + 1);
        Coordenada coord4 = new Coordenada(coord.getLinha(), coord.getColuna() - 1);

        if(coordenadaEhValida(coord1) && !setorFoiVisitado(coord1, setoresVisitados)) coordAleatorias.add(coord1);
        if(coordenadaEhValida(coord2) && !setorFoiVisitado(coord2, setoresVisitados)) coordAleatorias.add(coord2);
        if(coordenadaEhValida(coord3) && !setorFoiVisitado(coord3, setoresVisitados)) coordAleatorias.add(coord3);
        if(coordenadaEhValida(coord4) && !setorFoiVisitado(coord4, setoresVisitados)) coordAleatorias.add(coord4);

        /* Embaralha as coordenadas */
        if(coordAleatorias.size() > 1){
            for(int i = 0; i < coordAleatorias.size(); i++){
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
     * @param coord
     * @param setoresVisitados
     * @return
     */
    private boolean setorFoiVisitado(Coordenada coord, ArrayList<Coordenada> setoresVisitados){
        for(int i = 0; i < setoresVisitados.size(); i++){
            Coordenada aux = setoresVisitados.get(i);
            if((aux.getLinha() == coord.getLinha()) && ( aux.getColuna() == coord.getColuna())) 
                return true;
        }
        return false;
    }

    /**
     * Retorna TRUE se a coordenada eh valida baseando se nos limites do Tabuleiro
     * do contrario retorna FALSE 
     * @param coord
     * @return
     */
    private boolean coordenadaEhValida(Coordenada coord){
        if(coord.getLinha() >= altura || coord.getLinha() < 0)
            return false;
        if(coord.getColuna() >= largura || coord.getColuna() < 0)
            return false;

        return true;
    }

    /**
     * Abre o caminho em setores[][] baseando-se na lista de coordenadas recebida
     * @param setoresVisitados
     */
    private void abrirCaminho(ArrayList<Coordenada> setoresVisitados){
        for(int i = 0; i < setoresVisitados.size() - 1; i++){
            Coordenada coordSetorAtual = setoresVisitados.get(i);
            Coordenada coordSetorSeguinte = setoresVisitados.get(i + 1);

            Direcao dir = calcularDirecao(coordSetorAtual, coordSetorSeguinte);

            if(dir == null)
                throw new NullPointerException("Nenhuma direcao valida encontrada!");

            switch(dir){
                case CIMA:
                    abrirPorta(setores[coordSetorAtual.getLinha()][coordSetorAtual.getColuna()], Direcao.CIMA);
                    abrirPorta(setores[coordSetorSeguinte.getLinha()][coordSetorSeguinte.getColuna()], Direcao.BAIXO);
                    break;
                case DIREITA:
                    abrirPorta(setores[coordSetorAtual.getLinha()][coordSetorAtual.getColuna()], Direcao.DIREITA);
                    abrirPorta(setores[coordSetorSeguinte.getLinha()][coordSetorSeguinte.getColuna()], Direcao.ESQUERDA);
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
    private void abrirPorta(Setor setor, Direcao direcao){
        setor.setPorta(direcao);
    }

    /**
     * Retorna a direcao da origem para o destino
     * 
     * @param origem
     * @param destino
     * @return
     */
    private Direcao calcularDirecao(Coordenada origem, Coordenada destino){
        if((destino.getLinha() - origem.getLinha()) == 1)
            return Direcao.DIREITA;
        else if((destino.getLinha() - origem.getLinha()) == -1)
            return Direcao.ESQUERDA;
        else if((destino.getColuna() - origem.getColuna()) == 1)
            return Direcao.BAIXO;
        else if((destino.getColuna() - origem.getColuna()) == -1)
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
        for (int i = 1; i < 3; i++) {
            r += s.getInimigo(i) == null ? "   " : s.getInimigo(i).getAtk() + "/" + s.getInimigo(i).getDef();
            r += " ";
        }
        r += s.getInimigo(3) == null ? "   " : s.getInimigo(3).getAtk() + "/" + s.getInimigo(3).getDef();

        return r;
    }

    /**
     * Retorna o texto correspondente aos atributos ATK/DEF de um Jogador
     * específico.
     *
     * @param p lista de Jogadores cujos atributos desejam-se visualizar.
     * @param i número do mini-setor a ser impresso.
     */
    public String strJogadores(ArrayList<Jogador> p, int i) {
        // Variáveis auxiliares para legibilidade
        Jogador p1 = p.get(0);
        Jogador p2 = p.get(1);
        if (p1.getSetor() == p2.getSetor()) {
            return "P1    P2";
        } else {
            if (i == 1) {
                return "P1      ";
            } else if (i == 2) {
                return "P2      ";
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
    public String strATKDEF(ArrayList<Jogador> p, int i) {
        // Variáveis auxiliares para legibilidade
        Jogador p1 = p.get(0);
        Jogador p2 = p.get(1);
        if (p1.getSetor() == p2.getSetor()) {
            return p1.getAtk() + "/" + p1.getDef() + "   " + p2.getAtk() + "/" + p2.getDef();
        } else {
            if (i == 1) {
                return p1.getAtk() + "/" + p1.getDef() + "      ";
            } else if (i == 2) {
                return p2.getAtk() + "/" + p2.getDef() + "      ";
            }
        }
        return "";
    }

    /**
     * Retorna o texto correspondente às Coordenadas de um Jogador específico.
     *
     * @param p o Jogador cujas Coordenadas desejam-se visualizar.
     */
    public String strCoordenada(Jogador p) {
        return p.getSetor().getCoordenada().getLinha() + "," + p.getSetor().getCoordenada().getColuna();
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
        r += "    " + strLinhaSetores(p, setores[3]) + "     |  " + strJogadores(p, 1) + "   |   |  "
                + strJogadores(p, 2) + "   |\n";
        r += "    " + strBaseSetores(setores[3]) + "     |  " + strATKDEF(p, 1) + "  |   |  " + strATKDEF(p, 2)
                + "  |\n";
        r += "    " + strLinhaSetores(p, setores[4]) + "     |------*------|   |------*------|\n";
        r += "    |---|---|---|---|---|\n";

        return r;
    }

}
