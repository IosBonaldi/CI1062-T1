import java.util.ArrayList;
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
        this.jogadores = jogadores;
    }

    public PartidaVirus(ArrayList<Jogador> jogadores, Tabuleiro tabuleiro, boolean ativo) {
        this.jogadores = jogadores;
        this.tabuleiro = tabuleiro;
        this.ciclos = 0;
        this.ativo = ativo;
    }

    public PartidaVirus(ArrayList<Jogador> jogadores, Tabuleiro tabuleiro, int ciclos, boolean ativo) {
        this.jogadores = jogadores;
        this.tabuleiro = tabuleiro;
        this.ciclos = ciclos;
        this.ativo = ativo;
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

    public void chamarTurno(Personagem personagem) {
        Scanner input = new Scanner(System.in);
        char entrada;

        if((personagem instanceof Jogador) && !(personagem instanceof Suporte)) {
            Jogador p1 = (Jogador)personagem;

            // Lidar com a questão de ter inimigos no setor ou não pra saber se ele pode movimentar ou não
            // Movimenta
            System.out.printf("Where to go PLAYER 1 (P1)?\n");
            opcoesDeMovimento();
            entrada = input.nextLine().charAt(0);
            movimentacao(p1, entrada);
            
            // Ações (verificar entrada)
            for(int i = 0; i < 2; i++) {
                opcoesDeAcao(p1);
                entrada = input.nextLine().charAt(0);
                if(entrada == 'a') {
                    // p1.atacar(p1.getSetor().getInimigo(0));
                    System.out.println("Escolheu atacar");
                } else {
                    p1.procurar();
                    System.out.println("Escolheu procurar");
                }
            }
        }   

        if(personagem instanceof Suporte) {
            Suporte p2 = (Suporte)personagem;
            // Lidar com a questão de ter inimigos no setor ou não pra saber se ele pode movimentar ou não
            // Movimenta
            System.out.printf("Where to go PLAYER 2 (P2)?\n");
            opcoesDeMovimento();
            entrada = input.nextLine().charAt(0);
            movimentacao(p2, entrada);
            
            // Ações (verificar entrada)
            for(int i = 0; i < 2; i++) {
                opcoesDeAcao(p2);
                entrada = input.nextLine().charAt(0);
                if(entrada == 'a') {
                    // p2.atacar(p2.getSetor().getInimigo(0));
                    System.out.println("Escolheu atacar");
                } else if(entrada == 'b') {
                    p2.procurar();
                    System.out.println("Escolheu procurar");
                } else {
                    System.out.printf("Who do u wanna heal?\n");
                    System.out.printf("    a- P1\n");
                    System.out.printf("    b- P2\n");
                    entrada = input.nextLine().charAt(0);
                    if(entrada == 'a') {
                        p2.curar(jogadores.get(0));
                        System.out.println("Escolheu curar p1");
                    } else {
                        p2.curar(p2);
                        System.out.println("Escolheu curar p2");
                    }
                }
            }
        }

        input.close();
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
        if(personagem instanceof Suporte)
            System.out.printf("    c- heal\n");
    }

    public void movimentacao(Personagem personagem, char entrada) {
        Jogador jogador = (Jogador)personagem;
        if(entrada == 'U') {
            jogador.movimentar(tabuleiro, Direcao.CIMA);
        } else if(entrada == 'D') {
            jogador.movimentar(tabuleiro, Direcao.BAIXO);
        } else if(entrada == 'L') {
            jogador.movimentar(tabuleiro, Direcao.ESQUERDA);
        } else if(entrada == 'R') {
            jogador.movimentar(tabuleiro, Direcao.DIREITA);
        } 
    }

    public void iniciarTurno() {}

    public void imprimirTabuleiro() {

    }

    public void incrementaCiclo() {
        this.ciclos++;
    }
}
