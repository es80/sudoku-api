package sudoku.logic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSolver3By3 {

    private Puzzle puzzle;
    private final int BOX_HEIGHT = 3;
    private final int BOX_WIDTH = 3;
    private final Grid grid = new Grid(BOX_HEIGHT, BOX_WIDTH);

    /*
    Most test cases sourced from http://sudopedia.enjoysudoku.com/Test_Cases.html
    some from http://norvig.com/sudoku.html
    */

    /*
    Valid puzzles which should produce a single solution.
    */

    @Test
    public void alreadySolved() {
        int[] alreadySolved = {9, 7, 4, 2, 3, 6, 1, 5, 8, 6, 3, 8, 5, 9, 1, 7, 4, 2, 1, 2, 5, 4, 8, 7, 9, 3, 6, 3, 1, 6, 7, 5, 4, 2, 8, 9, 7, 4, 2, 9, 1, 8, 5, 6, 3, 5, 8, 9, 3, 6, 2, 4, 1, 7, 8, 6, 7, 1, 2, 5, 3, 9, 4, 2, 5, 3, 6, 4, 9, 8, 7, 1, 4, 9, 1, 8, 7, 3, 6, 2, 5};

        puzzle = new Puzzle(grid, alreadySolved);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(alreadySolved, solution);
    }

    @Test
    public void singleEmptyCell() {
        int[] singleEmptyCell = {2, 5, 6, 4, 8, 9, 1, 7, 3, 3, 7, 4, 6, 1, 5, 9, 8, 2, 9, 8, 1, 7, 2, 3, 4, 5, 6, 5, 9, 3, 2, 7, 4, 8, 6, 1, 7, 1, 2, 8, 0, 6, 5, 4, 9, 4, 6, 8, 5, 9, 1, 3, 2, 7, 6, 3, 5, 1, 4, 7, 2, 9, 8, 1, 2, 7, 9, 5, 8, 6, 3, 4, 8, 4, 9, 3, 6, 2, 7, 1, 5};
        int[] expected = {2, 5, 6, 4, 8, 9, 1, 7, 3, 3, 7, 4, 6, 1, 5, 9, 8, 2, 9, 8, 1, 7, 2, 3, 4, 5, 6, 5, 9, 3, 2, 7, 4, 8, 6, 1, 7, 1, 2, 8, 3, 6, 5, 4, 9, 4, 6, 8, 5, 9, 1, 3, 2, 7, 6, 3, 5, 1, 4, 7, 2, 9, 8, 1, 2, 7, 9, 5, 8, 6, 3, 4, 8, 4, 9, 3, 6, 2, 7, 1, 5};

        puzzle = new Puzzle(grid, singleEmptyCell);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void lastDigitInABlock() {
        int[] lastDigitInABlock = {3, 0, 5, 4, 2, 0, 8, 1, 0, 4, 8, 7, 9, 0, 1, 5, 0, 6, 0, 2, 9, 0, 5, 6, 3, 7, 4, 8, 5, 0, 7, 9, 3, 0, 4, 1, 6, 1, 3, 2, 0, 8, 9, 5, 7, 0, 7, 4, 0, 6, 5, 2, 8, 0, 2, 4, 1, 3, 0, 9, 0, 6, 5, 5, 0, 8, 6, 7, 0, 1, 9, 2, 0, 9, 6, 5, 1, 2, 4, 0, 8};
        int[] expected = {3, 6, 5, 4, 2, 7, 8, 1, 9, 4, 8, 7, 9, 3, 1, 5, 2, 6, 1, 2, 9, 8, 5, 6, 3, 7, 4, 8, 5, 2, 7, 9, 3, 6, 4, 1, 6, 1, 3, 2, 4, 8, 9, 5, 7, 9, 7, 4, 1, 6, 5, 2, 8, 3, 2, 4, 1, 3, 8, 9, 7, 6, 5, 5, 3, 8, 6, 7, 4, 1, 9, 2, 7, 9, 6, 5, 1, 2, 4, 3, 8};

        puzzle = new Puzzle(grid, lastDigitInABlock);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void soleLocationInABox() {
        int[] soleLocationInABox = {0, 0, 3, 0, 2, 0, 6, 0, 0, 9, 0, 0, 3, 0, 5, 0, 0, 1, 0, 0, 1, 8, 0, 6, 4, 0, 0, 0, 0, 8, 1, 0, 2, 9, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 6, 7, 0, 8, 2, 0, 0, 0, 0, 2, 6, 0, 9, 5, 0, 0, 8, 0, 0, 2, 0, 3, 0, 0, 9, 0, 0, 5, 0, 1, 0, 3, 0, 0};
        int[] expected = {4, 8, 3, 9, 2, 1, 6, 5, 7, 9, 6, 7, 3, 4, 5, 8, 2, 1, 2, 5, 1, 8, 7, 6, 4, 9, 3, 5, 4, 8, 1, 3, 2, 9, 7, 6, 7, 2, 9, 5, 6, 4, 1, 3, 8, 1, 3, 6, 7, 9, 8, 2, 4, 5, 3, 7, 2, 6, 8, 9, 5, 1, 4, 8, 1, 4, 2, 5, 3, 7, 6, 9, 6, 9, 5, 4, 1, 7, 3, 8, 2};

        puzzle = new Puzzle(grid, soleLocationInABox);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void soleLocationInABlock() {
        int[] soleLocationInABlock = {0, 0, 2, 0, 3, 0, 0, 0, 8, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 3, 1, 0, 2, 0, 0, 0, 0, 0, 6, 0, 0, 5, 0, 2, 7, 0, 0, 1, 0, 0, 0, 0, 0, 5, 0, 2, 0, 4, 0, 6, 0, 0, 3, 1, 0, 0, 0, 0, 8, 0, 6, 0, 5, 0, 0, 0, 0, 0, 0, 0, 1, 3, 0, 0, 5, 3, 1, 0, 4, 0, 0};
        int[] expected = {6, 7, 2, 4, 3, 5, 1, 9, 8, 5, 4, 9, 1, 7, 8, 3, 6, 2, 8, 3, 1, 6, 2, 9, 5, 4, 7, 3, 6, 8, 9, 5, 1, 2, 7, 4, 9, 1, 7, 2, 4, 3, 8, 5, 6, 2, 5, 4, 8, 6, 7, 9, 3, 1, 1, 9, 3, 7, 8, 4, 6, 2, 5, 4, 8, 6, 5, 9, 2, 7, 1, 3, 7, 2, 5, 3, 1, 6, 4, 8, 9};

        puzzle = new Puzzle(grid, soleLocationInABlock);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void soleDigit() {
        int[] soleDigit = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 8, 5, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 5, 0, 7, 0, 0, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 7, 3, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 9};
        int[] expected = {9, 8, 7, 6, 5, 4, 3, 2, 1, 2, 4, 6, 1, 7, 3, 9, 8, 5, 3, 5, 1, 9, 2, 8, 7, 4, 6, 1, 2, 8, 5, 3, 7, 6, 9, 4, 6, 3, 4, 8, 9, 2, 1, 5, 7, 7, 9, 5, 4, 6, 1, 8, 3, 2, 5, 1, 9, 2, 8, 6, 4, 7, 3, 4, 7, 2, 3, 1, 9, 5, 6, 8, 8, 6, 3, 7, 4, 5, 2, 1, 9};

        puzzle = new Puzzle(grid, soleDigit);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void nakedPair() {
        int[] nakedPair = {0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 9, 1, 9, 2, 0, 0, 0, 0, 0, 5, 0, 0, 0, 6, 0, 0, 7, 0, 4, 0, 4, 0, 9, 0, 1, 0, 5, 0, 2, 0, 1, 0, 0, 3, 0, 0, 0, 7, 0, 0, 0, 0, 0, 4, 8, 3, 3, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0};
        int[] expected = {6, 3, 4, 8, 5, 9, 1, 2, 7, 5, 8, 7, 2, 1, 4, 6, 3, 9, 1, 9, 2, 7, 3, 6, 8, 4, 5, 9, 5, 3, 6, 2, 8, 7, 1, 4, 8, 4, 6, 9, 7, 1, 3, 5, 2, 2, 7, 1, 5, 4, 3, 9, 6, 8, 7, 6, 5, 1, 9, 2, 4, 8, 3, 3, 2, 8, 4, 6, 7, 5, 9, 1, 4, 1, 9, 3, 8, 5, 2, 7, 6};

        puzzle = new Puzzle(grid, nakedPair);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void nakedTriple() {
        int[] nakedTriple = {8,5,0,0,0,2,4,0,0,7,2,0,0,0,0,0,0,9,0,0,4,0,0,0,0,0,0,0,0,0,1,0,7,0,0,2,3,0,5,0,0,0,9,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,8,0,0,7,0,0,1,7,0,0,0,0,0,0,0,0,0,0,3,6,0,4,0};
        int[] expected = {8,5,9,6,1,2,4,3,7,7,2,3,8,5,4,1,6,9,1,6,4,3,7,9,5,2,8,9,8,6,1,4,7,3,5,2,3,7,5,2,6,8,9,1,4,2,4,1,5,9,3,7,8,6,4,3,2,9,8,1,6,7,5,6,1,7,4,2,5,8,9,3,5,9,8,7,3,6,2,4,1};

        puzzle = new Puzzle(grid, nakedTriple);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void hiddenPair() {
        int[] hiddenPair = {4, 0, 0, 0, 0, 0, 8, 0, 5, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 8, 0, 4, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 6, 0, 3, 0, 7, 0, 5, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 0, 0, 0};
        int[] expected = {4, 1, 7, 3, 6, 9, 8, 2, 5, 6, 3, 2, 1, 5, 8, 9, 4, 7, 9, 5, 8, 7, 2, 4, 3, 1, 6, 8, 2, 5, 4, 3, 7, 1, 6, 9, 7, 9, 1, 5, 8, 6, 4, 3, 2, 3, 4, 6, 9, 1, 2, 7, 5, 8, 2, 8, 9, 6, 4, 3, 5, 7, 1, 5, 7, 3, 2, 9, 1, 6, 8, 4, 1, 6, 4, 8, 7, 5, 2, 9, 3};

        puzzle = new Puzzle(grid, hiddenPair);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void veryDifficult() {
        int[] veryDifficult = {0, 0, 0, 0, 3, 0, 0, 9, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 0, 5, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 8, 0, 4, 0, 6, 0, 8, 0, 5, 0, 0, 0, 2, 0, 0, 7, 5, 0, 0, 0, 0, 0, 0, 4, 0, 1, 0, 0, 6, 0, 0, 3, 0, 0, 0, 0, 0, 4, 0, 6, 0};
        int[] expected = {7, 1, 8, 4, 3, 5, 6, 9, 2, 9, 6, 3, 2, 7, 8, 5, 4, 1, 2, 5, 4, 9, 6, 1, 3, 7, 8, 5, 4, 7, 6, 1, 2, 8, 3, 9, 1, 9, 2, 3, 8, 7, 4, 5, 6, 3, 8, 6, 5, 4, 9, 1, 2, 7, 6, 7, 5, 8, 9, 3, 2, 1, 4, 4, 2, 1, 7, 5, 6, 9, 8, 3, 8, 3, 9, 1, 2, 4, 7, 6, 5};

        puzzle = new Puzzle(grid, veryDifficult);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void extremelyDifficult() {
        int[] extremelyDifficult = {0, 0, 8, 9, 0, 7, 0, 5, 0, 0, 7, 0, 0, 4, 0, 0, 1, 0, 0, 6, 0, 5, 0, 1, 0, 0, 7, 6, 3, 0, 0, 0, 0, 9, 0, 0, 0, 0, 9, 0, 0, 0, 8, 0, 0, 0, 0, 7, 4, 0, 0, 0, 3, 5, 7, 0, 0, 6, 0, 3, 0, 2, 0, 0, 4, 0, 0, 7, 0, 0, 6, 0, 0, 0, 6, 1, 0, 4, 7, 0, 0};
        int[] expected = {4, 1, 8, 9, 6, 7, 2, 5, 3, 5, 7, 2, 3, 4, 8, 6, 1, 9, 9, 6, 3, 5, 2, 1, 4, 8, 7, 6, 3, 4, 8, 1, 5, 9, 7, 2, 1, 5, 9, 7, 3, 2, 8, 4, 6, 2, 8, 7, 4, 9, 6, 1, 3, 5, 7, 9, 1, 6, 8, 3, 5, 2, 4, 8, 4, 5, 2, 7, 9, 3, 6, 1, 3, 2, 6, 1, 5, 4, 7, 9, 8};

        puzzle = new Puzzle(grid, extremelyDifficult);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    @Test
    public void backtrackingRequired() {
        int[] backtrackingRequired = {0,0,5,3,0,0,0,0,0,8,0,0,0,0,0,0,2,0,0,7,0,0,1,0,5,0,0,4,0,0,0,0,5,3,0,0,0,1,0,0,7,0,0,0,6,0,0,3,2,0,0,0,8,0,0,6,0,5,0,0,0,0,9,0,0,4,0,0,0,0,3,0,0,0,0,0,0,9,7,0,0};
        int[] expected = {1,4,5,3,2,7,6,9,8,8,3,9,6,5,4,1,2,7,6,7,2,9,1,8,5,4,3,4,9,6,1,8,5,3,7,2,2,1,8,4,7,3,9,5,6,7,5,3,2,9,6,4,8,1,3,6,7,5,4,2,8,1,9,9,8,4,7,6,1,2,3,5,5,2,1,8,3,9,7,6,4};

        puzzle = new Puzzle(grid, backtrackingRequired);
        int[] solution = puzzle.getSolution();

        assertEquals(1, puzzle.getNumSolutions());
        assertArrayEquals(expected, solution);
    }

    /*
    Invalid puzzles which should produce multiple solutions.
    */

    @Test
    public void has2Solutions() {
        int[] has2Solutions = {0, 3, 9, 0, 0, 0, 1, 2, 0, 0, 0, 0, 9, 0, 7, 0, 0, 0, 8, 0, 0, 4, 0, 1, 0, 0, 6, 0, 4, 2, 0, 0, 0, 7, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 1, 0, 0, 0, 5, 4, 0, 5, 0, 0, 1, 0, 9, 0, 0, 3, 0, 0, 0, 8, 0, 5, 0, 0, 0, 0, 1, 4, 0, 0, 0, 8, 7, 0};
        int[][] possibleSolutions = {{4, 3, 9, 6, 5, 8, 1, 2, 7, 1, 5, 6, 9, 2, 7, 3, 8, 4, 8, 2, 7, 4, 3, 1, 9, 5, 6, 3, 4, 2, 5, 1, 6, 7, 9, 8, 7, 8, 5, 2, 9, 4, 6, 3, 1, 6, 9, 1, 7, 8, 3, 5, 4, 2, 5, 7, 8, 1, 4, 9, 2, 6, 3, 2, 6, 3, 8, 7, 5, 4, 1, 9, 9, 1, 4, 3, 6, 2, 8, 7, 5},
                {4, 3, 9, 6, 5, 8, 1, 2, 7, 1, 5, 6, 9, 2, 7, 3, 8, 4, 8, 2, 7, 4, 3, 1, 9, 5, 6, 6, 4, 2, 5, 1, 3, 7, 9, 8, 7, 8, 5, 2, 9, 4, 6, 3, 1, 3, 9, 1, 7, 8, 6, 5, 4, 2, 5, 7, 8, 1, 4, 9, 2, 6, 3, 2, 6, 3, 8, 7, 5, 4, 1, 9, 9, 1, 4, 3, 6, 2, 8, 7, 5}};

        puzzle = new Puzzle(grid, has2Solutions);
        int[] solution = puzzle.getSolution();

        assertEquals(2, puzzle.getNumSolutions());
        assertThat(Arrays.asList(possibleSolutions), hasItems(solution));
    }

    @Test
    public void has3Solutions() {
        int[] has3Solutions = {0, 0, 3, 0, 0, 0, 0, 0, 6, 0, 0, 0, 9, 8, 0, 0, 2, 0, 9, 4, 2, 6, 0, 0, 7, 0, 0, 4, 5, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 9, 0, 5, 0, 4, 7, 0, 0, 0, 0, 0, 2, 5, 0, 4, 0, 6, 0, 0, 0, 7, 8, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[][] possibleSolutions = {{7, 8, 3, 5, 4, 2, 1, 9, 6, 5, 1, 6, 9, 8, 7, 3, 2, 4, 9, 4, 2, 6, 3, 1, 7, 5, 8, 4, 5, 7, 2, 9, 6, 8, 1, 3, 2, 3, 8, 7, 1, 4, 9, 6, 5, 1, 6, 9, 8, 5, 3, 4, 7, 2, 8, 9, 1, 3, 2, 5, 6, 4, 7, 6, 2, 4, 1, 7, 8, 5, 3, 9, 3, 7, 5, 4, 6, 9, 2, 8, 1},
                {7, 8, 3, 5, 4, 2, 9, 1, 6, 5, 1, 6, 9, 8, 7, 3, 2, 4, 9, 4, 2, 6, 3, 1, 7, 5, 8, 4, 5, 7, 2, 1, 6, 8, 3, 9, 2, 3, 8, 7, 9, 4, 1, 6, 5, 1, 6, 9, 8, 5, 3, 4, 7, 2, 8, 9, 1, 3, 2, 5, 6, 4, 7, 6, 2, 4, 1, 7, 8, 5, 9, 3, 3, 7, 5, 4, 6, 9, 2, 8, 1},
                {7, 8, 3, 5, 4, 2, 9, 1, 6, 5, 1, 6, 9, 8, 7, 3, 2, 4, 9, 4, 2, 6, 3, 1, 7, 5, 8, 4, 5, 7, 2, 1, 6, 8, 9, 3, 2, 3, 8, 7, 9, 4, 1, 6, 5, 1, 6, 9, 8, 5, 3, 4, 7, 2, 8, 9, 1, 3, 2, 5, 6, 4, 7, 6, 2, 4, 1, 7, 8, 5, 3, 9, 3, 7, 5, 4, 6, 9, 2, 8, 1}};

        puzzle = new Puzzle(grid, has3Solutions);
        int[] solution = puzzle.getSolution();

        assertEquals(Math.min(3, Solver.MAX_SOLUTIONS), puzzle.getNumSolutions());
        assertThat(Arrays.asList(possibleSolutions), hasItems(solution));
    }

    @Test
    public void has4Solutions() {
        int[] has4Solutions = {0, 0, 0, 0, 9, 0, 0, 0, 0, 6, 0, 0, 4, 0, 7, 0, 0, 8, 0, 4, 0, 8, 1, 2, 0, 3, 0, 7, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 4, 0, 0, 0, 9, 0, 0, 5, 0, 0, 3, 7, 1, 0, 0, 4, 0, 5, 0, 0, 6, 0, 0, 4, 0, 2, 0, 1, 7, 0, 8, 5, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[][] possibleSolutions = {{1, 7, 8, 6, 9, 3, 4, 5, 2, 6, 2, 3, 4, 5, 7, 1, 9, 8, 9, 4, 5, 8, 1, 2, 7, 3, 6, 7, 1, 6, 9, 8, 4, 3, 2, 5, 3, 8, 4, 5, 2, 6, 9, 1, 7, 5, 9, 2, 3, 7, 1, 6, 8, 4, 8, 5, 7, 1, 6, 9, 2, 4, 3, 2, 3, 1, 7, 4, 8, 5, 6, 9, 4, 6, 9, 2, 3, 5, 8, 7, 1},
                {1, 7, 8, 6, 9, 3, 4, 5, 2, 6, 2, 3, 4, 5, 7, 1, 9, 8, 9, 4, 5, 8, 1, 2, 7, 3, 6, 7, 1, 6, 9, 8, 4, 3, 2, 5, 3, 8, 4, 5, 2, 6, 9, 7, 1, 5, 9, 2, 3, 7, 1, 6, 8, 4, 8, 5, 7, 1, 6, 9, 2, 4, 3, 2, 3, 1, 7, 4, 8, 5, 6, 9, 4, 6, 9, 2, 3, 5, 8, 1, 7},
                {1, 7, 8, 6, 9, 3, 4, 5, 2, 6, 2, 3, 4, 5, 7, 1, 9, 8, 9, 4, 5, 8, 1, 2, 7, 3, 6, 7, 6, 2, 9, 8, 4, 3, 1, 5, 3, 1, 4, 5, 2, 6, 9, 8, 7, 5, 8, 9, 3, 7, 1, 6, 2, 4, 8, 5, 7, 1, 6, 9, 2, 4, 3, 2, 3, 1, 7, 4, 8, 5, 6, 9, 4, 9, 6, 2, 3, 5, 8, 7, 1},
                {1, 7, 8, 6, 9, 3, 4, 5, 2, 6, 2, 3, 4, 5, 7, 1, 9, 8, 9, 4, 5, 8, 1, 2, 7, 3, 6, 7, 8, 6, 9, 2, 4, 3, 1, 5, 3, 1, 4, 5, 8, 6, 9, 2, 7, 5, 9, 2, 3, 7, 1, 6, 8, 4, 8, 5, 7, 1, 6, 9, 2, 4, 3, 2, 3, 1, 7, 4, 8, 5, 6, 9, 4, 6, 9, 2, 3, 5, 8, 7, 1}};

        puzzle = new Puzzle(grid, has4Solutions);
        int[] solution = puzzle.getSolution();

        assertEquals(Math.min(4, Solver.MAX_SOLUTIONS), puzzle.getNumSolutions());
        assertThat(Arrays.asList(possibleSolutions), hasItems(solution));
    }

    @Test
    public void has10Solutions() {
        int[] has10Solutions = {5, 9, 0, 0, 0, 0, 0, 4, 8, 6, 0, 8, 0, 0, 0, 3, 0, 7, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 7, 5, 3, 0, 6, 9, 8, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 8, 0, 3, 0, 0, 0, 2, 0, 6, 0, 0, 0, 7, 0, 9, 3, 4, 0, 0, 0, 0, 0, 6, 5};

        puzzle = new Puzzle(grid, has10Solutions);

        assertEquals(Math.min(10, Solver.MAX_SOLUTIONS), puzzle.getNumSolutions());
    }

    @Test
    public void has125Solutions() {
        int[] has125Solutions = {0, 0, 0, 3, 1, 6, 5, 0, 0, 8, 0, 0, 5, 0, 0, 1, 0, 0, 0, 1, 0, 8, 9, 7, 2, 4, 0, 9, 0, 1, 0, 8, 5, 0, 2, 0, 0, 0, 0, 9, 0, 1, 0, 0, 0, 0, 4, 0, 2, 6, 3, 0, 0, 1, 0, 5, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 4, 0, 9, 0, 0, 2, 0, 0, 6, 1, 0, 8, 0, 0, 0};

        puzzle = new Puzzle(grid, has125Solutions);

        assertEquals(Math.min(125, Solver.MAX_SOLUTIONS), puzzle.getNumSolutions());
    }

    @Test
    public void hasManyThousandsOfSolutions() {
        int[] hasManyThousandsOfSolutions = {0,0,0,0,0,6,0,0,0,0,5,9,0,0,0,0,0,8,2,0,0,0,0,8,0,0,0,0,4,5,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,6,0,0,3,0,5,4,0,0,0,3,2,5,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        puzzle = new Puzzle(grid, hasManyThousandsOfSolutions);

        assertEquals(Math.min(10000, Solver.MAX_SOLUTIONS), puzzle.getNumSolutions());
    }

    @Test
    public void only16Clues() {
        // The accepted minimum number of clues necessary for a valid 3x3 puzzle is 17.
        int[] only16Clues = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 9, 0, 0, 0, 4, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 4, 3, 8, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        puzzle = new Puzzle(grid, only16Clues);

        assertEquals(Solver.MAX_SOLUTIONS, puzzle.getNumSolutions());
    }

    @Test
    public void onlyOneClue() {
        int[] onlyOneClue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        puzzle = new Puzzle(grid, onlyOneClue);

        assertEquals(Solver.MAX_SOLUTIONS, puzzle.getNumSolutions());
    }

    @Test
    public void emptyPuzzle() {
        int[] emptyPuzzle = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        puzzle = new Puzzle(grid, emptyPuzzle);

        assertEquals(Solver.MAX_SOLUTIONS, puzzle.getNumSolutions());
    }


    /*
    Invalid puzzles which should produce no solutions.
     */

    @Test
    public void duplicateGivenBox4() {
        int[] duplicateGivenBox4 = {0, 0, 9, 0, 7, 0, 0, 0, 5, 0, 0, 2, 1, 0, 0, 9, 0, 0, 1, 0, 0, 0, 2, 8, 0, 0, 0, 0, 7, 0, 0, 0, 5, 0, 0, 1, 0, 0, 8, 5, 1, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 6, 8, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 8, 7};

        puzzle = new Puzzle(grid, duplicateGivenBox4);
        int[] solution = puzzle.getSolution();

        assertEquals(0, puzzle.getNumSolutions());
        int[] zeros = new int[puzzle.getGrid().getNumCells()];
        assertArrayEquals(zeros, solution);
    }

    @Test
    public void duplicateGivenColumn4() {
        int[] duplicateGivenColumn4 = {6, 0, 1, 5, 9, 0, 0, 0, 0, 0, 9, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 7, 0, 3, 1, 4, 0, 0, 6, 0, 2, 4, 0, 0, 0, 0, 0, 5, 0, 0, 3, 0, 0, 0, 0, 1, 0, 0, 0, 6, 0, 0, 0, 0, 0, 3, 0, 0, 0, 9, 0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 1, 6, 0, 0};

        puzzle = new Puzzle(grid, duplicateGivenColumn4);
        int[] solution = puzzle.getSolution();

        assertEquals(0, puzzle.getNumSolutions());
        int[] zeros = new int[puzzle.getGrid().getNumCells()];
        assertArrayEquals(zeros, solution);
    }

    @Test
    public void duplicateGivenRow4() {
        int[] duplicateGivenRow4 = {0, 4, 0, 1, 0, 0, 3, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 5, 0, 0, 0, 0, 0, 0, 4, 0, 8, 9, 0, 0, 2, 6, 0, 0, 0, 0, 0, 1, 2, 0, 5, 0, 3, 0, 0, 0, 0, 7, 0, 0, 4, 0, 0, 0, 1, 6, 0, 6, 0, 0, 0, 0, 7, 0, 0, 0, 0, 1, 0, 0, 8, 0, 0, 2, 0};

        puzzle = new Puzzle(grid, duplicateGivenRow4);
        int[] solution = puzzle.getSolution();

        assertEquals(0, puzzle.getNumSolutions());
        int[] zeros = new int[puzzle.getGrid().getNumCells()];
        assertArrayEquals(zeros, solution);
    }

    @Test
    public void noCandidatesLeftForIndex36() {
        int[] noCandidatesLeftForIndex36 = {0, 0, 9, 0, 2, 8, 7, 0, 0, 8, 0, 6, 0, 0, 4, 0, 0, 5, 0, 0, 3, 0, 0, 0, 0, 0, 4, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 7, 1, 3, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 5, 0, 0, 9, 0, 0, 4, 0, 0, 8, 0, 7, 0, 0, 1, 2, 5, 0, 3, 0, 0};

        puzzle = new Puzzle(grid, noCandidatesLeftForIndex36);
        int[] solution = puzzle.getSolution();

        assertEquals(0, puzzle.getNumSolutions());
        int[] zeros = new int[puzzle.getGrid().getNumCells()];
        assertArrayEquals(zeros, solution);
    }

    @Test
    public void noLocationsLeftFor4InBox4() {
        int[] noLocationsLeftFor4InBox4 = {0, 9, 0, 3, 0, 0, 0, 0, 1, 0, 0, 0, 0, 8, 0, 0, 4, 6, 0, 0, 0, 0, 0, 0, 8, 0, 0, 4, 0, 5, 0, 6, 0, 0, 3, 0, 0, 0, 3, 2, 7, 5, 6, 0, 0, 0, 6, 0, 0, 1, 0, 9, 0, 4, 0, 0, 1, 0, 0, 0, 0, 0, 0, 5, 8, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 7, 0, 6, 0};

        puzzle = new Puzzle(grid, noLocationsLeftFor4InBox4);
        int[] solution = puzzle.getSolution();

        assertEquals(0, puzzle.getNumSolutions());
        int[] zeros = new int[puzzle.getGrid().getNumCells()];
        assertArrayEquals(zeros, solution);
    }

    @Test
    public void noLocationsLeftFor2InColumn4() {
        int[] noLocationsLeftFor2InColumn4 = {0, 0, 0, 0, 4, 1, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 3, 2, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 4, 1, 7, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 3, 0, 0, 4, 8, 0, 0, 0, 0, 0, 0, 5, 0, 1, 0, 0, 2, 0, 0, 0};

        puzzle = new Puzzle(grid, noLocationsLeftFor2InColumn4);
        int[] solution = puzzle.getSolution();

        assertEquals(0, puzzle.getNumSolutions());
        int[] zeros = new int[puzzle.getGrid().getNumCells()];
        assertArrayEquals(zeros, solution);
    }

    @Test
    public void noLocationsLeftFor1InRow4() {
        int[] noLocationsLeftFor1InRow4 = {9, 0, 0, 1, 0, 0, 0, 0, 4, 0, 1, 4, 0, 3, 0, 8, 0, 0, 0, 0, 3, 0, 0, 0, 0, 9, 0, 0, 0, 0, 7, 0, 8, 0, 0, 1, 8, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 2, 1, 0, 0, 0, 0, 7, 0, 0, 0, 9, 0, 4, 0, 5, 0, 0, 5, 0, 0, 0, 1, 6, 0, 0, 3};

        puzzle = new Puzzle(grid, noLocationsLeftFor1InRow4);
        int[] solution = puzzle.getSolution();

        assertEquals(0, puzzle.getNumSolutions());
        int[] zeros = new int[puzzle.getGrid().getNumCells()];
        assertArrayEquals(zeros, solution);
    }

    @Test
    public void slowToFindNoSolutions() {
        int[] slowToFindNoSolutions = {0,0,0,0,0,5,0,8,0,0,0,0,6,0,1,0,4,3,0,0,0,0,0,0,0,0,0,0,1,0,5,0,0,0,0,0,0,0,0,1,0,6,0,0,0,3,0,0,0,0,0,0,0,5,5,3,0,0,0,0,0,6,1,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0};

        // Takes around 5 minutes to verify no solutions.
        // Needs techniques like hidden/naked pair/triple to prune the search tree or else
        // some randomization in path choice to solve in good time.

        /*
        puzzle = new Puzzle(grid, slowToFindNoSolutions);
        int[] solution = puzzle.getSolution();

        assertEquals(0, puzzle.getNumSolutions());
        int[] zeros = new int[puzzle.getGrid().getNumCells()];
        assertArrayEquals(zeros, solution);
        */
    }
}