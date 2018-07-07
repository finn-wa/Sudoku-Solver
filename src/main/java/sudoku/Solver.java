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
            ArrayList<Grid> grids = loadGrids(new File("data/grids.txt"));
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
            // eliminate candidates
            eliminateCandidates(grid);
            findSolutions(grid);
            if(grid.getNumSolved() <= numSolved || grid.getSolvingFailed()) { // no more cells solved
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
        basicElimination(grid);
        lockedCandidatesElimination(grid);
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
     * Uses the basic rule of sudoku to eliminate candidates:
     * if a number is in the same row, column, or box as a given cell, then
     * the cell's value cannot be that number.
     * @param grid: Grid from which candidates will be eliminated
     */
    public static void basicElimination(Grid grid) {
        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
                Cell cell = grid.getCells()[row][col];
                if(cell.isSolved()) {
                    continue;
                }
                Cell[] rowCells = cell.getRow().getCells();
                Cell[] colCells = cell.getCol().getCells();
                Cell[] boxCells = cell.getBox().getCells();
                for(int i = 0; i < 9; i++) {
                    if(rowCells[i].isSolved()) {
                        cell.eliminateCandidate(rowCells[i].getSolution());
                    }
                    if(colCells[i].isSolved()) {
                        cell.eliminateCandidate(colCells[i].getSolution());
                    }
                    if(boxCells[i].isSolved()) {
                        cell.eliminateCandidate(boxCells[i].getSolution());
                    }
                }
            }
        }
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
        //get subsets
        //if subset containsAll map values for a given candidate, then
        //eliminate candidates from row/col of subset
    }

    /**
     * Finds solved cells using the sole candidate rule:
     * When a cell only has one candidate, then it must be the solution.
     * This method loops through each unsolved cell in the grid and checks
     * for sole candidates.
     * @param grid: Grid in which to find solutions
     */
    public static void soleCandidateSolving(Grid grid) {
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                Cell cell = grid.getCells()[row][col];
                if(!cell.isSolved() && cell.getNumCandidates() == 1) {
                    cell.setSolution(cell.getCandidates().get(0));
                    eliminateCandidates(grid);
                }
            }
        }
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
                        eliminateCandidates(grid);
                    }
                }
            }
        }
    }

    /**
     * Note: ignores solved cells.
     * @param cells: Array of cells to search for candidates
     * @return: HashMap where each key is a candidate and each value is a list of
     * cells with the key as a candidate.
     */
    private static HashMap<Integer, ArrayList<Cell>> getCandidateCellMap(Cell[] cells) {
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
