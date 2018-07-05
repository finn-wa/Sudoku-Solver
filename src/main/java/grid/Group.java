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
        if(type == Type.ROW) {
            for (Cell cell : cells) {
                cell.setRow(this);
            }
        }else if(type == Type.COL) {
            for (Cell cell : cells) {
                cell.setCol(this);
            }
        }else if(type == Type.BOX) {
            for (Cell cell : cells) {
                cell.setBox(this);
            }
        }
    }

    public Cell[] getCells() {
        return cells;
    }
}
