package sudoku.logic;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

/*
PuzzlePrinter provides two methods for printing puzzles, either printing with a single value in e
ach cell like a puzzle or solution, or printing with multiple values in each cell, such as to
show the candidates for each cell.
 */
class PuzzlePrinter {

    private static String genericGridToString(Grid grid, int cellWidth, Function<Integer, String> cellToString) {
        // Produce a string representation of a puzzle grid which could use
        // either puzzle values only or candidate values.
        int maxWidth = cellWidth + 2;
        StringBuilder line = new StringBuilder("+");
        for (int majorCol = 0; majorCol < grid.getBoxHeight(); majorCol++) {
            line.append("-".repeat(maxWidth).repeat(grid.getBoxWidth()));
            line.append("+");
        }
        line.append("\n");

        StringBuilder result = new StringBuilder();
        result.append(line);

        int i = 0;
        for (int majorRow = 0; majorRow < grid.getBoxWidth(); majorRow++) {
            for (int minorRow = 0; minorRow < grid.getBoxHeight(); minorRow++) {
                result.append("|");
                for (int majorCol = 0; majorCol < grid.getBoxHeight(); majorCol++) {
                    for (int minorCol = 0; minorCol < grid.getBoxWidth(); minorCol++) {
                        result.append(" ");
                        result.append(cellToString.apply(i++));
                        result.append(" ");
                    }
                    result.append("|");
                }
                result.append("\n");
            }
            result.append(line);
        }
        return result.toString();
    }

    private static String singleValueCell(int cellWidth, Integer i, int[] values) {
        // Determine the fixed width string containing the puzzle value for cell index i.
        if (values[i] != 0) {
            return String.format("%" + cellWidth + "d", values[i]);
        } else {
            return String.format("%" + cellWidth + "s", " ");
        }
    }

    private static String multiValueCell(int cellWidth, Integer i, int[][] candidateArray) {
        // Determine the fixed width string containing all the candidates for cell index i.
        StringBuilder result = new StringBuilder();
        for (int num = 1; num <= candidateArray[i][0]; num++) {
            result.append(candidateArray[i][num]).append(" ");
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        int currentLength = result.length();
        result.append(" ".repeat(cellWidth - currentLength));
        return result.toString();
    }

    public static String valuesArrayToString(Grid grid, int[] valuesArray) {
        // Determine the maximum possible width for a string of a single puzzle value first,
        int cellWidth = 0;
        int maxVal = grid.getNumDigits();
        while (maxVal != 0) {
            maxVal /= 10;
            cellWidth++;
        }
        int finalCellWidth = cellWidth;
        return genericGridToString(grid, cellWidth, i -> singleValueCell(finalCellWidth, i, valuesArray));
    }

    public static String candidatesArrayToString(Grid grid, int[][] candidatesArray) {
        // Determine the maximum possible width for a string of cell candidates first.
        int cellWidth = 0;
        for (int i = 0; i < grid.getNumCells(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int num = 1; num <= candidatesArray[i][0]; num++) {
                sb.append(candidatesArray[i][num]).append(" ");
            }
            if (sb.length() > cellWidth) {
                cellWidth = sb.length() - 1;
            }
        }
        int finalCellWidth = cellWidth;
        return genericGridToString(grid, finalCellWidth, i -> multiValueCell(finalCellWidth, i, candidatesArray));
    }

    public static String candidatesSetsToString(Grid grid, List<Set<Integer>> candidateSets) {
        int[][] candidates = new int[grid.getNumCells()][grid.getNumDigits() + 1];
        for (int cellIndex = 0; cellIndex < grid.getNumCells(); cellIndex++) {
            candidates[cellIndex][0] = candidateSets.get(cellIndex).size();
            int j = 1;
            for (Integer candidate : candidateSets.get(cellIndex)) {
                candidates[cellIndex][j++] = candidate;
            }
        }
        return candidatesArrayToString(grid, candidates);
    }
}