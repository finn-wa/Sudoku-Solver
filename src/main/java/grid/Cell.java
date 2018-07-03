package grid;

/**
 * Contains possible values for the cell in the format of a boolean array.
 * Each index represents a possible value. The 0 index is set to true if
 * the cell is unsolved, and false if it is solved (only one possible value).
 */
public class Cell {
    public boolean[] possibilities;

    public Cell(int value) {
        possibilities = new boolean[10];
        possibilities[value] = true;
    }
}
