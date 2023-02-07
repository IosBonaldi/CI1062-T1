import java.util.*;

public class Jogo{
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        ArrayList<Jogador> players = new ArrayList<Jogador>(2);
        PartidaVirus match = new PartidaVirus(players, new Tabuleiro(5,5), true);
        Jogador p1 = new Jogador(2,6,0,null);
        Jogador p2 = new Suporte(1,7,0,null);
        Turno turn = Turno.PLAYER1;

        p1.setSetor(match.getTabuleiro().getSetor(2, 2));
        p2.setSetor(match.getTabuleiro().getSetor(2, 2));
        p1.getSetor().setVisitado(true);
        players.add(p1);
        players.add(p2);

        while(match.checkGameConditions()) {
            match.displayBoard();
            switch(turn) {
                case PLAYER1:
                    match.chamarTurno(players.get(0), input);
                    turn = Turno.PLAYER2;
                    break;
                case PLAYER2:
                    match.chamarTurno(players.get(1), input);
                    turn = Turno.ENEMIES1;
                    break;
                case ENEMIES1:
                    for(Inimigo inimigo: p1.getSetor().getInimigos()) {
                        match.chamarTurno(inimigo, p1);
                    }
                    turn = Turno.ENEMIES2;
                    break;
                case ENEMIES2:
                    for(Inimigo inimigo: p2.getSetor().getInimigos()) {
                        match.chamarTurno(inimigo, p2);
                    }
                    turn = Turno.PLAYER1;
                    match.incrementaCiclo();
                    break;
                default:
                    break;
            }
        }

        match.displayGameResult();

        input.close();
    }
}