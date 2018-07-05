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
    private ArrayList<Grid> grids;

    public Solver(File gridFile) {
        try {
            loadGrids(gridFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Solver solver = new Solver(new File("data/grids.txt"));
        solver.solveAll();
    }

    /**
     * Loads sudoku grids from text file
     * @param gridFile: File consisting of sudoku grids with one line for a name,
     *                then each row as a 9-digit long number on the following 9 lines.
     * @throws IOException: If file reading fails.
     */
    private void loadGrids(File gridFile) throws IOException {
        grids = new ArrayList<>();
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
    }

    public void solveAll() {
        int numSolved = 0;
        for(Grid grid : grids) {
            System.out.println(grid.getName());
            if(solve(grid)) numSolved++;
            System.out.println("====================");
        }
        System.out.println("Solved "+numSolved+"/"+grids.size()+" grids.");
    }

    public boolean solve(Grid grid) {
        int count = 1;
        while(!grid.isSolved()) {
            int numSolved = grid.getNumSolved();
            System.out.println(count + ". Solved cells: "+numSolved);
            // eliminate candidates
            basicElimination(grid);
            soleCandidate(grid);
            uniqueCandidate(grid);
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
     * Uses the basic rule of sudoku to eliminate potential candidates:
     * if a number is in the same row, column, or box as a given cell, then
     * the cell's value cannot be that number.
     * @param grid: The current grid being solved
     */
    private void basicElimination(Grid grid) {
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
     * Finds solved cells using the sole candidate rule:
     * When a cell only has one candidate, then it must be the solution.
     * This method loops through each unsolved cell in the grid and checks
     * for sole candidates.
     * @param grid: The current grid being solved
     */
    private void soleCandidate(Grid grid) {
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                Cell cell = grid.getCells()[row][col];
                if(!cell.isSolved() && cell.getNumCandidates() == 1) {
                    // find sole candidate
                    for(int i = 1; i <= 9; i++) {
                        if(cell.getCandidates()[i]) {
                            cell.setSolution(i);
                            basicElimination(grid);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Finds solved cells using the unique candidate rule:
     * If a number can only be put in one cell in a group, then that cell's
     * value is guaranteed to be that number. This method searches for candidates
     * that only exist in one cell in a group and solves those cells.
     * @param grid: The current grid being solved
     */
    private void uniqueCandidate(Grid grid) {
        for(Group[] category : grid.getAllGroups()) {
            for(Group group : category) {
                // key = candidate, value = list of cells with candidate
                HashMap<Integer, ArrayList<Cell>> occurrences = new HashMap<>();
                for(Cell cell : group.getCells()) {
                    if(cell.isSolved()) continue;
                    for(int candidate = 1; candidate <= 9; candidate++) {
                        if(cell.getCandidates()[candidate]) {
                            if(!occurrences.containsKey(candidate)) {
                                occurrences.put(candidate, new ArrayList<>());
                            }
                            occurrences.get(candidate).add(cell);
                        }
                    }
                }
                // check for single occurrences of candidates
                for(Integer candidate : occurrences.keySet()) {
                    if(occurrences.get(candidate).size() == 1) {
                        if(!occurrences.get(candidate).get(0).isSolved()) {
                            occurrences.get(candidate).get(0).setSolution(candidate);
                            basicElimination(grid);
                        }
                    }
                }
            }
        }
    }
}
