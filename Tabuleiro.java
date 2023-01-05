import java.util.Random;

public class Tabuleiro {
    private Setor[][] setores;
    int linhas;
    int colunas;

    public Tabuleiro(Setor[][] setores) {
        this.setores = setores;
    }

    public Setor[][] getSetores() {
        return setores;
    }

    public void setSetores(Setor[][] setores) {
        this.setores = setores;
    }

    private void gerarPortas() {
    }

    private void acharVirus(Coordenada coord, Coordenada coord_antiga, Boolean virus_achado){
        if(virus_achado == true)
            return;
        if(!coordenadaEhValida(coord))
            return;
        if(setores[coord.getX()][coord.getY()].isFonte()){
            virus_achado = true;
            return;
        }

        Coordenada nova_coord = coord;

        /* (x+1, y) */
        nova_coord.setX(nova_coord.getX() + 1);
        if(nova_coord != coord_antiga){
            /* Abrir portas de nova_coord para a coord_antiga */
            acharVirus(nova_coord, coord, virus_achado);
        }

        /* (x-1, y) */
        nova_coord.setX(nova_coord.getX() -2);
        if(nova_coord != coord_antiga)
            acharVirus(nova_coord, coord, virus_achado);

        nova_coord.setX(nova_coord.getX() + 1);

        /* (x, y+1) */
        nova_coord.setY(nova_coord.getY() + 1);
        if(nova_coord != coord_antiga)
            acharVirus(nova_coord, coord, virus_achado);

        /* (x, y-1) */
        nova_coord.setY(nova_coord.getY() - 2);
        if(nova_coord != coord_antiga)
            acharVirus(nova_coord, coord, virus_achado);
    }

    private Boolean coordenadaEhValida(Coordenada coord){
        if(coord.getX() >= linhas || coord.getX() < 0)
            return false;
        if(coord.getY() >= colunas || coord.getY() < 0)
            return false;

        return true;
    }

    public Setor buscaSetor(int x, int y) {
        return null;
    }
}
