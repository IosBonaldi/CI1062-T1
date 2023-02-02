import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PartidaVirus {
    // Atributos
    private ArrayList<Jogador> jogadores;
    private Tabuleiro tabuleiro;
    private int ciclos;
    private boolean ativo;

    // Construtores
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

    // Métodos get/set
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
    public void iniciarJogo() {

    }

    public void chamarTurno(Jogador jogador, Scanner input) {
        char entrada;
        
        // Ações do Player 1
        if (!(jogador instanceof Suporte)) {
            Jogador p1 = (Jogador) jogador;

            /* Movimentação
             * Se não tem inimigo vivo deixa movimentar
             * Se ele movimentar, mostra o novo Setor e depois pede as ações */ 
            if (!(p1.getSetor().isThereEnemyAlife())) {
                Direcao dEntrada = null;
                while (dEntrada == null) {
                    System.out.printf("Where to go PLAYER 1 (P1)?\n");
                    showMovementOptions();
                    dEntrada = validadeEntry(input.nextLine().charAt(0));
                }
                this.getTabuleiro().movimentar(p1, dEntrada);
                System.out.println(this.getTabuleiro().strTabuleiro(jogadores));
            }

            // Ações
            for (int i = 0; i < 2; i++) {
                actionOptions(p1);
                entrada = input.nextLine().charAt(0);
                while(!checkInput(p1, entrada, true)) {
                    entrada = input.nextLine().charAt(0);
                }
                
                if (entrada == 'a') {
                    entrada = 'x';
                    if(whoToAttack(p1)) {
                        entrada = input.nextLine().charAt(0);
                        while(!checkInput(p1, entrada, false)) {
                            entrada = input.nextLine().charAt(0);
                        }
                    }
                     
                    performAttack(p1, entrada);
                } else {
                    p1.procurar();
                }
            }
        } else {
            Suporte p2 = (Suporte) jogador;

            /* Movimentação
             * Se não tem inimigo vivo deixa movimentar
             * Se ele movimentar, mostra o novo Setor e depois pede as ações */
            if (!(p2.getSetor().isThereEnemyAlife())) {
                Direcao dEntrada = null;
                while (dEntrada == null) {
                    System.out.printf("Where to go PLAYER 2 (P2)?\n");
                    showMovementOptions();
                    dEntrada = validadeEntry(input.nextLine().charAt(0));
                }
                this.getTabuleiro().movimentar(p2, dEntrada);
                System.out.println(this.getTabuleiro().strTabuleiro(jogadores));
            }

            // Ações
            for (int i = 0; i < 2; i++) {
                actionOptions(p2);
                entrada = input.nextLine().charAt(0);
                while(!checkInput(p2, entrada, true)) {
                    entrada = input.nextLine().charAt(0);
                }
                
                if (entrada == 'a') {
                    entrada = 'x';
                    if(whoToAttack(p2)) {
                        entrada = input.nextLine().charAt(0);
                        while(!checkInput(p2, entrada, false)) {
                            entrada = input.nextLine().charAt(0);
                        }
                    }
                    System.out.println(entrada);
                    performAttack(p2, entrada);
                } else if (entrada == 'b') {
                    p2.procurar();
                } else {
                    System.out.printf("Who do u wanna heal?\n");
                    System.out.printf("    a- P1\n");
                    System.out.printf("    b- P2\n");
                    entrada = input.nextLine().charAt(0);
                    while((entrada != 'a') || (entrada != 'b')) {
                        entrada = input.nextLine().charAt(0);
                    }
                    if (entrada == 'a') {
                        p2.curar(jogadores.get(0));
                        System.out.println("Escolheu curar p1");
                    } else {
                        p2.curar(p2);
                        System.out.println("Escolheu curar p2");
                    }
                }
            }
        }
    }

    // Sobrecarga de métodos porque os parâmetros pra inimigos eh diferente...
    public void chamarTurno(Inimigo inimigo, Jogador alvoInimigo) {
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

    public void showMovementOptions() {
        System.out.printf("    U- Up\n");
        System.out.printf("    D- Down\n");
        System.out.printf("    L- Left\n");
        System.out.printf("    R- Right\n");
    }

    public void actionOptions(Jogador personagem) {
        System.out.printf("What do u wanna do?\n");
        if(!(personagem.getSetor().isThereEnemyAlife())) {
            System.out.printf("    b- search\n");
        } else {
            System.out.printf("    a- attack\n");
            System.out.printf("    b- search\n");
        }    
        if (personagem instanceof Suporte)
            System.out.printf("    c- heal\n");
    }

    public Direcao validadeEntry(char entrada) {
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
    
    public void imprimirTabuleiro() {
    }

    public void incrementaCiclo() {
        this.setCiclos(this.getCiclos() + 1);
    }

    /* Retorna true se tem quem atacar, se não retorna false */
    public boolean whoToAttack(Jogador jogador) {
        if(jogador.getSetor().getInimigo(0) != null && jogador.getSetor().getInimigo(1) != null && jogador.getSetor().getInimigo(2) != null
        && jogador.getSetor().getInimigo(0).isVivo() && jogador.getSetor().getInimigo(1).isVivo() && jogador.getSetor().getInimigo(2).isVivo()) {
                System.out.printf("Who do u wanna attack:\n");
                System.out.printf("    a- first enemy\n");
                System.out.printf("    b- second enemy\n");
                System.out.printf("    c- third enemy\n");
                return true;
        } else if(jogador.getSetor().getInimigo(0) != null && jogador.getSetor().getInimigo(1) != null
        && jogador.getSetor().getInimigo(0).isVivo() && jogador.getSetor().getInimigo(1).isVivo()) {
                System.out.printf("Who do u wanna attack:\n");
                System.out.printf("    a- first enemy\n");
                System.out.printf("    b- second enemy\n");
                return true;
        } else if(jogador.getSetor().getInimigo(0) != null && jogador.getSetor().getInimigo(2) != null
        && jogador.getSetor().getInimigo(0).isVivo() && jogador.getSetor().getInimigo(2).isVivo()) {
                System.out.printf("Who do u wanna attack:\n");
                System.out.printf("    a- first enemy\n");
                System.out.printf("    c- third enemy\n");
                return true;
        } else if(jogador.getSetor().getInimigo(1) != null && jogador.getSetor().getInimigo(2) != null
        && jogador.getSetor().getInimigo(1).isVivo() && jogador.getSetor().getInimigo(2).isVivo()) {
                System.out.printf("Who do u wanna attack:\n");
                System.out.printf("    b- second enemy\n");
                System.out.printf("    c- third enemy\n");
                return true;      
        }

        return false;
    }

    /* Retorna true se a entrada eh válida, se não retorna false */
    public boolean checkInput(Jogador jogador, char entrada, boolean action) {
        if(action && !(jogador instanceof Suporte)) {
            if(jogador.getSetor().isThereEnemyAlife()) {
                if((entrada == 'a') || (entrada == 'b'))
                    return true;
            } else {
                if(entrada == 'b')
                    return true;
            }
        } else if(action && (jogador instanceof Suporte)) {
            if(jogador.getSetor().isThereEnemyAlife()) {
                if((entrada == 'a') || (entrada == 'b') || (entrada == 'c'))
                    return true;
            } else {
                if((entrada == 'b') || (entrada == 'c'))
                    return true;
            }
        } else if(!action) {
            if(jogador.getSetor().getInimigo(0) != null && jogador.getSetor().getInimigo(1) != null && jogador.getSetor().getInimigo(2) != null
            && jogador.getSetor().getInimigo(0).isVivo() && jogador.getSetor().getInimigo(1).isVivo() && jogador.getSetor().getInimigo(2).isVivo()) {
                    if((entrada == 'a') || (entrada == 'b') || (entrada == 'c'))
                        return true;
            } else if(jogador.getSetor().getInimigo(0) != null && jogador.getSetor().getInimigo(1) != null
            && jogador.getSetor().getInimigo(0).isVivo() && jogador.getSetor().getInimigo(1).isVivo()) {
                    if((entrada == 'a') || (entrada == 'b'))
                        return true;
            } else if(jogador.getSetor().getInimigo(0) != null && jogador.getSetor().getInimigo(2) != null 
            && jogador.getSetor().getInimigo(0).isVivo() && jogador.getSetor().getInimigo(2).isVivo()) {   
                    if((entrada == 'a') || (entrada == 'c'))
                        return true; 
            } else if(jogador.getSetor().getInimigo(1) != null && jogador.getSetor().getInimigo(2) != null
            && jogador.getSetor().getInimigo(1).isVivo() && jogador.getSetor().getInimigo(2).isVivo()) {  
                    if((entrada == 'b') || (entrada == 'c'))
                        return true;
            }
        }

        return false;
    }

    /* Realiza o ataque do jogador */
    public void performAttack(Jogador jogador, char entrada) {
        if(entrada == 'a') {
            jogador.atacar(jogador.getSetor().getInimigo(0));
        } else if(entrada == 'b') {
            jogador.atacar(jogador.getSetor().getInimigo(1));
        } else if(entrada == 'c') {
            jogador.atacar(jogador.getSetor().getInimigo(2));
        } else {
            if(jogador.getSetor().getInimigo(0) != null && jogador.getSetor().getInimigo(0).isVivo()) {
                jogador.atacar(jogador.getSetor().getInimigo(0));
            } else if(jogador.getSetor().getInimigo(1) != null && jogador.getSetor().getInimigo(1).isVivo()) {
                jogador.atacar(jogador.getSetor().getInimigo(1));
            } else if(jogador.getSetor().getInimigo(2) != null && jogador.getSetor().getInimigo(2).isVivo()) {
                jogador.atacar(jogador.getSetor().getInimigo(2));
            }
            
        }
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
