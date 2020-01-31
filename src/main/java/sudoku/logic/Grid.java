package sudoku.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/*
For a given size of grid, this class stores various arrays for fast lookup of grid indices when
solving and rating puzzles.
 */
public class Grid {

    private final int boxHeight;
    private final int boxWidth;
    private final int numDigits;
    private final int numCells;
    private final int[][] neighbours;
    private final int[][] rowIndices;
    private final int[][] colIndices;
    private final int[][] boxIndices;
    private final int[][] blockIndices;
    private final int[] indexToRowNumber;
    private final int[] indexToColNumber;
    private final int[] indexToBoxNumber;

    public Grid(int boxHeight, int boxWidth) {
        this.boxHeight = boxHeight;
        this.boxWidth = boxWidth;
        numDigits = boxHeight * boxWidth;
        numCells = numDigits * numDigits;

        rowIndices = new int[numDigits][numDigits];
        colIndices = new int[numDigits][numDigits];
        boxIndices = new int[numDigits][numDigits];
        blockIndices = new int[numDigits * 3][numDigits];
        indexToRowNumber = new int[numCells];
        indexToColNumber = new int[numCells];
        indexToBoxNumber = new int[numCells];

        for (int row = 0; row < numDigits; row++) {
            for (int i = 0; i < numDigits; i++) {
                rowIndices[row][i] = i + row * numDigits;
                indexToRowNumber[i + row * numDigits] = row;
            }
        }

        for (int col = 0; col < numDigits; col++) {
            for (int i = 0; i < numDigits; i++) {
                colIndices[col][i] = col + i * numDigits;
                indexToColNumber[col + i * numDigits] = col;
            }
        }

        for (int box = 0; box < numDigits; box++) {
            int k = 0;
            for (int i = 0; i < boxHeight; i++) {
                for (int j = 0; j < boxWidth; j++) {
                    int index  = (i * numDigits + j) +
                            ((box % boxHeight) * boxWidth) +
                            ((box / boxHeight) * boxHeight * numDigits);
                    boxIndices[box][k] = index;
                    indexToBoxNumber[index] = box;
                    k++;
                }
            }
        }

        for (int block = 0; block < numDigits; block++) {
            for (int i = 0; i < numDigits; i++) {
                blockIndices[block][i] = rowIndices[block][i];
                blockIndices[block + numDigits][i] = colIndices[block][i];
                blockIndices[block + numDigits * 2][i] = boxIndices[block][i];
            }
        }

        List<SortedSet<Integer>> allNeighboursSets = new ArrayList<>(numCells);
        for (int i = 0; i < numCells; i++) {
            allNeighboursSets.add(new TreeSet<>());
        }
        for (int block = 0; block < numDigits; block++) {
            for (int i = 0; i < numDigits; i++) {
                for (int j = 0; j < numDigits; j++) {
                    if (i != j) {
                        allNeighboursSets.get(rowIndices[block][i]).add(rowIndices[block][j]);
                        allNeighboursSets.get(colIndices[block][i]).add(colIndices[block][j]);
                        allNeighboursSets.get(boxIndices[block][i]).add(boxIndices[block][j]);
                    }
                }
            }
        }
        int numNeighbours = boxHeight * boxWidth * 3 - boxWidth - boxHeight - 1;
        neighbours = new int[numCells][numNeighbours];
        for (int i = 0; i < numCells; i++) {
            int j = 0;
            for (Integer index : allNeighboursSets.get(i)) {
                neighbours[i][j] = index;
                j++;
            }
        }
    }

    public int getBoxHeight() {
        return boxHeight;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getNumDigits() {
        return numDigits;
    }

    public int getNumCells() {
        return numCells;
    }

    public int[][] getNeighbours() {
        return neighbours;
    }

    public int[][] getRowIndices() {
        return rowIndices;
    }

    public int[][] getColIndices() {
        return colIndices;
    }

    public int[][] getBoxIndices() {
        return boxIndices;
    }

    public int[][] getBlockIndices() {
        return blockIndices;
    }

    public int getRowNumber(int index) {
        return indexToRowNumber[index];
    }

    public int getColNumber(int index) {
        return indexToColNumber[index];
    }

    public int getBoxNumber(int index) {
        return indexToBoxNumber[index];
    }
}