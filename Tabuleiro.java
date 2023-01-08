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

    private void acharVirus(Coordenada coord, ArrayList<Coordenada> setoresVisitados, boolean virusAchado){
        if(virusAchado == true)
            return;
        if(!coordenadaEhValida(coord))
            return;
        if(setores[coord.getX()][coord.getY()].isFonte()){
            virusAchado = true;
            setoresVisitados.add(coord);
            return;
        }
        setoresVisitados.add(coord);

        Coordenada novaCoord = new Coordenada(coord.getX(), coord.getY());
        ArrayList<Coordenada> coordenadasAleatorias = coordenadasAleatorias(coord);

        /* (x+1, y) */
        novaCoord.setX(novaCoord.getX() + 1);
        if(!setorFoiVisitado(novaCoord, setoresVisitados))
            acharVirus(novaCoord, setoresVisitados, virusAchado);

        /* (x-1, y) */
        novaCoord.setX(novaCoord.getX() -2);
        if(!setorFoiVisitado(novaCoord, setoresVisitados))
            acharVirus(novaCoord, setoresVisitados, virusAchado);

        novaCoord.setX(novaCoord.getX() + 1);

        /* (x, y+1) */
        novaCoord.setY(novaCoord.getY() + 1);
        if(!setorFoiVisitado(novaCoord, setoresVisitados))
            acharVirus(novaCoord, setoresVisitados, virusAchado);

        /* (x, y-1) */
        novaCoord.setY(novaCoord.getY() - 2);
        if(!setorFoiVisitado(novaCoord, setoresVisitados))
            acharVirus(novaCoord, setoresVisitados, virusAchado);

        return;
    }

    private ArrayList<Coordenada> coordenadasAleatorias(Coordenada coord){
        ArrayList<Coordenada> coordAleatorias = new ArrayList<Coordenada>();
        Random rand = new Random();

        coordAleatorias.add(new Coordenada(coord.getX() + 1, coord.getY()));
        coordAleatorias.add(new Coordenada(coord.getX() - 1, coord.getY()));
        coordAleatorias.add(new Coordenada(coord.getX(), coord.getY() + 1));
        coordAleatorias.add(new Coordenada(coord.getX(), coord.getY() - 1));

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
            //if(setoresVisitados.get(i).equals(coord))
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
