import java.util.*;

public class Jogo{
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        PartidaVirus match = new PartidaVirus(new Tabuleiro(5,5), true);
        Jogador p1 = new Jogador(2,6,0,null);
        Jogador p2 = new Suporte(1,7,0,null);
        Turno turn = Turno.PLAYER1;

        match.startGame(p1, p2);

        while(match.checkGameConditions()) {
            match.displayBoard();
            switch(turn) {
                case PLAYER1:
                    match.chamarTurno(match.getJogadores().get(0), input);
                    turn = Turno.PLAYER2;
                    break;
                case PLAYER2:
                    match.chamarTurno(match.getJogadores().get(1), input);
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