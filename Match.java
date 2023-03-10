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

    public void displayHighestScores(Scanner input, LogHandler handler) {
        System.out.println("\nDo you want to see the highest scores so far? Y/N");
        char in = input.next().charAt(0);
        while (in != 'Y' && in != 'y' && in != 'N' && in != 'n') {
            System.out.println("Invalid input. Only Y/y or N/n, please.");
            in = input.next().charAt(0);
        }

        System.out.println((in == 'Y' || in == 'y') ? "\nHIGHEST SCORES\n" + handler.topTenToString() : "Goodbye!");
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
        System.out.println(getBoard().strTabuleiro(getPlayers()));
        if (!player.isAlive())
            return;
        if (callMovement(player, input))
            System.out.println(this.getBoard().strTabuleiro(players));
        if (player.section.isSource())
            return;
        callAction(player, input);
        System.out.println(this.getBoard().strTabuleiro(players));
        callAction(player, input);
    }

    /**
     * 
     * Chama um acao.
     * 
     * @param player
     * @param input
     */
    private void callAction(Player player, Scanner input) {
        displayActionOptions(player, 3);
        char inputedAction = inputAction(player, input, 3);

        switch (inputedAction) {
            case 'a':
                Boolean attackSucceed;
                displayEnemiesToAttack(player);
                char inputedAttack = inputAction(player, input, 1);
                attackSucceed = executeAttack(player, inputedAttack);
                if (!attackSucceed)
                    System.out.println("Missed attack!");
                else
                    System.out.println("Successful attack!");
                break;
            case 'b':
                switch (player.search()) {
                    case 0:
                        System.out.println("Your search has no effect!");
                        break;

                    case 1:
                        System.out.println("You gained 1 defense point for your search!");
                        break;

                    case 2:
                        System.out.println("You gained 2 defense points for your search!");
                        break;

                    case 3:
                        System.out.println("Your search took 1 point of defense from all enemies!");
                        break;

                    default:
                        break;
                }

                break;
            case 'c':
                displayHealOptions();
                char inputedHeal = inputAction(player, input, 2);
                executeHeal((Support) player, inputedHeal);
                break;
            default:
                break;
        }
    }

    /**
     * Exibe as opções de movimento.
     */
    private void displayMovementOptions() {
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
    private void displayActionOptions(Player player, int context) {
        switch (context) {
            case 1:
                displayEnemiesToAttack(player);
                break;
            case 2:
                displayHealOptions();
                break;
            case 3:
                System.out.println("What do you want to do P" + (player instanceof Support ? 2 : 1) + ":");
                System.out.print(player.getSection().existAnEnemyAlive() ? "    a- attack\n" : "");
                System.out.print(player.getSection().getType() != SectionType.PRIVATE ? "    b- search\n" : "");
                if (player instanceof Support && (player.getDef() < player.getMaxDef() || (this.getPlayers().get(0)
                        .getDef() < this.getPlayers().get(0).getMaxDef() && areThePlayersInTheSameSection())))
                    System.out.printf("    c- heal\n");
                break;

            default:
                break;
        }

    }

    /**
     * Valida a entrada de direção.
     * 
     * @param input
     * @return
     */
    private Direction convertCharToDirection(char input) {
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

    /**
     * Exibe quais inimigos o player pode atacar.
     * 
     * @param player
     */
    private void displayEnemiesToAttack(Player player) {
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
     * @param context diz o contexto em que o checkInput foi chamado. Sendo
     *                1:ataque, 2:curar e 3:outros
     * @return True se o input eh valido, do contrario false
     */
    private boolean checkInput(Player player, char input, int context) {
        char[] actions = { 'a', 'b', 'c' };

        int charPosition = 0;
        while (charPosition < actions.length && input != actions[charPosition])
            charPosition++;
        /* O input nao eh um caracter valido */
        if (charPosition == actions.length)
            return false;

        /*
         * A posicao a onde o char input colidiu no while define qual eh a letra
         * recebida
         * sabendo disse eh possivel usar ela para validar os inputs
         */
        charPosition += 1;
        switch (context) {
            case 1:
                if (charPosition > player.getSection().countEnemiesAlive())
                    return false;
                break;
            case 2:
                /* Se charPosition + 1 > 2, entao a letra recebida foi a c */
                if (charPosition > 2)
                    return false;
                if (charPosition == 2 && this.getPlayers().get(1).getDef() == this
                        .getPlayers().get(1).getMaxDef())
                    return false;
                if (charPosition == 1 && (!areThePlayersInTheSameSection() || this.getPlayers().get(0).getDef() == this
                        .getPlayers().get(0).getMaxDef()))
                    return false;
                break;
            case 3:
                if (charPosition == 3
                        && (player.getDef() == player.getMaxDef() && this.getPlayers().get(0).getDef() == this
                                .getPlayers().get(0).getMaxDef())) {
                    return false;
                }
                if (charPosition == 2 && player.getSection().getType() == SectionType.PRIVATE)
                    return false;
                if (player.getSection().existAnEnemyAlive()) {
                    if (!(player instanceof Support) && charPosition > 2)
                        return false;
                    /* Nao eh necessario testar para o suporte */
                } else {
                    if (!(player instanceof Support) && charPosition != 2)
                        return false;
                    else if (charPosition != 2 && charPosition != 3)
                        return false;
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Realiza o input da acao e o retorna.
     * 
     * @param player
     * @param input
     * @param context diz o contexto em que o checkInput foi chamado. Sendo
     *                1:ataque, 2:curar e 3:outros
     * @return acao recebida
     */
    private char inputAction(Player player, Scanner input, int context) {
        char actionInput;
        actionInput = input.nextLine().charAt(0);
        while (!checkInput(player, actionInput, context)) {
            switch (context) {
                case 1:
                    System.out.println("You can't attack this! Please, enter a valid target.");
                    break;
                case 2:
                    System.out.println("Your heal doesn't seem to work... maybe you could try a valid input?");
                    break;
                case 3:
                    System.out.println("This isn't a valid input!");
                default:
                    break;
            }
            displayActionOptions(player, context);
            actionInput = input.nextLine().charAt(0);
        }
        return actionInput;
    }

    private void displayHealOptions() {
        System.out.printf("Who do you want to heal?\n");
        if (areThePlayersInTheSameSection() && this.getPlayers().get(0).getDef() < this.getPlayers().get(0).getMaxDef())
            System.out.printf("    a- P1\n");
        if (this.getPlayers().get(1).getDef() < this.getPlayers().get(1).getMaxDef())
            System.out.printf("    b- P2\n");
    }

    private void executeHeal(Support playerHealing, char playerHealed) {
        if (playerHealed == 'a')
            playerHealing.heal(players.get(0));
        else
            playerHealing.heal(players.get(1));
    }

    private Boolean areThePlayersInTheSameSection() {
        return getPlayers().get(0).section.equals(getPlayers().get(1).section);
    }

    /**
     * Verifica se o jogador pode movimentar, se sim, movimenta.
     * 
     * @param player
     * @param input
     * @return retorna true se um movimento foi realizado, do contrario false
     */
    private boolean callMovement(Player player, Scanner input) {
        if (!(player.getSection().existAnEnemyAlive())) {
            Direction dirInput = null;

            if (!(player instanceof Support)) {
                System.out.printf("Where to move PLAYER 1 (P1):\n");
            } else {
                System.out.printf("Where to move PLAYER 2 (P2):\n");
            }
            displayMovementOptions();
            dirInput = convertCharToDirection(input.nextLine().charAt(0));

            while (!validateMovement(player, dirInput)) {
                if (!(player instanceof Support)) {
                    System.out.printf("Where to move PLAYER 1 (P1):\n");
                } else {
                    System.out.printf("Where to move PLAYER 2 (P2):\n");
                }
                displayMovementOptions();
                dirInput = convertCharToDirection(input.nextLine().charAt(0));
            }
            this.getBoard().movePlayer(player, dirInput);
            return true;
        }
        return false;
    }

    private boolean validateMovement(Player player, Direction dir) {
        if (dir == null) {
            System.out.println("Hmmm... That's not a valid direction!");
            return false;
        } else if (!player.getSection().isADoor(dir)) {
            System.out.println("Are you trying to pass through a Wall? I don't think you can do that...");
            return false;
        }
        return true;
    }

    /**
     * Realiza o ataque do player. Se funcionar, retorna true, senão, false.
     * 
     * @param player
     * @param input
     * @return
     */
    private boolean executeAttack(Player player, char input) {
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
