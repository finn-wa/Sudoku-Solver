package grid;

/**
 * Contains possible values for the cell in the format of a boolean array.
 * Each index represents a possible value. The 0 index is set to true if
 * the cell is unsolved, and false if it is solved (only one possible value).
 */
public class Cell {
    public boolean[] candidates;

    public Cell(int value) {
        candidates = new boolean[10];
        candidates[value] = true;
    }

    public void solve(int value) {
        for(int index = 0; index <= 9; index++) {
            if(index == value) {
                candidates[index] = true;
            } else {
                candidates[index] = false;
            }
        }
    }

    public boolean isSolved() {
        return !candidates[0];
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

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append('[');
        for(int index = 0; index <= 9; index++) {
            if(candidates[index]) {
                out.append(index);
                out.append(',');
            }
        }
        // delete last comma
        if(out.length() > 2) {
            out.deleteCharAt(out.length() - 1);
        }
        return out.toString();
    }
}
