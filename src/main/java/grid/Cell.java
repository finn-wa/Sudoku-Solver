package grid;

import java.sql.SQLOutput;

/**
 * Contains possible values for the cell in the format of a boolean array.
 * Each index represents a possible value for the cell if set to true.
 * The 0 index is set to true if the cell is unsolved, and false if it
 * is solved.
 */
public class Cell {
    private boolean[] candidates;
    public Group row;
    public Group col;
    public Group box;
    private Grid grid;

    public Cell(Grid grid, int value) {
        this.grid = grid;
        candidates = new boolean[10];
        if(value == 0) { // not solved
            // set all numbers to possible candidates
            for(int i = 0; i <= 9; i++) {
                candidates[i] = true;
            }
        } else {
            setSolution(value);
        }
    }

    protected void setRow(Group row) {
        this.row = row;
    }

    protected void setCol(Group col) {
        this.col = col;
    }

    protected void setBox(Group box) {
        this.box = box;
    }

    public boolean isSolved() {
        return !candidates[0];
    }

    public void setSolution(int value) {
        for(int index = 0; index <= 9; index++) {
            // set all candidates to false bar the solution
            candidates[index] = (index == value);
        }
        grid.incrementNumSolved();
    }

    /**
     * @return Solution if solved, 0 if not.
     */
    public int getSolution() {
        for(int i = 0; i <= 9; i++) {
            if(candidates[i]) {
                return i;
            }
        }
        throw new IllegalStateException("Cell cannot be solved with no candidates");
    }

    public int getNumCandidates() {
        if(isSolved()) {
            return 1;
        }
        int count = 0;
        for(int i = 1; i <= 9; i++) {
            if(candidates[i]) {
                count++;
            }
        }
        return count;
    }

    public boolean[] getCandidates() {
        return this.candidates;
    }

    public void setCandidates(boolean[] newCandidates) {
        if(newCandidates.length != 10) {
            throw new IllegalArgumentException("Length of candidates array must be 10");
        }
        this.candidates = newCandidates;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append('[');
        for(int index = 1; index <= 9; index++) {
            if(candidates[index]) {
                out.append(index);
                out.append(',');
            }
        }
        // delete last comma
        if(out.length() > 1) {
            out.deleteCharAt(out.length() - 1);
        }
        out.append(']');
        return out.toString();
    }
}
