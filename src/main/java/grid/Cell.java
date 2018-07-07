package grid;

import java.util.ArrayList;

/**
 * Contains possible values for the cell in the format of a boolean array.
 * Each index represents a possible value for the cell if set to true.
 * The 0 index is set to true if the cell is unsolved, and false if it
 * is solved.
 */
public class Cell {
    private boolean[] candidates;
    private Group row;
    private Group col;
    private Group box;
    private Grid grid;

    public Cell(Grid grid, int value) {
        this.grid = grid;
        candidates = new boolean[10];
        // set all numbers to possible candidates
        for(int i = 0; i <= 9; i++) {
            candidates[i] = true;
        }
        if(value != 0) {
            setSolution(value);
        }
    }

    /**
     * Remove candidate from list of candidates (stored in field)
     * @param candidate: candidate to remove
     */
    public void eliminateCandidate(int candidate) {
        if(isSolved()) {
            throw new IllegalStateException("Cannot edit candidates of solved cell");
        }else if(candidate < 1 || 9 < candidate) {
            throw new IllegalArgumentException("Candidate must be a number between 1 and 9, candidate was "+candidate);
        };
        candidates[candidate] = false;
        if(getNumCandidates() == 0) {
            //System.err.println("All candidates eliminated.");
            grid.setSolvingFailed();
            throw new IllegalStateException("All candidates eliminated.");
        }
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

    public boolean isSolved() {
        return !candidates[0];
    }

    public void setSolution(int value) {
        if(isSolved()) {
            throw new IllegalStateException("Cannot change cell's solution once solved.");
        }else if(value < 1 || 9 < value) {
            throw new IllegalArgumentException("Solution must be a number between 1 and 9, value was "+value);
        }
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

    /**
     * @return ArrayList of candidates
     */
    public ArrayList<Integer> getCandidates() {
        ArrayList<Integer> candidateList = new ArrayList<>();
        for(int i = 1; i <= 9; i++) {
            if(candidates[i]){
                candidateList.add(i);
            }
        }
        return candidateList;
    }

    protected void setRow(Group row) {
        if(this.row != null) {
            throw new IllegalStateException("Row already set");
        }
        this.row = row;
    }

    protected void setCol(Group col) {
        if(this.col != null) {
            throw new IllegalStateException("Column already set");
        }
        this.col = col;
    }

    protected void setBox(Group box) {
        if(this.box != null) {
            throw new IllegalStateException("Box already set");
        }
        this.box = box;
    }

    public Group getRow() {
        return row;
    }

    public Group getCol() {
        return col;
    }

    public Group getBox() {
        return box;
    }
}
