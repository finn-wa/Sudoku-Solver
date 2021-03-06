package grid;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the three sudoku groups: row, column, and box.
 * Contains methods for determining properties of a given group.
 */
public class Group {

    public enum Type {ROW, COL, BOX}

    private final Type type;
    private final ArrayList<Cell> cells;

    Group(Type type, Cell[] cells) {
        this.type = type;
        this.cells = new ArrayList<>(Arrays.asList(cells));
        addGroupToCells();
    }

    /**
     * Loops through each and sets this Group as its row, column, or box
     */
    private void addGroupToCells() {
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

    /**
     * Eliminates candidate from every unsolved cell in group.
     * @param candidate: candidate to eliminate
     */
    public void eliminateCandidate(int candidate) {
        for(Cell cell : cells) {
            if(!cell.isSolved()) {
                cell.eliminateCandidate(candidate);
            }
        }
    }

    public ArrayList<Cell> getCells() {
        return this.cells;
    }
}
