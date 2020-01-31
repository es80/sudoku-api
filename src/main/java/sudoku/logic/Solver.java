package sudoku.logic;

import java.util.*;

/*
Solver uses brute force to quickly solve sudoku puzzles. The principal idea is to use backtracking
but speed it up considerably by using some partial constraint propagation to prune the search tree
- c.f. http://norvig.com/sudoku.html
 */
class Solver {

    private Puzzle puzzle;
    private int numCells;
    private int numDigits;
    private int[][] candidates;
    private int numSolutions;
    private int[] solution;
    public static final int MAX_SOLUTIONS = 2;

    public Solver() {
    }

    public int solve(Puzzle puzzle) {
        this.puzzle = puzzle;
        numCells = puzzle.getGrid().getNumCells();
        numDigits = puzzle.getGrid().getNumDigits();
        setCandidates();
        numSolutions = 0;
        backtracking();
        return numSolutions;
    }

    private void setCandidates() {
        // the candidates for each cell are the digits that might go in that cell
        // we need a collection of at most numDigits and will be using the operations:
        //   size(), contains(digit), remove(digit)
        // using an array turns out to be much faster than an arrayList or set
        //   (unless perhaps puzzles are very large)
        // the first index of each array stores the length,
        //   contains(digit) will iterate over the array
        //   remove(digit) will swap the digit with the current 'last' element and decrement length
        candidates = new int[numCells][numDigits + 1];
        int[] puzzleNums = puzzle.getPuzzleNums();
        for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
            if (puzzleNums[cellIndex] == 0) {
                // store length of candidates at index 0
                candidates[cellIndex][0] = numDigits;
                // the candidates array will now be 1-indexed which corresponds nicely with the
                // digits themselves
                for (int digit = 1; digit <= numDigits; digit++) {
                    candidates[cellIndex][digit] = digit;
                }
            } else {
                candidates[cellIndex][0] = 1;
                candidates[cellIndex][1] = puzzleNums[cellIndex];
            }
        }
    }

    public int[] generateRandomSolution(Grid grid) {
        // solve an empty puzzle for use in generating new random puzzles.
        puzzle = new Puzzle(grid, new int[grid.getNumCells()]);
        numCells = puzzle.getGrid().getNumCells();
        numDigits = puzzle.getGrid().getNumDigits();
        setShuffledCandidates();
        numSolutions = 0;
        backtracking();
        return solution;
    }

    private void setShuffledCandidates() {
        List<Integer> digits = new ArrayList<>(numDigits);
        for (int digit = 1; digit <= numDigits; digit++) {
            digits.add(digit);
        }
        candidates = new int[numCells][numDigits + 1];
        for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
            candidates[cellIndex][0] = numDigits;
            Collections.shuffle(digits);
            for (int j = 1; j <= numDigits; j++) {
                candidates[cellIndex][j] = digits.get(j - 1);
            }
        }
    }

    public int[] getSolution() {
        return solution;
    }

    private boolean isValid() {
        int[][] allNeighbours = puzzle.getGrid().getNeighbours();
        for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
            if (candidates[cellIndex][0] == 1) {
                int solvedDigit = candidates[cellIndex][1];
                for (int neighbourIndex : allNeighbours[cellIndex]) {
                    if (candidates[neighbourIndex][0] == 1 &&
                            candidates[neighbourIndex][1] == solvedDigit) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isUnsolvable() {
        for (int[] cellCandidates : candidates) {
            if (cellCandidates[0] == 0) {
                return true;
            }
        }
        return !isValid();
    }

    private boolean isSolved() {
        for (int[] cellCandidates : candidates) {
            if (cellCandidates[0] != 1) {
                return false;
            }
        }
        return isValid();
    }

    private void constrainCandidates() {
        // aka: candidate elimination
        // If a cell is solved, the digit it contains can be removed from the candidates of any
        // neighbouring (i.e. in the same block, row or column) cell.
        boolean altered;
        do {
            altered = false;
            for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
                if (candidates[cellIndex][0] == 1) {
                    if (removeFromNeighboursCandidates(cellIndex)) {
                        altered = true;
                    }
                }
            }
        }
        while (altered);
    }

    private boolean removeFromNeighboursCandidates(int cellIndex) {
        boolean altered = false;
        int solvedDigit = candidates[cellIndex][1];
        int[][] allNeighbours = puzzle.getGrid().getNeighbours();
        for (int neighbourIndex : allNeighbours[cellIndex]) {
            int numCandidates = candidates[neighbourIndex][0];
            // to remove a solvedDigit, search for it in the candidates, if found
            // swap it with the current last candidate and decrement the candidates length
            for (int j = 1; j <= numCandidates; j++) {
                if (candidates[neighbourIndex][j] == solvedDigit) {
                    candidates[neighbourIndex][j] = candidates[neighbourIndex][numCandidates];
                    candidates[neighbourIndex][0]--;
                    altered = true;
                    break;
                }
            }
        }
        return altered;
    }

    private void soleLocation() {
        // aka: hidden single, unique candidate, single position, last location
        // For a given unsolved cell, if there is only one candidate remaining then that candidate
        // must go in the solution for that cell.
        // For a given block, looking at each cell's candidates, if a digit only appears in one of
        // those candidate lists across all cells, that digit must be the solution for that cell.
        boolean altered;
        do {
            altered = false;
            constrainCandidates();
            for (int[] block : puzzle.getGrid().getBlockIndices()) {
                int[] soleLocationIndices = new int[numDigits + 1];
                boolean[] soleLocationFound = new boolean[numDigits + 1];
                Arrays.fill(soleLocationIndices, -1);
                Arrays.fill(soleLocationFound, false);
                for (int cellIndex : block) {
                    if (candidates[cellIndex][0] == 1) {
                        soleLocationIndices[candidates[cellIndex][1]] = cellIndex;
                    }
                    for (int j = 1; j <= candidates[cellIndex][0]; j++) {
                        int digit = candidates[cellIndex][j];
                        if (soleLocationIndices[digit] == -1) {
                            soleLocationIndices[digit] = cellIndex;
                            soleLocationFound[digit] = true;
                        } else {
                            soleLocationFound[digit] = false;
                        }
                    }
                }

                for (int digit = 1; digit <= numDigits; digit++) {
                    if (soleLocationFound[digit]) {
                        int cellIndex = soleLocationIndices[digit];
                        candidates[cellIndex][0] = 1;
                        candidates[cellIndex][1] = digit;
                        altered = true;
                    }
                }
            }
        }
        while (altered);
    }

    private void backtracking() {

        // try to prune the search tree
        soleLocation();
        if (isUnsolvable()) {
            return;
        }

        // check for a solution
        if (isSolved()) {
            if (numSolutions == 0) {
                solution = new int[numCells];
                for (int i = 0; i < numCells; i++) {
                    solution[i] = candidates[i][1];
                }
            }
            numSolutions++;
            return;
        }

        // look for the cell with the fewest candidates to start searching from
        int nextIndex = -1;
        int fewestCandidates = numDigits + 1;
        for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
            int numCandidates = candidates[cellIndex][0];
            if (numCandidates == 2) {
                nextIndex = cellIndex;
                break;
            }
            else if (numCandidates > 2 && numCandidates < fewestCandidates) {
                nextIndex = cellIndex;
                fewestCandidates = numCandidates;
            }
        }

        int numCandidates = candidates[nextIndex][0];
        // for each candidate in this cell
        for (int i = 1; i <= numCandidates; i++) {

            // keep a backup copy of all candidates (it will get mutated in recursive calls)
            int[][] candidatesCopy = new int[numCells][numDigits + 1];
            for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
                System.arraycopy(candidates[cellIndex], 0, candidatesCopy[cellIndex], 0, numDigits + 1);
            }

            // try this candidate
            candidates[nextIndex][0] = 1;
            candidates[nextIndex][1] = candidates[nextIndex][i];
            backtracking();

            // stop searching if we have enough solutions
            if (numSolutions >= MAX_SOLUTIONS) {
                break;
            }

            // restore the backup copy of all candidates before trying the next
            for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
                System.arraycopy(candidatesCopy[cellIndex], 0, candidates[cellIndex], 0, numDigits + 1);
            }
        }
    }

    /*
    private String candidatesToString() {
        return PuzzlePrinter.candidatesArrayToString(puzzle.getGrid(), candidates);
    }
    */
}