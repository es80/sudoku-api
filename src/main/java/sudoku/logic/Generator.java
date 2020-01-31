package sudoku.logic;

import java.util.*;

public class Generator {

    private final Grid grid;
    private final int[][] squareSymmetries;
    private final int[][] rectangularSymmetries;

    public Generator(Grid grid) {
        // limit to  2x2, 2x3, 2x4, 3x3
        this.grid = grid;

        // generate reflections and rotations for more symmetrical looking puzzles
        squareSymmetries = new int[grid.getNumCells()][];
        rectangularSymmetries = new int[grid.getNumCells()][];
        for (int cellIndex = 0; cellIndex < grid.getNumCells(); cellIndex++) {
            int row = grid.getRowNumber(cellIndex);
            int col = grid.getColNumber(cellIndex);

            int diagonal1 = grid.getRowIndices()[grid.getNumDigits() - col - 1][grid.getNumDigits() - row - 1];
            int diagonal2 = grid.getRowIndices()[col][row];
            int leftRight = grid.getRowIndices()[row][grid.getNumDigits() - col - 1];
            int topBottom = grid.getRowIndices()[grid.getNumDigits() - row - 1][col];
            int rotate90  = grid.getRowIndices()[col][grid.getNumDigits() - row - 1];
            int rotate180 = grid.getRowIndices()[grid.getNumDigits() - row - 1][grid.getNumDigits() - col - 1];
            int rotate270 = grid.getRowIndices()[grid.getNumDigits() - col - 1][row];

            squareSymmetries[cellIndex] = new int[] {diagonal1, diagonal2, leftRight, topBottom, rotate90, rotate180, rotate270};
            rectangularSymmetries[cellIndex] = new int[] {rotate180, leftRight, topBottom};
        }
    }

    private int[] getRandomizedIndices() {
        Integer[] puzzleIndices = new Integer[grid.getNumCells()];
        for (int i = 0; i < puzzleIndices.length; i++) {
            puzzleIndices[i] = i;
        }
        List<Integer> puzzleIndicesList = Arrays.asList(puzzleIndices);
        Collections.shuffle(puzzleIndicesList);

        // create a list of indices starting with a random index then adding its rotations and
        // reflections, then repeat until all indices selected
        boolean[] seen = new boolean[grid.getNumCells()];
        Arrays.fill(seen, false);

        int[] puzzleIndicesArray = new int[grid.getNumCells()];
        int[][] symmetries;
        if (grid.getBoxHeight() == grid.getBoxWidth()) {
            symmetries = squareSymmetries;
        } else {
            symmetries = rectangularSymmetries;
        }
        int index = 0;
        for (int i : puzzleIndicesList) {
            if (!seen[i]) {
                puzzleIndicesArray[index++] = i;
                seen[i] = true;
                for (int j : symmetries[i]) {
                    if (!seen[j]) {
                        puzzleIndicesArray[index++] = j;
                        seen[j] = true;
                    }
                }
            }
        }
        return puzzleIndicesArray;
    }

    public Puzzle generatePuzzle(Difficulty targetDifficulty) {
        Puzzle puzzle;
        while (true) {
            // generate a random solved puzzle and remove clues one-by-one
            int[] puzzleIndices = getRandomizedIndices();
            int[] puzzleNums = (new Solver()).generateRandomSolution(grid);
            for (int i = 0; i < puzzleNums.length; i++) {
                int puzzleIndex = puzzleIndices[i];
                int existingClue = puzzleNums[puzzleIndex];
                puzzleNums[puzzleIndex] = 0;
                puzzle = new Puzzle(grid, puzzleNums);
                // if the puzzle now has multiple solutions, put the deleted clue back
                if (puzzle.getNumSolutions() > 1) {
                    puzzleNums[puzzleIndex] = existingClue;
                }
            }
            // we now have a 'minimal' valid puzzle, i.e. a puzzle with a single solution where
            // removal of any one clue would lead to multiple solutions
            puzzle = new Puzzle(grid, puzzleNums);
            if (puzzle.getDifficulty() == targetDifficulty) {
                return puzzle;
            } else if (targetDifficulty == Difficulty.UNRATED) {
                // target UNRATED if we don't care about the puzzle's difficulty
                return puzzle;
            }
        }
    }
}