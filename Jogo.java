import java.util.*;

public class Jogo{
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        ArrayList<Jogador> jogadoresTeste = new ArrayList<Jogador>(2);
        ArrayList<ArrayList<Inimigo>> inimigos = new ArrayList<>(2);
        PartidaVirus partida = new PartidaVirus(jogadoresTeste);
        Jogador p1 = new Jogador(true);
        Jogador p2 = new Suporte(true);
        int cont = 0;

        jogadoresTeste.add(p1);
        jogadoresTeste.add(p2);

        while((jogadoresTeste.get(0).isVivo()) && (partida.getCiclos() < 2)) {
            switch(cont) {
                case 0:
                    partida.chamarTurno(jogadoresTeste.get(0));
                    cont++;
                    break;
                case 1:
                    partida.chamarTurno(jogadoresTeste.get(1));
                    cont++;
                    break;
                case 2:
                    for(Inimigo inimigo: inimigos.get(0)) {
                        partida.chamarTurno(inimigo);
                    }
                    cont++;
                    break;
                case 3:
                    for(Inimigo inimigo: inimigos.get(1)) {
                        partida.chamarTurno(inimigo);
                    }
                    cont = 0;
                    partida.incrementaCiclo();
                    break;
                default:
                    break;
            }
        }

        System.out.printf("#################################\n");
        System.out.printf("### You have won the game!!! ###\n");
        System.out.printf("#################################\n");

        input.close();
    }
}
