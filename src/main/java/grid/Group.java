package grid;

/**
 * Represents the three sudoku groups: row, column, and box.
 * Contains methods for determining properties of a given group.
 */
public class Group {

    public enum Type {ROW, COL, BOX};

    private final Type type;
    private final Cell[] cells;

    public Group(Type type, Cell[] cells) {
        this.type = type;
        this.cells = cells;
        // set link back to group for each cell
        switch (type) {
            case ROW:
                for(Cell cell : cells) {
                    cell.setRow(this);
                }
            case COL:
                for(Cell cell : cells) {
                    cell.setCol(this);
                }
            case BOX:
                for(Cell cell : cells) {
                    cell.setBox(this);
                }
        }
    }

    public Cell[] getCells() {
        return cells;
    }
}
