package sudoku;

import grid.Cell;
import grid.Grid;
import grid.Group;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Solver {

    public static void main(String[] args) {
        try {
            ArrayList<Grid> grids = loadGrids(new File("data/grid1.txt"));
            solveAll(grids);
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Loads sudoku grids from text file
     * @param gridFile: File consisting of sudoku grids with one line for a name,
     *                then each row as a 9-digit long number on the following 9 lines.
     * @throws IOException: If file reading fails.
     */
    public static ArrayList<Grid> loadGrids(File gridFile) throws IOException {
        ArrayList<Grid> grids = new ArrayList<>();
        Scanner scan = new Scanner(gridFile);
        while(scan.hasNext()) {
            int[][] cells = new int[9][9];
            String name = scan.nextLine();
            for(int row = 0; row < 9; row ++) {
                String line = scan.nextLine();
                for(int col = 0; col < 9; col++) {
                    cells[row][col] = Integer.parseInt(String.valueOf(line.charAt(col)));
                }
            }
            Grid grid = new Grid(name, cells);
            grids.add(grid);
        }
        return grids;
    }

    /**
     * Calls solve() on all grids in list
     * @param grids: List of grids to solve
     */
    public static void solveAll(ArrayList<Grid> grids) {
        int numSolved = 0;
        for(Grid grid : grids) {
            System.out.println(grid.getName());
            if(solve(grid)) numSolved++;
            System.out.println("====================");
        }
        System.out.println("Solved "+numSolved+"/"+grids.size()+" grids.");
    }

    /**
     * @param grid: Grid which method attempts to solve.
     * @return true if method solves grid, false if method fails to solve grid
     */
    public static boolean solve(Grid grid) {
        int count = 1;
        while(!grid.isSolved()) {
            int numSolved = grid.getNumSolved();
            System.out.println(count + ". Solved cells: "+numSolved);
            eliminateCandidates(grid);
            findSolutions(grid);
            if(grid.getNumSolved() <= numSolved || grid.getSolvingFailed()) {
                // no cells solved in last loop, cannot progress from here
                System.out.println("Cannot solve sudoku.");
                return false;
            }
            count++;
        }
        System.out.println(count + ". Solved cells: 81");
        System.out.println("Sudoku solved.");
        return true;
    }

    /**
     * Calls each candidate elimination method in turn.
     * @param grid: Grid from which candidates will be eliminated
     */
    public static void eliminateCandidates(Grid grid) {
        //lockedCandidatesElimination(grid);
    }

    /**
     * Calls each solution-finding method in turn.
     * @param grid: Grid in which to find solutions
     */
    public static void findSolutions(Grid grid) {
        soleCandidateSolving(grid);
        uniqueCandidateSolving(grid);
    }

    /**
     * Uses the locked candidates rule to eliminate candidates:
     * Sometimes a candidate within a box is restricted to one row or column.
     * Since one of these cells must contain that specific candidate, the
     * candidate can safely be excluded from the remaining cells in that
     * row or column outside of the box.
     * @param grid: Grid from which candidates will be eliminated
     */
    public static void lockedCandidatesElimination(Grid grid) {
        //get intersections
        //if intersection containsAll map values for a given candidate, then
        //eliminate candidates from row/col of intersections

        // box candidate elimination
        Group[] boxes = grid.getBoxes();
        // get intersection of boxes with rows
        for(Group box : boxes) {
            HashMap<Integer, ArrayList<Cell>> map = getCandidateCellMap(box.getCells());
            Group[] boxRows = new Group[]{
                    box.getCells().get(0).getRow(),
                    box.getCells().get(3).getRow(),
                    box.getCells().get(6).getRow()};
            for(Group row : boxRows) {
                ArrayList<Cell> intersection = new ArrayList<>(box.getCells());
                intersection.retainAll(row.getCells());
                for(Integer candidate : map.keySet()) {
                    if(intersection.containsAll(map.get(candidate))) {
                        // TODO: remove candidates from row: make method in Group that does this
                        // TODO:
                        // also make a method in cell that calls eliminateCandidate(solution) on each
                        // cell returned by getRow, getCol, and getBox. then soleCandidateSolving
                        // won't have to be called every time a cell is solved
                    }
                }
            }
        }
    }

    /**
     * Finds solved cells using the sole candidate rule:
     * When a cell only has one candidate, then it must be the solution.
     * This method loops through each unsolved cell in the grid and checks
     * for sole candidates. It will continue looking for sole candidates to
     * solve until there are none left.
     * @param grid: Grid in which to find solutions
     */
    public static void soleCandidateSolving(Grid grid) {
        boolean cellsSolved;
        do {
            cellsSolved = false;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    Cell cell = grid.getCells()[row][col];
                    if (!cell.isSolved() && cell.getNumCandidates() == 1) {
                        cell.setSolution(cell.getCandidates().get(0));
                        cellsSolved = true;
                    }
                }
            }
        } while (cellsSolved);
    }

    /**
     * Finds solved cells using the unique candidate rule:
     * If a number can only be put in one cell in a group, then that cell's
     * value is guaranteed to be that number. This method searches for candidates
     * that only exist in one cell in a group and solves those cells.
     * @param grid: Grid in which to find solutions
     */
    public static void uniqueCandidateSolving(Grid grid) {
        for(Group[] category : grid.getAllGroups()) {
            for(Group group : category) {
                // key = candidate, value = list of cells with candidate
                HashMap<Integer, ArrayList<Cell>> map = getCandidateCellMap(group.getCells());
                // check for single occurrences of candidates
                for(Integer candidate : map.keySet()) {
                    if(map.get(candidate).size() == 1) {
                        map.get(candidate).get(0).setSolution(candidate);
                    }
                }
            }
        }
    }

    /**
     * Note: ignores solved cells.
     * @param cells: ArrayList of cells to search for candidates
     * @return: HashMap where each key is a candidate and each value is a list of
     * cells with the key as a candidate.
     */
    private static HashMap<Integer, ArrayList<Cell>> getCandidateCellMap(ArrayList<Cell> cells) {
        HashMap<Integer, ArrayList<Cell>> map = new HashMap<>();
        for(Cell cell : cells) {
            if(cell.isSolved()) continue;
            for(Integer candidate : cell.getCandidates()) {
                if(!map.containsKey(candidate)) {
                    map.put(candidate, new ArrayList<>());
                }
                map.get(candidate).add(cell);
            }
        }
        return map;
    }
}
