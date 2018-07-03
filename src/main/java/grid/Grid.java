package grid;

public class Grid {
    private final Cell[][] cells; // 9x9 array of grid cells

    public Grid(int[][] values) {
        cells = new Cell[9][9];
        // convert integer array into array of Cells
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                cells[row][col] = new Cell(values[row][col]);
            }
        }
    }

    /**
     * Returns value in cell at row (1-9), col (1-9)
     * @param row: Row of cell to be returned
     * @param col: Column of cell to be returned
     * @return: Value of cell
     */
    public Cell get(int row, int col) {
        return cells[row][col];
    }

    public void print() {
        StringBuilder out = new StringBuilder();
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                out.append(cells[row][col]);
                out.append(' ');
            }
            out.append("\n");
        }
        System.out.println(out.toString());
    }
}
