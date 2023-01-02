import java.util.ArrayList;

public class Jogo {
    private ArrayList<Jogador> jogadores;
    private Tabuleiro tabuleiro;
    private int turnos;
    private boolean ativo;

    public Jogo(ArrayList<Jogador> jogadores, Tabuleiro tabuleiro, int turnos, boolean ativo) {
        this.jogadores = jogadores;
        this.tabuleiro = tabuleiro;
        this.turnos = turnos;
        this.ativo = ativo;
    }

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

    public int getTurnos() {
        return turnos;
    }

    public void setTurnos(int turnos) {
        this.turnos = turnos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void iniciarJogo() {

    }

    public void chamarTurno(Personagem p) {

    }

    public void imprimirTabuleiro() {

    }
}
