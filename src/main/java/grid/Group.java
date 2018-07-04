package grid;

/**
 * Represents the three sudoku groups: row, column, and box.
 * Contains methods for determining properties of a given group.
 */
public class Group {

    public enum Type{ROW, COL, BOX};

    public final Type type;
    public final Cell[] cells;

    public Group(Type type, Cell[] cells) {
        this.type = type;
        this.cells = cells;
    }

}
