import java.util.ArrayList;
import java.util.Random;

public class Tabuleiro {
    private Setor[][] setores;
    int linhas;
    int colunas;

    public Tabuleiro(int linhas, int colunas){
    }

    public Setor[][] getSetores() {
        return setores;
    }

    public void setSetores(Setor[][] setores) {
        this.setores = setores;
    }

    private void gerarPortas() {
        ArrayList<Coordenada> setoresVisitados = new ArrayList<Coordenada>();
        acharVirus(new Coordenada(linhas/2, colunas/2), setoresVisitados, false);
        abrirCaminho(setoresVisitados);
        System.out.println();
    }

    /* Acha um possivel caminha para o virus */
    /* Assume que a coordenada inicial eh valida */
    private boolean acharVirus(Coordenada coord, ArrayList<Coordenada> setoresVisitados, boolean virusAchado){
        if(virusAchado == true)
            return virusAchado;
        if(setores[coord.getX()][coord.getY()].isFonte()){
            setoresVisitados.add(coord); return true;
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

    /* Retorna uma lista possiveis novas coordenadas validas a partir da coordenada recebida */
    private ArrayList<Coordenada> coordenadasAleatoriasValidas(Coordenada coord, ArrayList<Coordenada> setoresVisitados){
        ArrayList<Coordenada> coordAleatorias = new ArrayList<Coordenada>();
        Random rand = new Random();

        Coordenada coord1 = new Coordenada(coord.getX() + 1, coord.getY());
        Coordenada coord2 = new Coordenada(coord.getX() - 1, coord.getY());
        Coordenada coord3 = new Coordenada(coord.getX(), coord.getY() + 1);
        Coordenada coord4 = new Coordenada(coord.getX(), coord.getY() - 1);

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

    private boolean setorFoiVisitado(Coordenada coord, ArrayList<Coordenada> setoresVisitados){
        for(int i = 0; i < setoresVisitados.size(); i++){
            Coordenada aux = setoresVisitados.get(i);
            if((aux.getX() == coord.getX()) && ( aux.getY() == coord.getY())) 
                return true;
        }
        return false;
    }

    private boolean coordenadaEhValida(Coordenada coord){
        if(coord.getX() >= linhas || coord.getX() < 0)
            return false;
        if(coord.getY() >= colunas || coord.getY() < 0)
            return false;

        return true;
    }

    /* Abre o caminho em setores[][] baseando-se na lista de coordenadas recebida */
    private void abrirCaminho(ArrayList<Coordenada> setoresVisitados){
        for(int i = 0; i < setoresVisitados.size() - 1; i++){
            Coordenada coordSetorAtual = setoresVisitados.get(i);
            Coordenada coordSetorSeguinte = setoresVisitados.get(i + 1);

            Direcao dir = calcularDirecao(coordSetorAtual, coordSetorSeguinte);

            if(dir == null)
                throw new NullPointerException("Nenhuma direcao valida encontrada!");

            switch(calcularDirecao(coordSetorAtual, coordSetorSeguinte)){
                case CIMA:
                    abrirPorta(setores[coordSetorAtual.getX()][coordSetorAtual.getY()], Direcao.CIMA.ordinal());
                    break;
                case DIREITA:
                    abrirPorta(setores[coordSetorAtual.getX()][coordSetorAtual.getY()], Direcao.DIREITA.ordinal());
                    break;
                case BAIXO:
                    abrirPorta(setores[coordSetorAtual.getX()][coordSetorAtual.getY()], Direcao.BAIXO.ordinal());
                    break;
                case ESQUERDA:
                    abrirPorta(setores[coordSetorAtual.getX()][coordSetorAtual.getY()], Direcao.ESQUERDA.ordinal());
                    break;
            }
        }
    }

    private void abrirPorta(Setor setor, int portaIndex){
        setor.setPorta(portaIndex, true);
    }

    /* Retorna a direcao da origem para o destino */
    /* Caso nenhuma direcao valida for encontrada, retorna null */
    private Direcao calcularDirecao(Coordenada origem, Coordenada destino){
        if((destino.getX() - origem.getX()) == 1)
            return Direcao.DIREITA;
        else if((destino.getX() - origem.getX()) == -1)
            return Direcao.ESQUERDA;
        else if((destino.getY() - origem.getY()) == 1)
            return Direcao.BAIXO;
        else if((destino.getY() - origem.getY()) == -1)
            return Direcao.CIMA;

        return null;
    }

    public Setor buscaSetor(int x, int y) {
        return null;
    }

}
