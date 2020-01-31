package sudoku.logic;

import java.util.*;

/*
PuzzleRater rates a puzzle from 0-6. The current approach is rather primitive. A sequence of
methods are applied which resemble the techniques people use to solve puzzles. The rating
corresponds to the most difficult technique required. The techniques, by rating, are:
1 - Last Digit in a Box/Row/Column, Sole Location in a Box.
2 - Sole Location in a Row/Column.
3 - Sole Digit. Using Candidates - Sole Candidate, Sole Location.
4 - Naked Pair, Naked Triple.
5 - Hidden Pair, Hidden Triple.
6 - Naked Quad, Hidden Quad.
Any puzzle still unsolved is rated 0.
A log can be produced which correspond to step-by-step instructions for solving a puzzle.

TODO:
Add techniques:
  intersection removal - box/row and box/col interactions
  box/box interactions
  x-wing
Factor out each technique - strategy pattern
Rate based on how often each technique required
 */
class PuzzleRater {

    private Puzzle puzzle;
    private int numCells;
    private int numDigits;
    private boolean usingCandidates;
    private List<Set<Integer>> candidateSets;
    private int[] partialSolution;
    //private final List<String> solutionLog;
    //private int verbosity = 0;  // 0, 1 or 2.

    public PuzzleRater() {
        //solutionLog = new ArrayList<>();
    }

    public int rate(Puzzle puzzle) {
        this.puzzle = puzzle;
        numCells = puzzle.getGrid().getNumCells();
        numDigits = puzzle.getGrid().getNumDigits();
        //solutionLog.clear();
        usingCandidates = false;
        partialSolution = Arrays.copyOf(puzzle.getPuzzleNums(), numCells);
        int difficultyScore = 1;

        while (!isSolved()) {

            //logMethod("Last Digit in a Box");
            if (lastDigitInABlock(puzzle.getGrid().getBoxIndices())) {
                continue;
            }
            //logMethod("Last Digit in a Row");
            if (lastDigitInABlock(puzzle.getGrid().getRowIndices())) {
                continue;
            }
            //logMethod("Last Digit in a Column");
            if (lastDigitInABlock(puzzle.getGrid().getColIndices())) {
                continue;
            }

            //logMethod("Sole Location in a Box");
            if (soleLocationInABlock(puzzle.getGrid().getBoxIndices())) {
                continue;
            }

            //logMethod("Sole Location in a Row");
            if (soleLocationInABlock(puzzle.getGrid().getRowIndices())) {
                difficultyScore = Integer.max(2, difficultyScore);
                continue;
            }
            //logMethod("Sole Location in a Column");
            if (soleLocationInABlock(puzzle.getGrid().getColIndices())) {
                difficultyScore = Integer.max(2, difficultyScore);
                continue;
            }

            //logMethod("Sole Digit");
            if (soleDigit()) {
                difficultyScore = Integer.max(3, difficultyScore);
                continue;
            }

            // start using candidates
            if (!usingCandidates) {
                //logMethod("Using Candidates");
                generateCandidates();
                usingCandidates = true;
            }

            // these two methods never find anything in the first pass, i.e. unless later methods have
            // changed the candidates these are the same as soleDigit and soleLocationInABlock

            //logMethod("Sole Candidate");
            if (soleCandidate()) {
                continue;
            }
            //logMethod("Sole Location");
            if (soleLocation()) {
                continue;
            }

            //logMethod("Naked Pair");
            if (nakedSets(2)) {
                difficultyScore = Integer.max(4, difficultyScore);
                continue;
            }
            //logMethod("Naked Triple");
            if (nakedSets(3)) {
                difficultyScore = Integer.max(4, difficultyScore);
                continue;
            }

            //logMethod("Hidden Pair");
            if (hiddenSets(2)) {
                difficultyScore = Integer.max(5, difficultyScore);
                continue;
            }
            //logMethod("Hidden Triple");
            if (hiddenSets(3)) {
                difficultyScore = Integer.max(5, difficultyScore);
                continue;
            }

            //logMethod("Naked Quad");
            if (nakedSets(4)) {
                difficultyScore = Integer.max(6, difficultyScore);
                continue;
            }
            //logMethod("Hidden Quad");
            if (hiddenSets(4)) {
                difficultyScore = Integer.max(6, difficultyScore);
                continue;
            }

            break;
        }
        if (!isSolved()) {
            difficultyScore = 0;
        }
        //solutionLog.add("The difficulty score is " + difficultyScore);
        return difficultyScore;
    }

    private boolean isSolved() {
        return Arrays.equals(partialSolution, puzzle.getSolution());
    }

