package sudoku.api;

import sudoku.logic.Difficulty;

public class PuzzleResponse {

    private int[] puzzleNums;
    private int[] solution;
    private NumberOfSolutions numberOfSolutions;
    private Difficulty difficulty;

    public PuzzleResponse() { }

    public int[] getPuzzleNums() {
        return puzzleNums;
    }

    public PuzzleResponse setPuzzleNums(int[] puzzleNums) {
        this.puzzleNums = puzzleNums;
        return this;
    }

    public int[] getSolution() {
        return solution;
    }

    public PuzzleResponse setSolution(int[] solution) {
        this.solution = solution;
        return this;
    }

    public NumberOfSolutions getNumberOfSolutions() {
        return numberOfSolutions;
    }

    public PuzzleResponse setNumberOfSolutions(NumberOfSolutions numberOfSolutions) {
        this.numberOfSolutions = numberOfSolutions;
        return this;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public PuzzleResponse setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }
}