import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PartidaVirus {
    /* Atributos */ 
    private ArrayList<Jogador> jogadores;
    private Tabuleiro tabuleiro;
    private int ciclos;
    private boolean ativo;

    /* Construtores */ 
    public PartidaVirus() {};

    public PartidaVirus(ArrayList<Jogador> jogadores) {
        this.setJogadores(jogadores);
    }

    public PartidaVirus(Tabuleiro tabuleiro, boolean ativo) {
        this.setTabuleiro(tabuleiro);
        this.setCiclos(0);
        this.setAtivo(ativo);
    }

    public PartidaVirus(ArrayList<Jogador> jogadores, Tabuleiro tabuleiro, boolean ativo) {
        this.setJogadores(jogadores);
        this.setTabuleiro(tabuleiro);
        this.setCiclos(0);
        this.setAtivo(ativo);
    }

    public PartidaVirus(ArrayList<Jogador> jogadores, Tabuleiro tabuleiro, int ciclos, boolean ativo) {
        this.setJogadores(jogadores);
        this.setTabuleiro(tabuleiro);
        this.setCiclos(ciclos);
        this.setAtivo(ativo);
    }

    /* Métodos get/set */ 
    public ArrayList<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(ArrayList<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public int getCiclos() {
        return ciclos;
    }

    public void setCiclos(int ciclos) {
        this.ciclos = ciclos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // Outros métodos
    public void startGame(Jogador p1, Jogador p2) {
        p1.setSetor(getTabuleiro().getSetor(2, 2));
        p2.setSetor(getTabuleiro().getSetor(2, 2));
        getJogadores().add(p1);
        getJogadores().add(p2);
    }

    public boolean checkGameConditions() {
        if((getJogadores().get(0).isVivo()) && (getCiclos() < 25) && (!(getJogadores().get(0).getSetor().isFonte()) && !(getJogadores().get(1).getSetor().isFonte())))
            return true;
        return false;
    }

    public void displayGameResult() {
        if((getJogadores().get(0).isVivo() && getJogadores().get(0).getSetor().isFonte()) || (getJogadores().get(1).isVivo() && getJogadores().get(1).getSetor().isFonte())) {
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
     * Chama o turno dos jogadores e mostra as respectivas opções de movimentação/ação.
     * @param player
     * @param input
     */
    public void displayShift(Jogador player, Scanner input) {
        
        /* Turno do Player 1 */ 
        if (!(player instanceof Suporte)) {

            /* Movimentação
             * Se não tem inimigo vivo deixa movimentar
             * Se ele movimentar, mostra o novo Setor e depois parte para as ações */ 
            performMovement(player, input);

            /* Ações */ 
            performAction(player, input);

        } else {
            /* Turno do Player 2 */
            Suporte p2 = (Suporte) player;

            /* Movimentação
             * Se não tem inimigo vivo deixa movimentar
             * Se ele movimentar, mostra o novo Setor e depois pede as ações */
            performMovement(p2, input);

            /* Ações */ 
            performAction(p2, input);
        }
    }

    /**
     * Sobrecarga de métodos porque os parâmetros pra inimigos eh diferente.
     * @param inimigo
     * @param alvoInimigo
     */
    public void displayShift(Inimigo inimigo, Jogador alvoInimigo) {
        Random random = new Random();
        int num;

        if (inimigo.isVivo()) {
            num = random.nextInt(6) + 1;
            // Caso o número aleatório seja par
            if ((num % 2) == 0)
                inimigo.atacar(alvoInimigo);
        }
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
     * @param player
     */
    public void displayActionOptions(Jogador player) {
        if(!(player instanceof Suporte)) {
            System.out.printf("What do you want to do P1:\n");
        } else {
            System.out.printf("What do you want to do P2:\n");
        }
        if(!(player.getSetor().isThereEnemyAlive())) {
            System.out.printf("    b- search\n");
        } else {
            System.out.printf("    a- attack\n");
            System.out.printf("    b- search\n");
        }    
        if (player instanceof Suporte)
            System.out.printf("    c- heal\n");
    }

    /**
     * Valida a entrada de direção.
     * @param entrada
     * @return
     */
    public Direcao validateDirectionInput(char entrada) {
        switch (entrada) {
            case 'U':
                return Direcao.CIMA;

            case 'D':
                return Direcao.BAIXO;

            case 'L':
                return Direcao.ESQUERDA;

            case 'R':
                return Direcao.DIREITA;

            default:
                return null;
        }
    }
    
    public void displayBoard() {
        System.out.println(getTabuleiro().strTabuleiro(getJogadores()));
    }

    /**
     * Exibe quais inimigos o player pode atacar.
     * @param player
     */
    public void displayEnemiesToAttack(Jogador player) {    
        String[][] enemiesTexts = {{"a", "First"}, {"b", "Second"}, {"c", "Third"}};
        System.out.println("Who do you want to attack:");
        for (int i = 0; i < player.getSetor().countEnemiesAlive(); i++) {
            System.out.printf("    %s - %s enemy\n", enemiesTexts[i][0], enemiesTexts[i][1]);
        }
    }

    /**
     * Retorna true se a entrada eh válida, se não retorna false.
     * @param player
     * @param input
     * @param attack
     * @return
     */
    public boolean checkInput(Jogador player, char input, boolean attack) {
        if(! attack) {
            if(player.getSetor().isThereEnemyAlive()) {
                if(!(player instanceof Suporte)) {
                    if((input == 'a') || (input == 'b'))
                        return true;
                } else {
                    if((input == 'a') || (input == 'b') || (input == 'c'))
                        return true;
                }
            } else {
                if(!(player instanceof Suporte)) {
                    if(input == 'b')
                        return true;
                } else {
                    if((input == 'b') || (input == 'c'))
                        return true;
                }
            }
        } else {
            char[] inputChars = {'a', 'b', 'c'};
            for (int i = 0; i < player.getSetor().countEnemiesAlive(); i++) {
                if((input == inputChars[i]))
                    return true;
            }
        }

        return false;
    }

    /**
     * Realiza as duas ações do player.
     * @param player
     * @param input
     */
    public void performAction(Jogador player, Scanner input) {
        char localInput;
        
        for (int i = 0; i < 2; i++) {
            displayActionOptions(player);
            localInput = input.nextLine().charAt(0);
            while(!checkInput(player, localInput, false)) {
                System.out.println("Entrada inválida");
                displayActionOptions(player);
                localInput = input.nextLine().charAt(0);
            }
            
            if (localInput == 'a') {
                if(player.getSetor().countEnemiesAlive() > 1) {
                    displayEnemiesToAttack(player);
                    localInput = input.nextLine().charAt(0);
                    while(!checkInput(player, localInput, true)) {
                        System.out.println("Entrada inválida");
                        displayEnemiesToAttack(player);
                        localInput = input.nextLine().charAt(0);
                    }
                }
                 
                if(! performAttack(player, localInput))
                    System.out.println("Ataque falhou!");
            } else if (localInput == 'b') {
                player.procurar();
            } else if (localInput == 'c') {
                Suporte sup = (Suporte) player;

                System.out.printf("Who do u wanna heal?\n");
                System.out.printf("    a- P1\n");
                System.out.printf("    b- P2\n");
                localInput = input.nextLine().charAt(0);
                while((localInput != 'a') && (localInput != 'b')) {
                    localInput = input.nextLine().charAt(0);
                }
                if (localInput == 'a') {
                    sup.curar(jogadores.get(0));
                } else {
                    sup.curar(sup);
                }
            }
            if(i == 0)
                System.out.println(this.getTabuleiro().strTabuleiro(jogadores));
        }
    }

    /**
     * Verifica se o jogador pode movimentar, se sim, movimenta.
     * @param player
     * @param input
     */
    public void performMovement(Jogador player, Scanner input) {
        if (!(player.getSetor().isThereEnemyAlive())) {
            Direcao dInput = null;
            while (dInput == null) {
                if(!(player instanceof Suporte)) {
                    System.out.printf("Where to go PLAYER 1 (P1):\n");
                } else {
                    System.out.printf("Where to go PLAYER 2 (P2):\n");
                }
                displayMovementOptions();
                dInput = validateDirectionInput(input.nextLine().charAt(0));
            }
            this.getTabuleiro().movimentar(player, dInput);
            System.out.println(this.getTabuleiro().strTabuleiro(jogadores));
        }
    }

    /**
     * Realiza o ataque do player. Se funcionar, retorna true, senão, false.
     * @param player
     * @param input
     * @return
     */
    public boolean performAttack(Jogador player, char input) {
        int cont = 1, flag = 1;

        if(input == 'b') {
            flag = 2;
        } else if(input == 'c') {
            flag = 3;
        }

        for(Inimigo enemy: player.getSetor().getInimigos()) {
            if(enemy.isVivo()) {
                if(cont == flag)
                    return player.atacar(enemy);
                cont++;
            }
        }

        return true;
    }

    public void incrementaCiclo() {
        this.setCiclos(this.getCiclos() + 1);
    }
}