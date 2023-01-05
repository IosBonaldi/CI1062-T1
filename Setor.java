import java.util.ArrayList;

public class Setor {
    private SetorTipos tipo;
    private Coordenada coordenada;
    private Paredes[] paredes;
    private boolean[] portas;
    private ArrayList<Inimigo> inimigos;
    private boolean fonte;
    private boolean visitado;

    public Setor(SetorTipos tipo, boolean fonte) {
        this.tipo = tipo;
        this.fonte = fonte;
        this.portas = new boolean[4];
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

    public Paredes[] getParedes() {
        return paredes;
    }

    public void setParedes(Paredes[] paredes) {
        this.paredes = paredes;
    }

    public void setPorta(int indexPorta, boolean aberta){
        this.portas[indexPorta] = aberta;
    }

    public boolean isAberta(int indexPorta){
        return this.portas[indexPorta];
    }

    public ArrayList<Inimigo> getInimigos() {
        return inimigos;
    }

    public void setInimigos(ArrayList<Inimigo> inimigos) {
        this.inimigos = inimigos;
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

    private void gerarInimigos() {

    }

    public Inimigo getInimigo(int i) {
        return null;
    }
}
