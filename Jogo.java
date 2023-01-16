import java.util.ArrayList;

public class Jogo {
    private ArrayList<Jogador> jogadores;
    private Tabuleiro tabuleiro;
    private int turnos;
    private boolean ativo;
    private LogHandler log;

    public Jogo(ArrayList<Jogador> jogadores, Tabuleiro tabuleiro, int turnos, boolean ativo) {
        this.jogadores = jogadores;
        this.tabuleiro = tabuleiro;
        this.turnos = turnos;
        this.ativo = ativo;
        this.log = new LogHandler("./log.txt");
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
    
    public LogHandler getLog() {
        return log;
    }

    public void iniciarJogo() {
        // Início do jogo (verifica existência do log para criar ou não);
        log.createLogFile();
    }

    public void chamarTurno(Personagem p) {
        // Quando chegar no fim do jogo (atualiza arquivo de log);
        log.logFileManipulation(this.pontuacaoFinal());
    }

    public void imprimirTabuleiro() {

    }

    // Quando finalizar o jogo, chama-se este método para
    // calcular a pontuação final (pontos principal + pontos suporte);
    public Integer pontuacaoFinal(){
        Integer pontuacaoFinal = 0;
        for (Jogador jogador : jogadores) {
            pontuacaoFinal += jogador.pontuacao;
        }
        return pontuacaoFinal;
    }

}
