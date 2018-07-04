package sudoku;

import grid.Cell;
import grid.Grid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Solver {
    public ArrayList<Grid> grids;

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
     * @param gridFile: File consisting of sudoku grids with one line for a title,
     *                then each row as a 9-digit long number on the following 9 lines.
     * @throws IOException: If file reading fails.
     */
    private void loadGrids(File gridFile) throws IOException {
        grids = new ArrayList<Grid>();
        Scanner scan = new Scanner(gridFile);
        while(scan.hasNext()) {
            int[][] cells = new int[9][9];
            scan.nextLine(); // scan grid title
            for(int row = 0; row < 9; row ++) {
                String line = scan.nextLine();
                for(int col = 0; col < 9; col++) {
                    cells[row][col] = Integer.parseInt(String.valueOf(line.charAt(col)));
                }
            }
            Grid grid = new Grid(cells);
            grids.add(grid);
        }
    }

    public void solveAll() {
        int numSolved = 0;
        int count = 1;
        for(Grid grid : grids) {
            System.out.println("====================");
            System.out.println("Puzzle "+count);
            if(solve(grid)) numSolved++;
            count++;
        }
        System.out.println("Solved "+numSolved+"/"+grids.size()+" grids.");
    }

    public boolean solve(Grid grid) {
        int count = 1;
        while(!grid.isSolved()) {
            int numSolved = grid.getNumSolved();
            System.out.println(count + ". Solved cells: "+numSolved);
            calculateCandidates(grid);
            soleCandidate(grid);
            if(!(grid.getNumSolved() > numSolved)) { // no more cells solved
                return false;
            }
            count++;
        }
        System.out.println(count + ". Solved cells: 81");
        System.out.println("Puzzle solved.");
        return true;
    }

    private void calculateCandidates(Grid grid) {
        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
                Cell cell = grid.cells[row][col];
                if(cell.isSolved()) {
                    continue;
                }
                boolean[] newCandidates = new boolean[10];
                for(int i = 0; i < 10; i++) {
                    newCandidates[i] = true;
                }
                Cell[] rowCells = cell.row.cells;
                Cell[] colCells = cell.col.cells;
                Cell[] boxCells = cell.box.cells;
                for(int i = 0; i < 9; i++) {
                    if(rowCells[i].isSolved()) {
                        newCandidates[rowCells[i].getSolution()] = false;
                    }
                    if(colCells[i].isSolved()) {
                        newCandidates[colCells[i].getSolution()] = false;
                    }
                    if(boxCells[i].isSolved()) {
                        newCandidates[boxCells[i].getSolution()] = false;
                    }
                }
                cell.setCandidates(newCandidates);
            }
        }
    }

    /**
     * When a cell only has one candidate, then it must be the solution.
     * This method loops through each unsolved cell in the grid and checks
     * for sole candidates.
     */
    private void soleCandidate(Grid grid) {
        for(int row = 0; row < 9; row ++) {
            for(int col = 0; col < 9; col++) {
                Cell cell = grid.cells[row][col];
                if(!cell.isSolved() && cell.getNumCandidates() == 1) {
                    // find sole candidate
                    for(int i = 1; i <= 9; i++) {
                        if(cell.getCandidates()[i]) {
                            cell.setSolution(i);
                            break;
                        }
                    }
                } else if(cell.getNumCandidates() == 0) {
                    System.out.println("Failed to solve puzzle");
                }
            }
        }
    }
}
