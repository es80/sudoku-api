package sudoku.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestPuzzleRater3By3 {

    private Puzzle puzzle;
    private final int BOX_HEIGHT = 3;
    private final int BOX_WIDTH = 3;
    private final Grid grid = new Grid(BOX_HEIGHT, BOX_WIDTH);
    private final PuzzleRater rater = new PuzzleRater();

    /*
    Most test cases sourced from http://sudopedia.enjoysudoku.com/Test_Cases.html
    some from http://norvig.com/sudoku.html
    */

    /*
    Valid puzzles which the rater can solve and rate.
    */

    @Test
    public void alreadySolved() {
        int[] alreadySolved = {9, 7, 4, 2, 3, 6, 1, 5, 8, 6, 3, 8, 5, 9, 1, 7, 4, 2, 1, 2, 5, 4, 8, 7, 9, 3, 6, 3, 1, 6, 7, 5, 4, 2, 8, 9, 7, 4, 2, 9, 1, 8, 5, 6, 3, 5, 8, 9, 3, 6, 2, 4, 1, 7, 8, 6, 7, 1, 2, 5, 3, 9, 4, 2, 5, 3, 6, 4, 9, 8, 7, 1, 4, 9, 1, 8, 7, 3, 6, 2, 5};

        puzzle = new Puzzle(grid, alreadySolved);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();

        assertEquals(1, difficulty);
        assertArrayEquals(alreadySolved, solution);
    }

    @Test
    public void singleEmptyCell() {
        int[] singleEmptyCell = {2, 5, 6, 4, 8, 9, 1, 7, 3, 3, 7, 4, 6, 1, 5, 9, 8, 2, 9, 8, 1, 7, 2, 3, 4, 5, 6, 5, 9, 3, 2, 7, 4, 8, 6, 1, 7, 1, 2, 8, 0, 6, 5, 4, 9, 4, 6, 8, 5, 9, 1, 3, 2, 7, 6, 3, 5, 1, 4, 7, 2, 9, 8, 1, 2, 7, 9, 5, 8, 6, 3, 4, 8, 4, 9, 3, 6, 2, 7, 1, 5};
        int[] expected = {2, 5, 6, 4, 8, 9, 1, 7, 3, 3, 7, 4, 6, 1, 5, 9, 8, 2, 9, 8, 1, 7, 2, 3, 4, 5, 6, 5, 9, 3, 2, 7, 4, 8, 6, 1, 7, 1, 2, 8, 3, 6, 5, 4, 9, 4, 6, 8, 5, 9, 1, 3, 2, 7, 6, 3, 5, 1, 4, 7, 2, 9, 8, 1, 2, 7, 9, 5, 8, 6, 3, 4, 8, 4, 9, 3, 6, 2, 7, 1, 5};

        puzzle = new Puzzle(grid, singleEmptyCell);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();
        // Methods used:
        // Last Digit

        assertEquals(1, difficulty);
        assertArrayEquals(expected, solution);
    }

    @Test
    public void lastDigitInABlock() {
        int[] lastDigitInABlock = {3, 0, 5, 4, 2, 0, 8, 1, 0, 4, 8, 7, 9, 0, 1, 5, 0, 6, 0, 2, 9, 0, 5, 6, 3, 7, 4, 8, 5, 0, 7, 9, 3, 0, 4, 1, 6, 1, 3, 2, 0, 8, 9, 5, 7, 0, 7, 4, 0, 6, 5, 2, 8, 0, 2, 4, 1, 3, 0, 9, 0, 6, 5, 5, 0, 8, 6, 7, 0, 1, 9, 2, 0, 9, 6, 5, 1, 2, 4, 0, 8};
        int[] expected = {3, 6, 5, 4, 2, 7, 8, 1, 9, 4, 8, 7, 9, 3, 1, 5, 2, 6, 1, 2, 9, 8, 5, 6, 3, 7, 4, 8, 5, 2, 7, 9, 3, 6, 4, 1, 6, 1, 3, 2, 4, 8, 9, 5, 7, 9, 7, 4, 1, 6, 5, 2, 8, 3, 2, 4, 1, 3, 8, 9, 7, 6, 5, 5, 3, 8, 6, 7, 4, 1, 9, 2, 7, 9, 6, 5, 1, 2, 4, 3, 8};

        puzzle = new Puzzle(grid, lastDigitInABlock);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();
        // Methods used:
        // Last Digit

        assertEquals(1, difficulty);
        assertArrayEquals(expected, solution);
    }

    @Test
    public void soleLocationInABox() {
        int[] soleLocationInABox = {0, 0, 3, 0, 2, 0, 6, 0, 0, 9, 0, 0, 3, 0, 5, 0, 0, 1, 0, 0, 1, 8, 0, 6, 4, 0, 0, 0, 0, 8, 1, 0, 2, 9, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 6, 7, 0, 8, 2, 0, 0, 0, 0, 2, 6, 0, 9, 5, 0, 0, 8, 0, 0, 2, 0, 3, 0, 0, 9, 0, 0, 5, 0, 1, 0, 3, 0, 0};
        int[] expected = {4, 8, 3, 9, 2, 1, 6, 5, 7, 9, 6, 7, 3, 4, 5, 8, 2, 1, 2, 5, 1, 8, 7, 6, 4, 9, 3, 5, 4, 8, 1, 3, 2, 9, 7, 6, 7, 2, 9, 5, 6, 4, 1, 3, 8, 1, 3, 6, 7, 9, 8, 2, 4, 5, 3, 7, 2, 6, 8, 9, 5, 1, 4, 8, 1, 4, 2, 5, 3, 7, 6, 9, 6, 9, 5, 4, 1, 7, 3, 8, 2};

        puzzle = new Puzzle(grid, soleLocationInABox);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();
        // Methods used:
        // Last Digit
        // Sole Location in a Box

        assertEquals(1, difficulty);
        assertArrayEquals(expected, solution);
    }

    @Test
    public void soleLocationInABlock() {
        int[] soleLocationInABlock = {0, 0, 2, 0, 3, 0, 0, 0, 8, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 3, 1, 0, 2, 0, 0, 0, 0, 0, 6, 0, 0, 5, 0, 2, 7, 0, 0, 1, 0, 0, 0, 0, 0, 5, 0, 2, 0, 4, 0, 6, 0, 0, 3, 1, 0, 0, 0, 0, 8, 0, 6, 0, 5, 0, 0, 0, 0, 0, 0, 0, 1, 3, 0, 0, 5, 3, 1, 0, 4, 0, 0};
        int[] expected = {6, 7, 2, 4, 3, 5, 1, 9, 8, 5, 4, 9, 1, 7, 8, 3, 6, 2, 8, 3, 1, 6, 2, 9, 5, 4, 7, 3, 6, 8, 9, 5, 1, 2, 7, 4, 9, 1, 7, 2, 4, 3, 8, 5, 6, 2, 5, 4, 8, 6, 7, 9, 3, 1, 1, 9, 3, 7, 8, 4, 6, 2, 5, 4, 8, 6, 5, 9, 2, 7, 1, 3, 7, 2, 5, 3, 1, 6, 4, 8, 9};

        puzzle = new Puzzle(grid, soleLocationInABlock);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();
        // Methods used:
        // Last Digit
        // Sole Location in a Box
        // Sole Location in a Row/Column

        assertEquals(2, difficulty);
        assertArrayEquals(expected, solution);
    }

    @Test
    public void soleDigit() {
        int[] soleDigit = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 8, 5, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 5, 0, 7, 0, 0, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 7, 3, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 9};
        int[] expected = {9, 8, 7, 6, 5, 4, 3, 2, 1, 2, 4, 6, 1, 7, 3, 9, 8, 5, 3, 5, 1, 9, 2, 8, 7, 4, 6, 1, 2, 8, 5, 3, 7, 6, 9, 4, 6, 3, 4, 8, 9, 2, 1, 5, 7, 7, 9, 5, 4, 6, 1, 8, 3, 2, 5, 1, 9, 2, 8, 6, 4, 7, 3, 4, 7, 2, 3, 1, 9, 5, 6, 8, 8, 6, 3, 7, 4, 5, 2, 1, 9};

        puzzle = new Puzzle(grid, soleDigit);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();
        // Methods used:
        // Last Digit
        // Sole Location in a Box
        // Sole Location in a Row/Column
        // Sole Digit

        assertEquals(3, difficulty);
        assertArrayEquals(expected, solution);
    }

    @Test
    public void nakedPair() {
        int[] nakedPair = {0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 9, 1, 9, 2, 0, 0, 0, 0, 0, 5, 0, 0, 0, 6, 0, 0, 7, 0, 4, 0, 4, 0, 9, 0, 1, 0, 5, 0, 2, 0, 1, 0, 0, 3, 0, 0, 0, 7, 0, 0, 0, 0, 0, 4, 8, 3, 3, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0};
        int[] expected = {6, 3, 4, 8, 5, 9, 1, 2, 7, 5, 8, 7, 2, 1, 4, 6, 3, 9, 1, 9, 2, 7, 3, 6, 8, 4, 5, 9, 5, 3, 6, 2, 8, 7, 1, 4, 8, 4, 6, 9, 7, 1, 3, 5, 2, 2, 7, 1, 5, 4, 3, 9, 6, 8, 7, 6, 5, 1, 9, 2, 4, 8, 3, 3, 2, 8, 4, 6, 7, 5, 9, 1, 4, 1, 9, 3, 8, 5, 2, 7, 6};

        puzzle = new Puzzle(grid, nakedPair);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();
        // Methods used:
        // Last Digit
        // Sole Location in a Box
        // Sole Location in a Row
        // Sole Digit
        // Candidates, Sole Candidate, Sole Location
        // Naked Pair

        assertEquals(4, difficulty);
        assertArrayEquals(expected, solution);
    }

    @Test
    public void nakedTriple() {
        int[] nakedTriple = {8,5,0,0,0,2,4,0,0,7,2,0,0,0,0,0,0,9,0,0,4,0,0,0,0,0,0,0,0,0,1,0,7,0,0,2,3,0,5,0,0,0,9,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,8,0,0,7,0,0,1,7,0,0,0,0,0,0,0,0,0,0,3,6,0,4,0};
        int[] expected = {8,5,9,6,1,2,4,3,7,7,2,3,8,5,4,1,6,9,1,6,4,3,7,9,5,2,8,9,8,6,1,4,7,3,5,2,3,7,5,2,6,8,9,1,4,2,4,1,5,9,3,7,8,6,4,3,2,9,8,1,6,7,5,6,1,7,4,2,5,8,9,3,5,9,8,7,3,6,2,4,1};

        puzzle = new Puzzle(grid, nakedTriple);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();
        // Methods used:
        // Last Digit
        // Sole Location in a Box
        // Sole Location in a Row/Column
        // Sole Digit
        // Candidates, Sole Candidate
        // Naked Pair
        // Naked Triple

        assertEquals(4, difficulty);
        assertArrayEquals(expected, solution);
    }

    @Test
    public void hiddenPair() {
        int[] hiddenPair = {4, 0, 0, 0, 0, 0, 8, 0, 5, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 8, 0, 4, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 6, 0, 3, 0, 7, 0, 5, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0};
        int[] expected = {4, 1, 7, 3, 6, 9, 8, 2, 5, 6, 3, 2, 1, 5, 8, 9, 4, 7, 9, 5, 8, 7, 2, 4, 3, 1, 6, 8, 2, 5, 4, 3, 7, 1, 6, 9, 7, 9, 1, 5, 8, 6, 4, 3, 2, 3, 4, 6, 9, 1, 2, 7, 5, 8, 2, 8, 9, 6, 4, 3, 5, 7, 1, 5, 7, 3, 2, 9, 1, 6, 8, 4, 1, 6, 4, 8, 7, 5, 2, 9, 3};

        puzzle = new Puzzle(grid, hiddenPair);
        int difficulty = rater.rate(puzzle);
        int[] solution = rater.getPartialSolution();
        // Methods used:
        // Last Digit
        // Sole Location in a Box
        // Sole Location in a Row/Column
        // Sole Digit
        // Candidates, Sole Candidate, Sole Location
        // Naked Pair
        // Naked Triple
        // Hidden Pair

        assertEquals(5, difficulty);
        assertArrayEquals(expected, solution);

    }

    /*
    Valid puzzles currently beyond the scope of the rater.
    */

    @Test
    public void veryDifficult() {
        int[] veryDifficult = {0, 0, 0, 0, 3, 0, 0, 9, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 0, 5, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 8, 0, 4, 0, 6, 0, 8, 0, 5, 0, 0, 0, 2, 0, 0, 7, 5, 0, 0, 0, 0, 0, 0, 4, 0, 1, 0, 0, 6, 0, 0, 3, 0, 0, 0, 0, 0, 4, 0, 6, 0};
        int[] expected = {7, 1, 8, 4, 3, 5, 6, 9, 2, 9, 6, 3, 2, 7, 8, 5, 4, 1, 2, 5, 4, 9, 6, 1, 3, 7, 8, 5, 4, 7, 6, 1, 2, 8, 3, 9, 1, 9, 2, 3, 8, 7, 4, 5, 6, 3, 8, 6, 5, 4, 9, 1, 2, 7, 6, 7, 5, 8, 9, 3, 2, 1, 4, 4, 2, 1, 7, 5, 6, 9, 8, 3, 8, 3, 9, 1, 2, 4, 7, 6, 5};

        puzzle = new Puzzle(grid, veryDifficult);
        int difficulty = rater.rate(puzzle);

        assertEquals(0, difficulty);
    }

    @Test
    public void extremelyDifficult() {
        int[] extremelyDifficult = {0, 0, 8, 9, 0, 7, 0, 5, 0, 0, 7, 0, 0, 4, 0, 0, 1, 0, 0, 6, 0, 5, 0, 1, 0, 0, 7, 6, 3, 0, 0, 0, 0, 9, 0, 0, 0, 0, 9, 0, 0, 0, 8, 0, 0, 0, 0, 7, 4, 0, 0, 0, 3, 5, 7, 0, 0, 6, 0, 3, 0, 2, 0, 0, 4, 0, 0, 7, 0, 0, 6, 0, 0, 0, 6, 1, 0, 4, 7, 0, 0};
        int[] expected = {4, 1, 8, 9, 6, 7, 2, 5, 3, 5, 7, 2, 3, 4, 8, 6, 1, 9, 9, 6, 3, 5, 2, 1, 4, 8, 7, 6, 3, 4, 8, 1, 5, 9, 7, 2, 1, 5, 9, 7, 3, 2, 8, 4, 6, 2, 8, 7, 4, 9, 6, 1, 3, 5, 7, 9, 1, 6, 8, 3, 5, 2, 4, 8, 4, 5, 2, 7, 9, 3, 6, 1, 3, 2, 6, 1, 5, 4, 7, 9, 8};

        puzzle = new Puzzle(grid, extremelyDifficult);
        int difficulty = rater.rate(puzzle);

        assertEquals(0, difficulty);
    }

    @Test
    public void backtrackingRequired() {
        int[] backtrackingRequired = {0,0,5,3,0,0,0,0,0,8,0,0,0,0,0,0,2,0,0,7,0,0,1,0,5,0,0,4,0,0,0,0,5,3,0,0,0,1,0,0,7,0,0,0,6,0,0,3,2,0,0,0,8,0,0,6,0,5,0,0,0,0,9,0,0,4,0,0,0,0,3,0,0,0,0,0,0,9,7,0,0};
        int[] expected = {1,4,5,3,2,7,6,9,8,8,3,9,6,5,4,1,2,7,6,7,2,9,1,8,5,4,3,4,9,6,1,8,5,3,7,2,2,1,8,4,7,3,9,5,6,7,5,3,2,9,6,4,8,1,3,6,7,5,4,2,8,1,9,9,8,4,7,6,1,2,3,5,5,2,1,8,3,9,7,6,4};

        puzzle = new Puzzle(grid, backtrackingRequired);
        int difficulty = rater.rate(puzzle);

        assertEquals(0, difficulty);
    }
}