import java.util.*;

public class Jogo{
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        ArrayList<Jogador> jogadoresTeste = new ArrayList<Jogador>(2);
        PartidaVirus partida = new PartidaVirus(jogadoresTeste, new Tabuleiro(5,5), true);
        Jogador p1 = new Jogador(partida.getTabuleiro().getSetor(2, 2));
        Jogador p2 = new Suporte(partida.getTabuleiro().getSetor(2, 2));
        int cont = 0;

        p1.getSetor().setVisitado(true);
        jogadoresTeste.add(p1);
        jogadoresTeste.add(p2);

        while((jogadoresTeste.get(0).isVivo()) && (partida.getCiclos() < 2)) {
            switch(cont) {
                case 0:
                    System.out.println(partida.getTabuleiro().strTabuleiro(jogadoresTeste));
                    partida.displayShift(jogadoresTeste.get(0), input);
                    cont++;
                    break;
                case 1:
                    System.out.println(partida.getTabuleiro().strTabuleiro(jogadoresTeste));
                    partida.displayShift(jogadoresTeste.get(1), input);
                    cont++;
                    break;
                case 2:
                    System.out.println("-----------------------------");
                    System.out.println("| Turno dos inimigos de P1  |");
                    System.out.println("-----------------------------");
                    for(Inimigo inimigo: p1.getSetor().getInimigos()) {
                        partida.displayShift(inimigo, p1);
                    }
                    cont++;
                    break;
                case 3:
                    System.out.println("-----------------------------");
                    System.out.println("| Turno dos inimigos de P2  |");
                    System.out.println("-----------------------------");
                    for(Inimigo inimigo: p2.getSetor().getInimigos()) {
                        partida.displayShift(inimigo, p2);
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

