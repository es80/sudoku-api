package sudoku.logic;

import java.util.Arrays;

/*
Puzzle stores the puzzle numbers, it's rating, difficulty and solution. Initial calls to
getSolution or getRating trigger solving or rating the puzzle.
 */
public class Puzzle {

    private final Grid grid;
    private final int[] puzzleNums;
    private int[] solution;
    private int numUnknowns = -1;
    private int numSolutions = -1;
    private int rating = -1;
    private Difficulty difficulty = Difficulty.UNRATED;

    public Puzzle(Grid grid, int[] puzzleNums) {
        this.grid = grid;
        this.puzzleNums = Arrays.copyOf(puzzleNums, grid.getNumCells());
    }

    public Grid getGrid() {
        return grid;
    }

    public int[] getPuzzleNums() {
        return puzzleNums;
    }

    public int getNumUnknowns() {
        if (numUnknowns == -1) {
            numUnknowns = 0;
            for (int digit : puzzleNums) {
                if (digit == 0) {
                    numUnknowns++;
                }
            }
        }
        return numUnknowns;
    }

    public int[] getSolution() {
        if (numSolutions == -1) {
            Solver solver = new Solver();
            numSolutions = solver.solve(this);
            if (numSolutions != 0) {
                solution = solver.getSolution();
            } else {
                solution = new int[grid.getNumCells()];
                Arrays.fill(solution, 0);
            }
        }
        return solution;
    }

    public int getNumSolutions() {
        if (numSolutions == -1) {
            getSolution();
        }
        return numSolutions;
    }

    public int getRating() {
        if (rating == -1) {
            rating = (new PuzzleRater()).rate(this);
        }
        return rating;
    }

    public Difficulty getDifficulty() {
        if (difficulty != Difficulty.UNRATED) {
            return difficulty;
        }

        if (getNumSolutions() != 1) {
            difficulty = Difficulty.INVALID;
            return difficulty;
        }

        int rating = getRating();

        // rating 0 and Difficulty.TOO_DIFFICULT means the rater failed to
        // solve the puzzle, but it is still a valid puzzle.
        if (rating == 0) {
            difficulty = Difficulty.TOO_DIFFICULT;
            return difficulty;
        }

        // In what follows the magic numbers come using statistics gathered when
        // generating puzzles. The aim is to ensure a reasonable split between
        // difficulty levels and a reasonable time taken to generate a puzzle at
        // each difficulty.

        // All 2x2 puzzles are easy.
        if (grid.getNumDigits() == 4) {
            difficulty = Difficulty.EASY;
            return difficulty;
        }

        // No 2x3 puzzle is fiendish.
        if (grid.getNumDigits() == 6) {
            if (rating == 1) {
                difficulty = Difficulty.EASY;
            } else if (rating == 2) {
                difficulty = Difficulty.MEDIUM;
            } else {
                difficulty = Difficulty.TRICKY;
            }
            return difficulty;
        }

        // Cases 2x4, 2x5 and 3x3 are similar:
        if (rating <= 2) {
            difficulty = Difficulty.EASY;
        } else if (rating == 3) {
            difficulty = Difficulty.MEDIUM;
        } else if (rating == 4) {
            if ((grid.getNumDigits() == 8 && getNumUnknowns() < 46) ||
                    (grid.getNumDigits() == 9 && getNumUnknowns() < 58) ||
                    (grid.getNumDigits() == 10 && getNumUnknowns() < 68)) {
                difficulty = Difficulty.TRICKY;
            } else {
                difficulty = Difficulty.FIENDISH;
            }
        } else {
            difficulty = Difficulty.FIENDISH;
        }
        return difficulty;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Puzzle:\n");
        result.append(PuzzlePrinter.valuesArrayToString(grid, puzzleNums));
        result.append("\n");
        if (numSolutions == -1) {
            result.append("This puzzle has not yet been solved.\n");
        } else if (numSolutions > 1) {
            if (numSolutions >= Solver.MAX_SOLUTIONS) {
                result.append("This puzzle has at least ").append(Solver.MAX_SOLUTIONS).append(" solutions.\n");
            } else {
                result.append("This puzzle has ").append(numSolutions).append(" solutions.\n");
            }
            result.append("One possible solution:\n");
            result.append(PuzzlePrinter.valuesArrayToString(grid, solution));
        } else if (numSolutions == 1) {
            result.append("This puzzle has a single solution:\n");
            result.append(PuzzlePrinter.valuesArrayToString(grid, solution));
        } else {
            result.append("The puzzle has no solutions.\n");
        }
        result.append("This puzzle is rated: ").append(difficulty).append(".\n");
        return result.toString();
    }
}