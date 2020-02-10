package mazeMapping;

public enum Direction {
    DOWN(9, 1, 0),
    LEFT(20, 0, -1),
    RIGHT(32, 0, 1),
    UP(41, -1, 0);

    private int rowTranslation;
    private int colTranslation;
    private int cellPos;
    private byte possibility;

    Direction(int cellPos, int rowTranslation, int colTranslation) {
        this.cellPos = cellPos;
        this.rowTranslation = rowTranslation;
        this.colTranslation = colTranslation;
    }

    public void setPossibility(byte possibility) {
        this.possibility = possibility;
    }

    public int getCellId() {
        return cellPos;
    }

    public byte getPossibility() {
        return possibility;
    }

    public int getColTranslation() {
        return colTranslation;
    }

    public int getRowTranslation() {
        return rowTranslation;
    }
}
