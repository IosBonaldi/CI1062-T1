import java.util.ArrayList;
import java.util.Scanner;

public class Match {
    /* Atributos */
    private ArrayList<Player> players;
    private Board board;
    private int cycles;

    /* Construtores */
    public Match(int height, int width) {
        this.setBoard(new Board(height, width));
        this.setCycles(0);
        this.setPlayers(new ArrayList<Player>());
    }

    /* Métodos get/set */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        if (0 > cycles || cycles < this.cycles)
            throw new IllegalArgumentException();
        this.cycles = cycles;
    }

    public void startGame() {
        int iniLine = this.getBoard().getHeight() / 2;
        int iniColumn = this.getBoard().getWidth() / 2;
        Section iniSection = this.getBoard().getSection(iniLine, iniColumn);
        iniSection.setVisited(true);
        getPlayers().add(new Player(iniSection));
        getPlayers().add(new Support(iniSection));
    }

    public boolean checkGameConditions() {
        if ((getPlayers().get(0).isAlive()) && (getCycles() < 25)
                && (!(getPlayers().get(0).getSection().isSource()) && !(getPlayers().get(1).getSection().isSource())))
            return true;
        return false;
    }

    public void displayGameResult() {
        if ((getPlayers().get(0).isAlive() && getPlayers().get(0).getSection().isSource())
                || (getPlayers().get(1).isAlive() && getPlayers().get(1).getSection().isSource())) {
            System.out.printf("#################################\n");
            System.out.printf("### You have won the game!!! ###\n");
            System.out.printf("#################################\n");
        } else {
            System.out.printf("#################################\n");
            System.out.printf("### You have lost the game!!! ###\n");
            System.out.printf("#################################\n");
        }
    }

    /**
     * 
     * Chama o turno dos jogadores e mostra as respectivas opções de
     * movimentação/ação.
     * 
     * @param player
     * @param input
     */
    public void callTurn(Player player, Scanner input) {
        if(!player.isAlive())
            return;
        executeMovement(player, input);
        if(player.section.isSource())
            return;
        executeAction(player, input);
    }

    /**
     * Exibe as opções de movimento.
     */
    public void displayMovementOptions() {
        System.out.printf("    U- Up\n");
        System.out.printf("    D- Down\n");
        System.out.printf("    L- Left\n");
        System.out.printf("    R- Right\n");
    }

    /**
     * Exibe as opções de ação.
     * 
     * @param player
     */
    public void displayActionOptions(Player player) {
        if (!(player instanceof Support)) {
            System.out.printf("What do you want to do P1:\n");
        } else {
            System.out.printf("What do you want to do P2:\n");
        }
        if (!(player.getSection().existAnEnemyAlive())) {
            System.out.printf("    b- search\n");
        } else {
            System.out.printf("    a- attack\n");
            System.out.printf("    b- search\n");
        }
        if (player instanceof Support)
            System.out.printf("    c- heal\n");
    }

    /**
     * Valida a entrada de direção.
     * 
     * @param input
     * @return
     */
    public Direction validateDirectionInput(char input) {
        switch (input) {
            case 'U':
                return Direction.UP;

            case 'D':
                return Direction.DOWN;

            case 'L':
                return Direction.LEFT;

            case 'R':
                return Direction.RIGHT;

            default:
                return null;
        }
    }

    public void displayBoard() {
        System.out.println(getBoard().strTabuleiro(getPlayers()));
    }

    /**
     * Exibe quais inimigos o player pode atacar.
     * 
     * @param player
     */
    public void displayEnemiesToAttack(Player player) {
        String[][] enemiesTexts = { { "a", "First" }, { "b", "Second" }, { "c", "Third" } };
        System.out.println("Who do you want to attack:");
        for (int i = 0; i < player.getSection().countEnemiesAlive(); i++) {
            System.out.printf("    %s - %s enemy\n", enemiesTexts[i][0], enemiesTexts[i][1]);
        }
    }

    /**
     * Retorna true se a entrada eh válida, se não retorna false.
     * 
     * @param player
     * @param input
     * @param attack
     * @return
     */
    public boolean checkInput(Player player, char input, boolean attack) {
        if (!attack) {
            if (player.getSection().existAnEnemyAlive()) {
                if (!(player instanceof Support)) {
                    if ((input == 'a') || (input == 'b'))
                        return true;
                } else {
                    if ((input == 'a') || (input == 'b') || (input == 'c'))
                        return true;
                }
            } else {
                if (!(player instanceof Support)) {
                    if (input == 'b')
                        return true;
                } else {
                    if ((input == 'b') || (input == 'c'))
                        return true;
                }
            }
        } else {
            char[] inputChars = { 'a', 'b', 'c' };
            for (int i = 0; i < player.getSection().countEnemiesAlive(); i++) {
                if ((input == inputChars[i]))
                    return true;
            }
        }

        return false;
    }

    /**
     * Realiza as duas ações do player.
     * 
     * @param player
     * @param input
     */
    public void executeAction(Player player, Scanner input) {
        char localInput;

        for (int i = 0; i < 2; i++) {
            displayActionOptions(player);
            localInput = input.nextLine().charAt(0);
            while (!checkInput(player, localInput, false)) {
                System.out.println("Invalid input");
                displayActionOptions(player);
                localInput = input.nextLine().charAt(0);
            }

            if (localInput == 'a') {
                if (player.getSection().countEnemiesAlive() > 1) {
                    displayEnemiesToAttack(player);
                    localInput = input.nextLine().charAt(0);
                    while (!checkInput(player, localInput, true)) {
                        System.out.println("Invalid input");
                        displayEnemiesToAttack(player);
                        localInput = input.nextLine().charAt(0);
                    }
                }

                if (!executeAttack(player, localInput))
                    System.out.println("Missed attack!");
            } else if (localInput == 'b') {
                player.search();
            } else if (localInput == 'c') {
                Support sup = (Support) player;

                System.out.printf("Who do you want to heal?\n");
                System.out.printf("    a- P1\n");
                System.out.printf("    b- P2\n");
                localInput = input.nextLine().charAt(0);
                while ((localInput != 'a') && (localInput != 'b')) {
                    localInput = input.nextLine().charAt(0);
                }
                if (localInput == 'a') {
                    sup.heal(players.get(0));
                } else {
                    sup.heal(sup);
                }
            }
            if (i == 0)
                System.out.println(this.getBoard().strTabuleiro(players));
        }
    }

    /**
     * Verifica se o jogador pode movimentar, se sim, movimenta.
     * 
     * @param player
     * @param input
     */
    public void executeMovement(Player player, Scanner input) {
        if (!(player.getSection().existAnEnemyAlive())) {
            Direction dirInput = null;
            while (dirInput == null) {
                if (!(player instanceof Support)) {
                    System.out.printf("Where to move PLAYER 1 (P1):\n");
                } else {
                    System.out.printf("Where to move PLAYER 2 (P2):\n");
                }
                displayMovementOptions();
                dirInput = validateDirectionInput(input.nextLine().charAt(0));
            }
            this.getBoard().movePlayer(player, dirInput);
            System.out.println(this.getBoard().strTabuleiro(players));
        }
    }

    /**
     * Realiza o ataque do player. Se funcionar, retorna true, senão, false.
     * 
     * @param player
     * @param input
     * @return
     */
    public boolean executeAttack(Player player, char input) {
        int counter = 1, flag = 1;

        if (input == 'b') {
            flag = 2;
        } else if (input == 'c') {
            flag = 3;
        }

        for (Enemy enemy : player.getSection().getEnemies()) {
            if (enemy.isAlive()) {
                if (counter == flag)
                    return player.attack(enemy);
                counter++;
            }
        }

        return true;
    }

    public void increaseCycles() {
        this.setCycles(this.getCycles() + 1);
    }
}
