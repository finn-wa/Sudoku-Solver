package grid;

public class Grid {
    private final String name;
    private Cell[][] cells; // 9x9 array of grid cells
    private Group[] rows;
    private Group[] cols;
    private Group[] boxes;
    private Group[][] allGroups; // list containing rows, cols, boxes
    private int numSolved;
    private boolean solvingFailed;

    public Grid(String name, int[][] values) {
        this.name = name;
        numSolved = 0;
        generateCells(values);
        generateGroups();
        solvingFailed = false;
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
            Cell[] colGroup = new Cell[9];
            for(int row = 0; row < 9; row++) {
                colGroup[row] = cells[row][col];
            }
            cols[col] = new Group(Group.Type.COL, colGroup);
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
        allGroups = new Group[][]{rows, cols, boxes};
    }

    public void printGroups() {
        StringBuilder out = new StringBuilder();
        out.append("ROWS\n");
        for(Group row : rows) {
            for(Cell cell : row.getCells()) {
                out.append(cell);
            }
            out.append('\n');
        }
        out.append("COLS\n");
        for(Group col : cols) {
            for(Cell cell : col.getCells()) {
                out.append(cell);
            }
            out.append('\n');
        }
        out.append("BOXES\n");
        for(Group box : boxes) {
            for(Cell cell : box.getCells()) {
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

    protected void setSolvingFailed() {
        this.solvingFailed = true;
    }

    public boolean getSolvingFailed() {
        return this.solvingFailed;
    }

    public void print() {
        StringBuilder out = new StringBuilder();
        out.append(name);
        out.append('\n');
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                out.append(cells[row][col]);
            }
            out.append("\n");
        }
        System.out.println(out.toString());
    }

    public String getName() {
        return name;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Group[] getRows() {
        return rows;
    }

    public Group[] getCols() {
        return cols;
    }

    public Group[] getBoxes() {
        return boxes;
    }

    public Group[][] getAllGroups() {
        return allGroups;
    }
}
