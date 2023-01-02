import java.util.ArrayList;

public class Setor {
    private SetorTipos tipo;
    private Coordenada coordenada;
    private Paredes[] paredes;
    private ArrayList<Inimigo> inimigos;
    private boolean fonte;
    private boolean visitado;

    public Setor(SetorTipos tipo, Coordenada coordenada, Paredes[] paredes, ArrayList<Inimigo> inimigos, boolean fonte,
            boolean visitado) {
        this.tipo = tipo;
        this.coordenada = coordenada;
        this.paredes = paredes;
        this.inimigos = inimigos;
        this.fonte = fonte;
        this.visitado = visitado;
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
