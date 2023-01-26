import java.util.ArrayList;

public class Main{
    public static void main(String args[]){
        Jogador p1 = new Jogador(0, 0, false, 0, null);
        Suporte p2 = new Suporte(0, 0, false, 0, null);
        ArrayList<Jogador> p = new ArrayList<Jogador>();
        p.add(p1);
        p.add(p2);
        Tabuleiro t = new Tabuleiro(null);
        Setor s[][] = new Setor[5][5];
        for (int index = 0; index < 5; index++) {
            for (int i = 0; i < 5; i++) {
                s[index][i]=new Setor();
                s[index][i].setCoordenada(new Coordenada(index+1, i+1));
            }
        }
        p1.setSetor(s[2][2]);
        p2.setSetor(s[2][3]);
        s[1][1].setFonte(true);
        t.setSetores(s);
        System.out.println(t.strTabuleiro(p));
    }

}