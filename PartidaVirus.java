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

    /* Outros métodos */
    public void iniciarJogo() {

    }

    /* Chama o turno dos jogadores e mostra as respectivas opções de movimentação/ação */ 
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

    /* Sobrecarga de métodos porque os parâmetros pra inimigos eh diferente */ 
    public void displayShift(Inimigo inimigo, Jogador alvoInimigo) {
        Random random = new Random();
        int num;

        if (inimigo.isVivo()) {
            num = random.nextInt(6) + 1;
            System.out.println(num);
            // Caso o número aleatório seja par
            if ((num % 2) == 0) {
                inimigo.atacar(alvoInimigo);
                System.out.println(num + ": atacou;"); // Teste
            }
        }
    }

    /* Exibe as opções de movimento */
    public void displayMovementOptions() {
        System.out.printf("    U- Up\n");
        System.out.printf("    D- Down\n");
        System.out.printf("    L- Left\n");
        System.out.printf("    R- Right\n");
    }

    /* Exibe as opções de ação */
    public void displayActionOptions(Jogador player) {
        if(!(player instanceof Suporte)) {
            System.out.printf("What do you want to do P1:\n");
        } else {
            System.out.printf("What do you want to do P2:\n");
        }
        if(!(player.getSetor().areEnemiesAlive())) {
            System.out.printf("    b- search\n");
        } else {
            System.out.printf("    a- attack\n");
            System.out.printf("    b- search\n");
        }    
        if (player instanceof Suporte)
            System.out.printf("    c- heal\n");
    }

    /* Valida a entrada de direção */
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

    /* Retorna true se tem quem atacar, se não retorna false */
    public void displayEnemiesToAttack(Jogador player) {
        ArrayList<Inimigo> aliveEnemies = new ArrayList<>();

        for (Inimigo enemy: player.getSetor().getInimigos()) {
            if (enemy.isVivo()) {
                aliveEnemies.add(enemy);
            }
        }
    
        String[][] enemiesTexts = {{"a", "First"}, {"b", "Second"}, {"c", "Third"}};
        System.out.println("Who do you want to attack:");
        for (int i = 0; i < aliveEnemies.size(); i++) {
            System.out.printf("    %s - %s enemy\n", enemiesTexts[(aliveEnemies.get(i).getPosicao())][0], enemiesTexts[(aliveEnemies.get(i).getPosicao())][1]);
        }
    }

    /* Retorna true se a entrada eh válida, se não retorna false */
    public boolean checkInput(Jogador player, char input, boolean action) {
        ArrayList<Inimigo> aliveEnemies = new ArrayList<>();

        if(action) {
            if(player.getSetor().areEnemiesAlive()) {
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
            for (Inimigo inimigo: player.getSetor().getInimigos()) {
                if (inimigo.isVivo()) {
                    aliveEnemies.add(inimigo);
                }
            }

            if (aliveEnemies.isEmpty()) {
                return false;
            }

            char[] inputChars = {'a', 'b', 'c'};
            for (int i = 0; i <= aliveEnemies.size(); i++) {
                if((input == inputChars[(aliveEnemies.get(i).getPosicao())]))
                    return true;
            }
        }

        return false;
    }

    /* Realiza as duas ações do player */
    public void performAction(Jogador player, Scanner input) {
        char localInput;
        
        for (int i = 0; i < 2; i++) {
            displayActionOptions(player);
            localInput = input.nextLine().charAt(0);
            while(!checkInput(player, localInput, true)) {
                displayActionOptions(player);
                localInput = input.nextLine().charAt(0);
            }
            
            if (localInput == 'a') {
                if(contEnemiesAlive(player) > 1) {
                    displayEnemiesToAttack(player);
                    localInput = input.nextLine().charAt(0);
                    while(!checkInput(player, localInput, false)) {
                        displayEnemiesToAttack(player);
                        localInput = input.nextLine().charAt(0);
                    }
                }
                 
                performAttack(player, localInput);
            } else if (localInput == 'b') {
                player.procurar();
            } else if (localInput == 'c') {
                Suporte sup = (Suporte) player;

                System.out.printf("Who do u wanna heal?\n");
                System.out.printf("    a- P1\n");
                System.out.printf("    b- P2\n");
                localInput = input.nextLine().charAt(0);
                while((localInput != 'a') || (localInput != 'b')) {
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

    /* Verifica se o jogador pode movimentar, se sim, movimenta */
    public void performMovement(Jogador player, Scanner input) {
        if (!(player.getSetor().areEnemiesAlive())) {
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

    /* Realiza o ataque do player */
    public void performAttack(Jogador player, char input) {

        if(input == 'a' && contEnemiesAlive(player) > 1) {
            player.atacar(player.getSetor().getInimigo(0));
        } else if(input == 'b') {
            player.atacar(player.getSetor().getInimigo(1));
        } else if(input == 'c') {
            player.atacar(player.getSetor().getInimigo(2));
        } else {
            for(Inimigo enemy: player.getSetor().getInimigos()) {
                if(enemy.isVivo())
                    player.atacar(player.getSetor().getInimigo(enemy.getPosicao()));
            }
        }
    }

    /* Retorna a quantidade de inimigos vivos dentro do setor do player */
    public int contEnemiesAlive(Jogador player) {
        int contEnemies = 0;
        for(Inimigo enemy: player.getSetor().getInimigos()) {
            if(enemy.isVivo())
                contEnemies++;
        }

        return contEnemies;
    }

    public void imprimirTabuleiro() {
    }

    public void incrementaCiclo() {
        this.setCiclos(this.getCiclos() + 1);
    }
}

/*
 * public class Jogo {
 * private ArrayList<Jogador> jogadores;
 * private Tabuleiro tabuleiro;
 * private int turnos;
 * private boolean ativo;
 * private LogHandler log;
 * 
 * public Jogo(ArrayList<Jogador> jogadores, Tabuleiro tabuleiro, int turnos,
 * boolean ativo) {
 * this.jogadores = jogadores;
 * this.tabuleiro = tabuleiro;
 * this.turnos = turnos;
 * this.ativo = ativo;
 * this.log = new LogHandler("./log.txt");
 * }
 * 
 * public ArrayList<Jogador> getJogadores() {
 * return jogadores;
 * }
 * 
 * public void setJogadores(ArrayList<Jogador> jogadores) {
 * this.jogadores = jogadores;
 * }
 * 
 * public Tabuleiro getTabuleiro() {
 * return tabuleiro;
 * }
 * 
 * public void setTabuleiro(Tabuleiro tabuleiro) {
 * this.tabuleiro = tabuleiro;
 * }
 * 
 * public int getTurnos() {
 * return turnos;
 * }
 * 
 * public void setTurnos(int turnos) {
 * this.turnos = turnos;
 * }
 * 
 * public boolean isAtivo() {
 * return ativo;
 * }
 * 
 * public void setAtivo(boolean ativo) {
 * this.ativo = ativo;
 * }
 * 
 * public LogHandler getLog() {
 * return log;
 * }
 * 
 * public void iniciarJogo() {
 * // Início do jogo (verifica existência do log para criar ou não);
 * log.createLogFile();
 * }
 * 
 * public void chamarTurno(Personagem p) {
 * // Quando chegar no fim do jogo (atualiza arquivo de log);
 * log.logF}ileManipulation(this.pontuacaoFinal());
 * }
 * 
 * public void imprimirTabuleiro() {
 * 
 * }
 * 
 * // Quando finalizar o jogo, chama-se este método para
 * // calcular a pontuação final (pontos principal + pontos suporte);
 * public Integer pontuacaoFinal(){
 * Integer pontuacaoFinal = 0;
 * for (Jogador jogador : jogadores) {
 * pontuacaoFinal += jogador.pontuacao;
 * }
 * return pontuacaoFinal;
 * }
 * 
 * }
 */
