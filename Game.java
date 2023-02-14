
/*
 * Paradigmas de Programação (CI1062) - Antivírus por um dia
 * Acadêmicos:  Izalorran Oliveira Santos Bonaldi - GRR20210582
 *              Mateus Oliveira Silva - GRR20205669
 *              Matheus Luiz de Oliveira Souza - GRR20210259
 *              Yuri Junqueira Tobias - GRR20211767
 * Docente: Rachel Carlos Duque Reis
 * Data: 15/02/23
 */
import java.util.*;

public class Game {
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        LogHandler log = new LogHandler("./gameLog.txt");

        if (!log.logFileExist())
            log.createLogFile();

        Match match = new Match(5, 5);

        match.startGame();
        Player p1 = match.getPlayers().get(0);
        Player p2 = match.getPlayers().get(1);
        Turn turn = Turn.PLAYER1;

        while (match.checkGameConditions()) {
            switch (turn) {
                case PLAYER1:
                    match.callTurn(p1, input);
                    turn = Turn.PLAYER2;
                    break;
                case PLAYER2:
                    match.callTurn(p2, input);
                    turn = Turn.ENEMIES1;
                    break;
                case ENEMIES1:
                    if (p1.getSection().countEnemiesAlive() != 0) {
                        System.out.println("-----------------------------");
                        System.out.println("|      P1 enemy turn        |");
                        System.out.println("-----------------------------");
                        for (Enemy e : p1.getSection().getEnemies()) {
                            if (e.isAlive())
                                e.attack(p1);
                        }
                    }
                    turn = Turn.ENEMIES2;
                    break;
                case ENEMIES2:
                    if (p2.getSection().countEnemiesAlive() != 0) {
                        System.out.println("-----------------------------");
                        System.out.println("|      P2 enemy turn        |");
                        System.out.println("-----------------------------");
                        for (Enemy e : p2.getSection().getEnemies()) {
                            if (e.isAlive())
                                e.attack(p2);
                        }
                    }
                    turn = Turn.PLAYER1;
                    match.increaseCycles();
                    break;
                default:
                    break;
            }
        }

        if (p1.section.isSource() || !p1.isAlive())
            log.logFileManipulation(p1.score.getPoints());
        else
            log.logFileManipulation(p2.score.getPoints());

        match.displayGameResult();
        match.displayHighestScores(input, log);

        input.close();
    }
}
