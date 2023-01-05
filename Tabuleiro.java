import java.util.Random;

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

        setores[ran.ints(0, 5).findFirst().getAsInt()][ran.ints(0, 5).findFirst().getAsInt()].setFonte(true);

        gerarPortas();
    }

    public Setor[][] getSetores() {
        return setores;
    }

    public void setSetores(Setor[][] setores) {
        this.setores = setores;
    }

    private void gerarPortas() {
        acharVirus(new Coordenada(linhas/2, colunas/2), new Coordenada(linhas/2, colunas/2), false);
    }

    private void acharVirus(Coordenada coord, Coordenada coordAntiga, boolean virusAchado){
        if(virusAchado == true)
            return;
        if(!coordenadaEhValida(coord))
            return;
        if(setores[coord.getX()][coord.getY()].isFonte()){
            virusAchado = true;
            return;
        }

        Coordenada nova_coord = coord;

        /* (x+1, y) */
        nova_coord.setX(nova_coord.getX() + 1);
        if(nova_coord != coordAntiga){
            /* Abrir portas de nova_coord para a coordAntiga */
            abrirPorta(nova_coord, coord);
            acharVirus(nova_coord, coord, virusAchado);
        }

        /* (x-1, y) */
        nova_coord.setX(nova_coord.getX() -2);
        if(nova_coord != coordAntiga){
            /* Abrir portas de nova_coord para a coordAntiga */
            abrirPorta(nova_coord, coord);
            acharVirus(nova_coord, coord, virusAchado);
        }

        nova_coord.setX(nova_coord.getX() + 1);

        /* (x, y+1) */
        nova_coord.setY(nova_coord.getY() + 1);
        if(nova_coord != coordAntiga){
            /* Abrir portas de nova_coord para a coordAntiga */
            abrirPorta(nova_coord, coord);
            acharVirus(nova_coord, coord, virusAchado);
        }

        /* (x, y-1) */
        nova_coord.setY(nova_coord.getY() - 2);
        if(nova_coord != coordAntiga){
            /* Abrir portas de nova_coord para a coordAntiga */
            abrirPorta(nova_coord, coord);
            acharVirus(nova_coord, coord, virusAchado);
        }

        return;
    }

    private boolean coordenadaEhValida(Coordenada coord){
        if(coord.getX() >= linhas || coord.getX() < 0)
            return false;
        if(coord.getY() >= colunas || coord.getY() < 0)
            return false;

        return true;
    }

    private void abrirPorta(Coordenada nova_coord, Coordenada antiga_coord){
        /* 
         * Index das portas
         * 0 = Cima
         * 1 = Direita
         * 2 = Baixo
         * 3 = Esquerda
         */

        /* Abri porta a cima */
        if((nova_coord.getX() - antiga_coord.getX()) == -1)
            setores[antiga_coord.getX()][antiga_coord.getY()].setPorta(3, true);
        else if((nova_coord.getX() - antiga_coord.getX()) == 1)
            setores[antiga_coord.getX()][antiga_coord.getY()].setPorta(1, true);
        else if((nova_coord.getY() - antiga_coord.getY()) == -1)
            setores[antiga_coord.getX()][antiga_coord.getY()].setPorta(0, true);
        else if((nova_coord.getY() - antiga_coord.getY()) == 1)
            setores[antiga_coord.getX()][antiga_coord.getY()].setPorta(2, true);
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
