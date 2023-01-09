import java.util.ArrayList;
import java.util.Random;
import java.util.zip.ZipError;

public class Tabuleiro {
    private Setor[][] setores;
    int linhas;
    int colunas;

    public Tabuleiro(int linhas, int colunas){
        this.linhas = linhas;
        this.colunas = colunas;

        this.setores = new Setor[linhas][colunas];

        Random ran = new Random();

        for(int i = 0; i < linhas; i++)
            for(int j = 0; j < colunas; j++)
                setores[i][j] = new Setor(SetorTipos.NORMAL, false);

        setores[4][4].setFonte(true);
        //setores[ran.ints(0, 5).findFirst().getAsInt()][ran.ints(0, 5).findFirst().getAsInt()].setFonte(true);

        gerarPortas();
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
        System.out.printf("Aaaa");
        //abrirPortas(setoresVisitados);
    }

    /* Acha um possivel caminha para o virus */
    private boolean acharVirus(Coordenada coord, ArrayList<Coordenada> setoresVisitados, boolean virusAchado){
        if(virusAchado == true)
            return false;
        if(!coordenadaEhValida(coord))
            return false;
        if(setores[coord.getX()][coord.getY()].isFonte()){
            setoresVisitados.add(coord);
            return true;
        }

        setoresVisitados.add(coord);

        ArrayList<Coordenada> coordAleatorias = coordenadasAleatorias(coord);

        /* Chama o backtracking paras os 4 movimentos possiveis */
        for(int i = 0; i < coordAleatorias.size() && !virusAchado; i++){
            if(!setorFoiVisitado(coordAleatorias.get(i), setoresVisitados))
                 virusAchado = acharVirus(coordAleatorias.get(i), setoresVisitados, virusAchado);
        }

        return virusAchado;
    }

    /* Retorna uma lista de 4 possiveis novas coordenadas a partir da coordenada recebida */
    /* Nao verifica se as coordenadas sao validas */
    private ArrayList<Coordenada> coordenadasAleatorias(Coordenada coord){
        ArrayList<Coordenada> coordAleatorias = new ArrayList<Coordenada>();
        Random rand = new Random();

        coordAleatorias.add(new Coordenada(coord.getX() + 1, coord.getY()));
        coordAleatorias.add(new Coordenada(coord.getX() - 1, coord.getY()));
        coordAleatorias.add(new Coordenada(coord.getX(), coord.getY() + 1));
        coordAleatorias.add(new Coordenada(coord.getX(), coord.getY() - 1));

        /* Embaralha as coordenadas */
        for(int i = 0; i < coordAleatorias.size(); i++){
            int randIndex = rand.nextInt(3);

            Coordenada auxAtual = coordAleatorias.get(i);
            Coordenada auxRand = coordAleatorias.get(randIndex);

            coordAleatorias.set(i, auxRand);
            coordAleatorias.set(randIndex, auxAtual);
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

    private void abrirPorta(Coordenada setoresVisitados){
        for(int i = 0; i < setoresVisitados.size(); i++){

        }
    }

    public Setor buscaSetor(int x, int y) {
        return null;
    }

    public void printaTabuleiro(){
        System.out.printf("\n");
        for(int i = 0; i < linhas; i++){
            System.out.printf("\n");
            System.out.printf("\n");
            /* Cima */
            for(int j = 0; j < colunas; j++){
                for(int k = 0; k < 5; k++)
                    System.out.printf(" ");
                if(setores[i][j].isAberta(0))
                    System.out.print("*");
                else 
                    System.out.print("#");
            }

            System.out.printf("\n");

            /* Lados */
            for(int j = 0; j < colunas; j++){
                for(int k = 0; k < 2; k++)
                    System.out.printf(" ");

                if(setores[i][j].isAberta(3))
                    System.out.print("*");
                else 
                    System.out.print("#");
                
                for(int k = 0; k < 2; k++)
                    System.out.printf(" ");

                if(setores[i][j].isAberta(1))
                    System.out.print("*");
                else 
                    System.out.print("#");
            }
            System.out.printf("\n");
            /* Baixo */
            for(int j = 0; j < colunas; j++){
                for(int k = 0; k < 5; k++)
                    System.out.printf(" ");
                if(setores[i][j].isAberta(2))
                    System.out.print("*");
                else 
                    System.out.print("#");
            }
        }
        System.out.printf("\n");
    }
}
