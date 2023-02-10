public class Coordinate {
    private int row;
    private int column;

    public Coordinate(int row, int column) {
        this.setRow(row);
        this.setColumn(column);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        if (row < 0)
            throw new IllegalArgumentException();
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        if (column < 0)
            throw new IllegalArgumentException();
        this.column = column;
    }
}