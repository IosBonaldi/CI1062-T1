import java.util.*;

public class Jogo{
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        ArrayList<Jogador> jogadoresTeste = new ArrayList<Jogador>(2);
        ArrayList<ArrayList<Inimigo>> inimigos = new ArrayList<>(2);
        PartidaVirus partida = new PartidaVirus(jogadoresTeste, new Tabuleiro(5,5), true);
        Jogador p1 = new Jogador(2,6,0,null);
        Jogador p2 = new Suporte(1,7,0,null);
        p1.setSetor(partida.getTabuleiro().getSetor(2, 2));
        p2.setSetor(partida.getTabuleiro().getSetor(2, 2));
        p1.getSetor().setVisitado(true);
        int cont = 0;

        jogadoresTeste.add(p1);
        jogadoresTeste.add(p2);

        while((jogadoresTeste.get(0).isVivo()) && (partida.getCiclos() < 2)) {
            System.out.println(partida.getTabuleiro().strTabuleiro(jogadoresTeste));
            switch(cont) {
                case 0:
                    partida.chamarTurno(jogadoresTeste.get(0), input);
                    cont++;
                    break;
                case 1:
                    partida.chamarTurno(jogadoresTeste.get(1), input);
                    cont++;
                    break;
                case 2:
                    for(Inimigo inimigo: p1.getSetor().getInimigos()) {
                        partida.chamarTurno(inimigo, p1);
                    }
                    cont++;
                    break;
                case 3:
                    for(Inimigo inimigo: p2.getSetor().getInimigos()) {
                        partida.chamarTurno(inimigo, p2);
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