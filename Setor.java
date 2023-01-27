import java.util.ArrayList;

public class Setor {
    private SetorTipos tipo;
    private Coordenada coordenada;
    private ArrayList<Construcao> construcoes;
    private ArrayList<Inimigo> inimigos;
    private boolean fonte;
    private boolean visitado;

    public Setor(Coordenada coordenada) {
        this.tipo = gerarTipoSetor();
        this.coordenada = coordenada;
        this.construcoes = gerarConstrucoesIniciais();
        this.inimigos = new ArrayList<>();
        this.fonte = false;
        this.visitado = false;
    }

    public SetorTipos getTipo() {
        return tipo;
    }

    public void setTipo(SetorTipos tipo) {
        this.tipo = tipo;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public ArrayList<Construcao> getConstrucoes() {
        return construcoes;
    }

    public void setConstrucoes(ArrayList<Construcao> construcoes) {
        this.construcoes = construcoes;
    }

    public boolean isFonte() {
        return fonte;
    }

    public void setFonte(boolean fonte) {
        this.fonte = fonte;
    }
    
    public boolean isVisitado() {
        return visitado;
    }
    
    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }
    
    public ArrayList<Inimigo> getInimigos() {
        return inimigos;
    }

    public Inimigo getInimigo(Integer posicao) {
        for (Inimigo inimigo : inimigos) {
            if(inimigo.getPosicao() == posicao){
                return inimigo;
            }
        }
        return null;
    }
    
    public SetorTipos gerarTipoSetor(){
        int randomNumber = randomNumber(100, 0);
        if(randomNumber >= 40){
            return SetorTipos.NORMAL;
        }else if(randomNumber >= 10 && randomNumber < 40){
            return SetorTipos.OCULTO;
        }else{
            return SetorTipos.PRIVADO;
        }
    }

    public ArrayList<Construcao> gerarConstrucoesIniciais(){
        int qntParedes = 4;
        ArrayList<Construcao> construcoes = new ArrayList<>();
        for(int i = 0; i < qntParedes; i++){
            construcoes.add(Construcao.PAREDE);
        }
        return construcoes;
    }

    public void gerarInimigos() {
        for(int posicao = 0; posicao < 3; posicao++){
            int inimigoNaPosicao = randomNumber(100, 0);
            if(inimigoNaPosicao <= 60){
                int atkDef = randomNumber(3,1);
                Inimigo inimigo = new Inimigo(atkDef, atkDef, true, posicao);
                this.inimigos.add(inimigo);
            }
        }
    }

    public int randomNumber(int max, int min){
        return (int) (Math.random() * (max-min+1) + min);
    }
}
