import java.util.ArrayList;

public class Section {
    private SectionType type;
    private Coordinate coordinate;
    private ArrayList<Construction> constructions;
    private ArrayList<Enemy> enemies;
    private boolean source;
    private boolean visited;

    public Section(Coordinate coordinate) {
        this.setType(initializeSectionType());
        this.setCoordinate(coordinate);
        this.setConstructions(initializeConstructions());
        this.setEnemies(new ArrayList<Enemy>());
        this.setSource(false);
        this.setVisited(false);
        initializeEnemies();
    }

    public SectionType getType() {
        return type;
    }

    public void setType(SectionType type) {
        this.type = type;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public ArrayList<Construction> getConstructions() {
        return constructions;
    }

    public void setDoor(Direction direction) {
        constructions.set(direction.ordinal(), Construction.DOOR);
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void setConstructions(ArrayList<Construction> construcoes) {
        this.constructions = construcoes;
    }

    public boolean isADoor(Direction dir) {
        if(constructions.get(dir.ordinal()) == Construction.DOOR)
            return true;
        return false;
    }

    public boolean isSource() {
        return source;
    }

    public void setSource(boolean source) {
        this.source = source;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * 
     * @param position
     * @return Inimigo ou null(caso não haja inimigo na posição).
     */
    public Enemy getInimigo(Integer position) {
        for (Enemy enemy : this.getEnemies()) {
            if (enemy.getPosition() == position) {
                return enemy;
            }
        }
        return null;
    }

    /**
     * Gera setores com determinadas probabilidades.
     * 
     * @return Tipo do setor gerado.
     */
    public SectionType initializeSectionType() {
        int randomNumber = randomNumber(100, 0);
        if (randomNumber >= 40) {
            return SectionType.NORMAL;
        } else if (randomNumber >= 10 && randomNumber < 40) {
            return SectionType.HIDDEN;
        } else {
            return SectionType.PRIVATE;
        }
    }

    /**
     * Atualização inicial do ArrayList de Construcao.
     * 
     * @return
     */
    public ArrayList<Construction> initializeConstructions() {
        int wallsQuantity = 4;
        ArrayList<Construction> constructions = new ArrayList<Construction>();
        for (int i = 0; i < wallsQuantity; i++) {
            constructions.add(Construction.WALL);
        }
        return constructions;
    }

    /**
     * Para cada posição é gerado ou não um novo inimigo.
     */
    public void initializeEnemies() {
        for (int position = 0; position < 3; position++) {
            int enemyAtPosition = randomNumber(100, 0);
            if (enemyAtPosition <= 70) {
                int atkDef = randomNumber(3, 1);
                Enemy enemy = new Enemy(atkDef, atkDef, position);
                this.getEnemies().add(enemy);
            }
        }
    }

    /**
     * Verifica se há inimigo vivo.
     */
    public boolean existAnEnemyAlive() {
        for (Enemy e : this.getEnemies())
            if (e.isAlive())
                return true;
        return false;
    }

    public int countEnemiesAlive() {
        int counter = 0;
        for (Enemy e : this.getEnemies())
            if (e.isAlive())
                counter++;
        return counter;
    }

    /**
     * Método auxiliar para gerar números inteiros aleatórios.
     */
    public int randomNumber(int max, int min) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}