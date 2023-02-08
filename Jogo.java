import java.util.*;

public class Jogo {
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        LogHandler log = new LogHandler("./gameLog.txt");

        if(!log.logFileExist())
            log.createLogFile();

        PartidaVirus match = new PartidaVirus(5, 5);

        match.startGame();
        Jogador p1 = match.getJogadores().get(0);
        Jogador p2 = match.getJogadores().get(1);
        Turno turn = Turno.PLAYER1;

        while (match.checkGameConditions()) {
            match.displayBoard();
            switch (turn) {
                case PLAYER1:
                    match.displayShift(match.getJogadores().get(0), input);
                    turn = Turno.PLAYER2;
                    break;
                case PLAYER2:
                    match.displayShift(match.getJogadores().get(1), input);
                    turn = Turno.ENEMIES1;
                    break;
                case ENEMIES1:
                    System.out.println("-----------------------------");
                    System.out.println("| Turno dos inimigos de P1  |");
                    System.out.println("-----------------------------");
                    for (Inimigo inimigo : p1.getSetor().getInimigos()) {
                        match.displayShift(inimigo, p1);
                    }
                    turn = Turno.ENEMIES2;
                    break;
                case ENEMIES2:
                    System.out.println("-----------------------------");
                    System.out.println("| Turno dos inimigos de P2  |");
                    System.out.println("-----------------------------");
                    for (Inimigo inimigo : p2.getSetor().getInimigos()) {
                        match.displayShift(inimigo, p2);
                    }
                    turn = Turno.PLAYER1;
                    match.incrementaCiclo();
                    break;
                default:
                    break;
            }
        }

        if(p1.setor.isFonte())
            log.logFileManipulation(p1.pontuacao);
        else 
            log.logFileManipulation(p2.pontuacao);

        match.displayGameResult();

        input.close();
    }
}
