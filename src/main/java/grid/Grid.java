package grid;

public class Grid {
    public Cell[][] cells; // 9x9 array of grid cells
    public Group[] rows;
    public Group[] cols;
    public Group[] boxes;
    private int numSolved;

    public Grid(int[][] values) {
        numSolved = 0;
        generateCells(values);
        generateGroups();
    }

    private void generateCells(int[][] values) {
        cells = new Cell[9][9];
        // convert integer array into array of Cells
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                cells[row][col] = new Cell(this, values[row][col]);
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
        int index = 0;
        for(int col = 0; col < 9; col += 3) {
            for(int row = 0; row < 9; row += 3) {
                Cell[] box = new Cell[9];
                int boxIndex = 0;
                for(int colRow = 0; colRow < 3; colRow++) {
                    for(int boxRow = 0; boxRow < 3; boxRow++) {
                        box[boxIndex] = cells[row+boxRow][col+colRow];
                        boxIndex++;
                    }
                }
                boxes[index] = new Group(Group.Type.BOX, box);
                index++;
            }
        }
    }

    public void printGroups() {
        StringBuilder out = new StringBuilder();
        out.append("ROWS\n");
        for(Group row : rows) {
            for(Cell cell : row.cells) {
                out.append(cell);
            }
            out.append('\n');
        }
        out.append("COLS\n");
        for(Group col : cols) {
            for(Cell cell : col.cells) {
                out.append(cell);
            }
            out.append('\n');
        }
        out.append("BOXES\n");
        for(Group box : boxes) {
            for(Cell cell : box.cells) {
                out.append(cell);
            }
            out.append('\n');
        }
        System.out.println(out);
    }

    public boolean isSolved() {
        return numSolved == 81;
    }

    public int getNumSolved() {
        return this.numSolved;
    }

    public void incrementNumSolved() {
        if(numSolved >= 81) {
            throw new IllegalStateException("Can't solve more than 81 cells");
        }
        numSolved++;
    }

    public void print() {
        StringBuilder out = new StringBuilder();
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                out.append(cells[row][col]);
            }
            out.append("\n");
        }
        System.out.println(out.toString());
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

}
