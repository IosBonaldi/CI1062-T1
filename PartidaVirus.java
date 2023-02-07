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
    public PartidaVirus() {
    };

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

    public boolean checkGameConditions() {
        if((getJogadores().get(0).isVivo()) && (getCiclos() < 2) && (!(getJogadores().get(0).getSetor().isFonte()) && !(getJogadores().get(1).getSetor().isFonte())))
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

    public void chamarTurno(Jogador jogador, Scanner input) {
        char entrada;

        if (!(jogador instanceof Suporte)) {
            Jogador p1 = (Jogador) jogador;
            Boolean podeMovimentar = true;

            // Lida com a questão de ter inimigos no setor ou não pra saber se ele pode
            // movimentar ou não
            // Não foi testada, alguém vê necessidade disso virar um método/função?
            for (Inimigo i : p1.getSetor().getInimigos()) {
                if (i.isVivo())
                    podeMovimentar = false;
            }

            // Movimentação
            if (podeMovimentar) {
                Direcao dEntrada = null;
                while (dEntrada == null) {
                    System.out.printf("Where to go PLAYER 1 (P1)?\n");
                    opcoesDeMovimento();
                    dEntrada = validaEntrada(input.nextLine().charAt(0));
                }
                this.getTabuleiro().movimentar(p1, dEntrada);
            }

            // Ações (verificar entrada)
            for (int i = 0; i < 2; i++) {
                opcoesDeAcao(p1);
                entrada = input.nextLine().charAt(0);
                if (entrada == 'a') {
                    // p1.atacar(p1.getSetor().getInimigo(0));
                    System.out.println("Escolheu atacar");
                } else {
                    p1.procurar();
                    System.out.println("Escolheu procurar");
                }
            }
        } else {
            Suporte p2 = (Suporte) jogador;

            Boolean podeMovimentar = true;

            // Lida com a questão de ter inimigos no setor ou não pra saber se ele pode
            // movimentar ou não
            // Não foi testada, alguém vê necessidade disso virar um método/função?
            for (Inimigo i : p2.getSetor().getInimigos()) {
                if (i.isVivo())
                    podeMovimentar = false;
            }

            // Movimentação
            if (podeMovimentar) {
                Direcao dEntrada = null;
                while (dEntrada == null) {
                    System.out.printf("Where to go PLAYER 2 (P2)?\n");
                    opcoesDeMovimento();
                    dEntrada = validaEntrada(input.nextLine().charAt(0));
                }
                this.getTabuleiro().movimentar(p2, dEntrada);
            }

            // Ações (verificar entrada)
            for (int i = 0; i < 2; i++) {
                opcoesDeAcao(p2);
                entrada = input.nextLine().charAt(0);
                if (entrada == 'a') {
                    // p2.atacar(p2.getSetor().getInimigo(0));
                    System.out.println("Escolheu atacar");
                } else if (entrada == 'b') {
                    p2.procurar();
                    System.out.println("Escolheu procurar");
                } else {
                    System.out.printf("Who do u wanna heal?\n");
                    System.out.printf("    a- P1\n");
                    System.out.printf("    b- P2\n");
                    entrada = input.nextLine().charAt(0);
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

    public void opcoesDeMovimento() {
        System.out.printf("    U- Up\n");
        System.out.printf("    D- Down\n");
        System.out.printf("    L- Left\n");
        System.out.printf("    R- Right\n");
    }

    public void opcoesDeAcao(Personagem personagem) {
        System.out.printf("What do u wanna do?\n");
        System.out.printf("    a- attack\n");
        System.out.printf("    b- search\n");
        if (personagem instanceof Suporte)
            System.out.printf("    c- heal\n");
    }

    public Direcao validaEntrada(char entrada) {
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
