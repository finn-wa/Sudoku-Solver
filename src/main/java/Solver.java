import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        new Solver(new File("data/grids.txt"));
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
            System.out.println(scan.nextLine()); // scan grid title
            for(int row = 0; row < 9; row ++) {
                String line = scan.nextLine();
                for(int col = 0; col < 9; col++) {
                    cells[row][col] = Integer.parseInt(""+line.charAt(col));
                }
            }
            Grid grid = new Grid(cells);
            grid.print();
            grids.add(grid);
        }
    }

}
