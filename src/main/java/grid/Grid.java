package grid;

public class Grid {
    public Cell[][] cells; // 9x9 array of grid cells
    public Group[] rows;
    public Group[] cols;
    public Group[] boxes;

    public Grid(int[][] values) {
        generateCells(values);
        generateGroups();
    }

    private void generateCells(int[][] values) {
        cells = new Cell[9][9];
        // convert integer array into array of Cells
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                cells[row][col] = new Cell(values[row][col]);
            }
        }
    }

    private void generateGroups() {
        rows = new Group[9];
        for(int i = 0; i < 9; i++) {
            rows[i] = new Group(Group.Type.ROW, cells[i]);
        }
        cols = new Group[9];
        for(int col = 0; col < 9; col++) {
            // build new array to hold column
            Cell[] column = new Cell[9];
            for(int row = 0; row < 9; row++) {
                column[row] = cells[row][col];
            }
            cols[col] = new Group(Group.Type.COL, column);
        }
        boxes = new Group[9];
        Cell[] box = new Cell[9];
        for(int col = 0; col < 9; col += 3) {
            for(int row = 0; row < 9; row += 3) {
                int index = 0;
               // box[index] = row
                        // christ alive
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
