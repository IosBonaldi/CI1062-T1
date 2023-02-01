import java.util.ArrayList;

public class Setor {
    private SetorTipos tipo;
    private Coordenada coordenada;
    private ArrayList<Construcao> construcoes;
    private ArrayList<Inimigo> inimigos;
    private boolean fonte;
    private boolean visitado;

    public Setor(Coordenada coordenada) {
        this.setTipo(gerarTipoSetor());
        this.setCoordenada(coordenada);
        this.setConstrucoes(gerarConstrucoesIniciais());
        this.setInimigos(new ArrayList<Inimigo>());
        this.setFonte(false);
        this.setVisitado(false);
        gerarInimigos();
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

    public void setPorta(Direcao direcao) {
        construcoes.set(direcao.ordinal(), Construcao.PORTA);
    }

    public void setInimigos(ArrayList<Inimigo> inimigos) {
        this.inimigos = inimigos;
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

    /**
     * 
     * @param posicao
     * @return Inimigo ou null(caso não haja inimigo na posição).
     */
    public Inimigo getInimigo(Integer posicao) {
        for (Inimigo inimigo : this.getInimigos()) {
            if (inimigo.getPosicao() == posicao) {
                return inimigo;
            }
        }
        return null;
    }

    /**
     * Gera setores com determinadas probabilidades.
     * 
     * @return Tipo do setor gerado.
     */
    public SetorTipos gerarTipoSetor() {
        int randomNumber = randomNumber(100, 0);
        if (randomNumber >= 40) {
            return SetorTipos.NORMAL;
        } else if (randomNumber >= 10 && randomNumber < 40) {
            return SetorTipos.OCULTO;
        } else {
            return SetorTipos.PRIVADO;
        }
    }

    /**
     * Atualização inicial do ArrayList de Construcao.
     * 
     * @return
     */
    public ArrayList<Construcao> gerarConstrucoesIniciais() {
        int qntParedes = 4;
        ArrayList<Construcao> construcoes = new ArrayList<Construcao>();
        for (int i = 0; i < qntParedes; i++) {
            construcoes.add(Construcao.PAREDE);
        }
        return construcoes;
    }

    /**
     * Para cada posição é gerado ou não um novo inimigo.
     */
    public void gerarInimigos() {
        for (int posicao = 0; posicao < 3; posicao++) {
            int inimigoNaPosicao = randomNumber(100, 0);
            if (inimigoNaPosicao <= 70) {
                int atkDef = randomNumber(3, 1);
                Inimigo inimigo = new Inimigo(atkDef, atkDef, posicao);
                this.getInimigos().add(inimigo);
            }
        }
    }

    /**
     * Método auxiliar para gerar números inteiros aleatórios.
     */
    public int randomNumber(int max, int min) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public boolean isThereEnemyAlife() {
        for(Inimigo i: this.getInimigos()) {
            if(i.isVivo())
                return true;
        }
        return false;
    }
}