    public int[] getPartialSolution() {
        return partialSolution;
    }

    private boolean lastDigitInABlock(int[][] blocks) {
        // aka: last number in a block, open singles.
        // Look at each block. For a given block, if all but one cell is already solved then we
        // can easily determine which digit goes in the last cell.
        boolean altered = false;
        boolean[] seenDigit = new boolean[numDigits + 1];
        for (int[] block : blocks) {
            Arrays.fill(seenDigit, false);
            int emptyCellIndex = -1;
            boolean foundOneEmptyCell = false;
            for (int cellIndex : block) {
                if (partialSolution[cellIndex] == 0) {
                    if (foundOneEmptyCell) {
                        foundOneEmptyCell = false;
                        break;
                    } else {
                        foundOneEmptyCell = true;
                        emptyCellIndex = cellIndex;
                    }
                } else {
                    seenDigit[partialSolution[cellIndex]] = true;
                }
            }
            if (foundOneEmptyCell) {
                for (int digit = 1; digit <= numDigits; digit++) {
                    if (!seenDigit[digit]) {
                        handleSolvedCell(emptyCellIndex, digit);
                        altered = true;
                        break;
                    }
                }
            }
        }
        return altered;
    }

    private boolean soleLocationInABlock(int[][] blocks) {
        // aka: last remaining cell in a box/row/column, visual elimination, unique candidate,
        //      single position, hidden single (without use of candidates), pinned digit,
        //      last location
        // Look at a particular block (box/row/column). Consider each digit. It may be possible to
        // determine there is only one location possible for the digit by ruling out the
        // other locations in the block. Other locations are obviously ruled out if they are already
        // solved, but also ruled out if the digit in question appears in the group of neighbouring
        // cells in other blocks since the digit can only appear once in a block.
        for (int[] block : blocks) {
            for (int digit = 1; digit <= numDigits; digit++) {
                boolean testThisDigit = true;
                for (int cellIndex : block) {
                    if (partialSolution[cellIndex] == digit) {
                        testThisDigit = false;
                        break;
                    }
                }
                if (!testThisDigit) {
                    continue;
                }
                int soleLocationIndex = -1;
                for (int cellIndex : block) {
                    boolean foundPossibleSoleLocation = true;
                    if (partialSolution[cellIndex] == 0) {
                        int[][] allNeighbours = puzzle.getGrid().getNeighbours();
                        for (int neighbourIndex : allNeighbours[cellIndex]) {
                            if (partialSolution[neighbourIndex] == digit) {
                                foundPossibleSoleLocation = false;
                                break;
                            }
                        }
                        if (foundPossibleSoleLocation) {
                            if (soleLocationIndex == -1) {
                                soleLocationIndex = cellIndex;
                            } else {
                                soleLocationIndex = -1;
                                break;
                            }
                        }
                    }
                }
                if (soleLocationIndex != -1) {
                    handleSolvedCell(soleLocationIndex, digit);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean soleDigit() {
        // aka: last possible number, sole candidate, single candidate, forced digit, naked single,
        //      lone single
        // For a given unsolved cell, consider all the digits that might go there. A digit can be
        // ruled out if it already appears in the box, row or column the cell belongs to. If after
        // ruling digits out only one remains, it must go in that cell.
        for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
            if (partialSolution[cellIndex] == 0) {
                boolean[] possibleSoleDigits = new boolean[numDigits + 1];
                Arrays.fill(possibleSoleDigits, true);
                int[][] allNeighbours = puzzle.getGrid().getNeighbours();
                for (int neighbourIndex : allNeighbours[cellIndex]) {
                    if (partialSolution[neighbourIndex] != 0) {
                        possibleSoleDigits[partialSolution[neighbourIndex]] = false;
                    }
                }
                int soleDigit = -1;
                for (int digit = 1; digit <= numDigits; digit++) {
                    if (possibleSoleDigits[digit]) {
                        if (soleDigit == -1) {

                            soleDigit = digit;
                        } else {
                            soleDigit = -1;
                            break;
                        }
                    }
                }
                if (soleDigit != -1) {
                    handleSolvedCell(cellIndex, soleDigit);
                    return true;
                }
            }
        }
        return false;
    }

    private void generateCandidates() {
        // aka: pencil marks
        // Each unsolved cell can be considered to have a set of candidate solutions, initially
        // all digits from 1 to numDigits.
        candidateSets = new ArrayList<>(numCells);
        for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
            Set<Integer> cellCandidateSet;
            if (partialSolution[cellIndex] == 0) {
                cellCandidateSet = new HashSet<>(numDigits);
                for (int digit = 1; digit <= numDigits; digit++) {
                    cellCandidateSet.add(digit);
                }
            } else {
                cellCandidateSet = new HashSet<>(1);
                cellCandidateSet.add(partialSolution[cellIndex]);
            }
            candidateSets.add(cellIndex, cellCandidateSet);
        }
        for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
            if (candidateSets.get(cellIndex).size() == 1) {
                constrainCandidates(cellIndex);
            }
        }
    }

    private void constrainCandidates(int cellIndex) {
        // aka: candidate elimination
        // If a cell is solved, the digit it contains can be removed from the candidates of any
        // neighbouring (i.e. in the same block, row or column) cell.
        int solvedDigit = partialSolution[cellIndex];
        Set<Integer> cellCandidateSet = new HashSet<>(1);
        cellCandidateSet.add(solvedDigit);
        candidateSets.set(cellIndex, cellCandidateSet);
        // remove the solved digit from candidates of all neighbouring cells
        int[][] allNeighbours = puzzle.getGrid().getNeighbours();
        for (int neighbourIndex : allNeighbours[cellIndex]) {
            if (candidateSets.get(neighbourIndex).remove(solvedDigit)) {
                //logCandidatesChange(neighbourIndex, partialSolution[cellIndex]);
            }
        }
    }

    private boolean soleCandidate() {
        // aka: last possible number, sole digit, single candidate, forced digit, naked single,
        //      lone single
        // This technique differs from soleDigit above in that it can now leverage information
        // from the candidates for each unsolved cell rather than only using the information of
        // solved cells. If later techniques have altered the candidates then this might pick up
        // a solved cell that soleDigit would not.
        // For a given unsolved cell, if there is only one candidate remaining then that candidate
        // must go in the solution for that cell.
        for (int cellIndex = 0; cellIndex < numCells; cellIndex++) {
            if (candidateSets.get(cellIndex).size() == 1) {
                if (partialSolution[cellIndex] == 0) {
                    handleSolvedCell(cellIndex, candidateSets.get(cellIndex).iterator().next());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean soleLocation() {
        // aka: hidden single, unique candidate, single position, last location
        // This technique differs from soleLocationInABlock above in that it can now leverage
        // information from the candidates for each unsolved cell rather than only using the
        // information of solved cells. If later techniques have altered the candidates then this
        // might pick up a solved cell that soleLocationInABlock would not.
        // For a given block, looking at each cell's candidates, if a digit only appears in one of
        // those candidate lists across all cells, that digit must be the solution for that cell.
        for (int[] block : puzzle.getGrid().getBlockIndices()) {
            int[] soleLocationIndices = new int[numDigits + 1];
            boolean[] soleLocationFound = new boolean[numDigits + 1];
            Arrays.fill(soleLocationIndices, -1);
            Arrays.fill(soleLocationFound, true);
            for (int cellIndex : block) {
                if (candidateSets.get(cellIndex).size() == 1) {
                    soleLocationFound[candidateSets.get(cellIndex).iterator().next()] = false;
                }
                for (Integer candidate : candidateSets.get(cellIndex)) {
                    if (soleLocationIndices[candidate] == -1) {
                        soleLocationIndices[candidate] = cellIndex;
                    } else {
                        soleLocationFound[candidate] = false;
                    }
                }
            }

            for (int digit = 1; digit <= numDigits; digit++) {
                if (soleLocationFound[digit]) {
                    handleSolvedCell(soleLocationIndices[digit], digit);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean nakedSets(int size) {
        int[][] blocks = puzzle.getGrid().getBlockIndices();
        for (int[] block : blocks) {
            if (setInBlock(new ArrayDeque<>(), new HashSet<>(), block, 0, size)) {
                return true;
            }
        }
        return false;
    }

    private boolean hiddenSets(int size) {
        int[][] blocks = puzzle.getGrid().getBoxIndices();
        for (int[] block : blocks) {
            int countEmpty = 0;
            for (int cellIndex : block) {
                if (partialSolution[cellIndex] == 0) {
                    countEmpty++;
                }
            }

            int nakedSetSize = countEmpty - size;
            if (nakedSetSize <= 3) {
                continue;
            }

            if (setInBlock(new ArrayDeque<>(), new HashSet<>(), block, 0, nakedSetSize)) {
                return true;
            }
        }
        return false;
    }

    private boolean setInBlock(Deque<Integer> cellIndices, Set<Integer> digits,
                               int[] block, int startingIndex, int size) {

        if (cellIndices.size() == size) {
            return constrainUsingSet(cellIndices, digits, block);
        }

        for (int i = startingIndex; i < block.length; i++) {
            int cellIndex = block[i];
            Set<Integer> currentSet = candidateSets.get(cellIndex);
            if (currentSet.size() > 1 && currentSet.size() <= size) {
                Set<Integer> unionSet = new HashSet<>();
                unionSet.addAll(digits);
                unionSet.addAll(currentSet);
                if (unionSet.size() <= size) {
                    cellIndices.push(cellIndex);
                    if (setInBlock(cellIndices, unionSet, block, i + 1, size)) {
                        return true;
                    }
                    cellIndices.pop();
                }
            }
        }
        return false;
    }

    private boolean constrainUsingSet(Deque<Integer> indices, Set<Integer> digits, int[] block) {
        //logCandidatesSetFound(indices, digits);
        boolean altered = false;
        for (int cellIndex : block) {
            if (indices.contains(cellIndex)) {
                continue;
            }
            for (int digit : digits) {
                if (candidateSets.get(cellIndex).remove(digit)) {
                    //logCandidatesChange(cellIndex, digit);
                    altered = true;
                }
            }
        }
        return altered;
    }

    private void handleSolvedCell(int cellIndex, int digit) {
        partialSolution[cellIndex] = digit;
        //logSolvedCell(cellIndex, digit);
        if (usingCandidates) {
            constrainCandidates(cellIndex);
        }
    }

    /*
    private void logMethod(String method) {
        if (verbosity == 0) {
            return;
        }
        solutionLog.add("Trying: " + method + "\n");
    }

    private void logSolvedCell(int cellIndex, int digit) {
        if (verbosity == 0) {
            return;
        }
        if (verbosity == 2) {
            solutionLog.add("Puzzle grid: \n");
            solutionLog.add(PuzzlePrinter.valuesArrayToString(puzzle.getGrid(), partialSolution));
            if (usingCandidates) {
                solutionLog.add("Candidates grid: \n");
                solutionLog.add(PuzzlePrinter.candidatesSetsToString(puzzle.getGrid(), candidateSets));
            }
        }
        int boxNum = puzzle.getGrid().getBoxNumber(cellIndex) + 1;
        int rowNum = puzzle.getGrid().getRowNumber(cellIndex) + 1;
        int colNum = puzzle.getGrid().getColNumber(cellIndex) + 1;
        solutionLog.add("\tDigit " + digit + " goes at box " + boxNum +
                    ", row " + rowNum + ", column " + colNum + "\n");
        if (usingCandidates) {
            solutionLog.add("\tConstraining candidates using digit " + digit +
                    " from box " + boxNum + ", row " + rowNum + ", column " + colNum + "\n");
        }
    }

    private void logCandidatesChange(int cellIndex, int digit) {
        if (verbosity == 2) {
            int boxNum = puzzle.getGrid().getBoxNumber(cellIndex) + 1;
            int rowNum = puzzle.getGrid().getRowNumber(cellIndex) + 1;
            int colNum = puzzle.getGrid().getColNumber(cellIndex) + 1;
            solutionLog.add("\t\tRemoving digit " + digit + " from candidates for box " +
                    boxNum + ", row " + rowNum + ", column " + colNum + "\n");
        }
    }

    private void logCandidatesSetFound(Deque<Integer> indices, Set<Integer> digits) {
        if (verbosity >= 1) {
            Set<Integer> boxNums = new HashSet<>();
            Set<Integer> rowNums = new HashSet<>();
            Set<Integer> colNums = new HashSet<>();
            for (Integer i : indices) {
                boxNums.add(puzzle.getGrid().getBoxNumber(i) + 1);
                rowNums.add(puzzle.getGrid().getRowNumber(i) + 1);
                colNums.add(puzzle.getGrid().getColNumber(i) + 1);
            }
            solutionLog.add("\tFound set of size " + digits.size() + " containing digits " + digits +
                    " around box " + boxNums + ", row " + rowNums + ", column " + colNums + "\n");
        }
        if (verbosity == 2) {
            solutionLog.add("Candidates grid: \n");
            solutionLog.add(sudoku.logic.PuzzlePrinter.candidatesSetsToString(puzzle.getGrid(), candidateSets));
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String s : solutionLog) {
            result.append(s);
        }
        if (puzzle.getNumSolutions() != 1) {
            result.append("Invalid sudoku.logic.Puzzle\n");
            return result.toString();
        }
        if (isSolved()) {
            result.append("\nSolution:\n")
                    .append(PuzzlePrinter.valuesArrayToString(puzzle.getGrid(), partialSolution));
        } else {
            result.append("\nNot solved:\n")
                    .append(PuzzlePrinter.candidatesSetsToString(puzzle.getGrid(), candidateSets));
        }
        return result.toString();
    }
    */
}