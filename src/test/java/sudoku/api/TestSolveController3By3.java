package sudoku.api;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;
import sudoku.logic.Difficulty;

import javax.inject.Inject;
import java.util.Arrays;

import static io.micronaut.http.HttpRequest.GET;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/*
A small selection of test cases from TestSolver3By3 and TestPuzzleRater3By3.
*/
@MicronautTest
public class TestSolveController3By3 {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/solve")
    HttpClient client;

    private final int BOX_HEIGHT = 3;
    private final int BOX_WIDTH = 3;
    private final String uriBase = "/" + BOX_HEIGHT + "/" + BOX_WIDTH + "/";

    private static String puzzleNumsAsString(int[] puzzleNums) {
        StringBuilder sb = new StringBuilder();
        for (int digit : puzzleNums) {
            sb.append(digit);
        }
        return sb.toString();
    }

    /*
    Valid puzzles which should produce a single solution.
     */

    @Test
    public void singleEmptyCell() {
        int[] singleEmptyCell = {2,5,6,4,8,9,1,7,3,3,7,4,6,1,5,9,8,2,9,8,1,7,2,3,4,5,6,5,9,3,2,7,4,8,6,1,7,1,2,8,0,6,5,4,9,4,6,8,5,9,1,3,2,7,6,3,5,1,4,7,2,9,8,1,2,7,9,5,8,6,3,4,8,4,9,3,6,2,7,1,5};
        int[] expected = {2,5,6,4,8,9,1,7,3,3,7,4,6,1,5,9,8,2,9,8,1,7,2,3,4,5,6,5,9,3,2,7,4,8,6,1,7,1,2,8,3,6,5,4,9,4,6,8,5,9,1,3,2,7,6,3,5,1,4,7,2,9,8,1,2,7,9,5,8,6,3,4,8,4,9,3,6,2,7,1,5};

        String uri = uriBase + puzzleNumsAsString(singleEmptyCell);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(singleEmptyCell, puzzleResponse.getPuzzleNums());
        assertArrayEquals(expected, puzzleResponse.getSolution());
        assertEquals(NumberOfSolutions.SINGLE_SOLUTION, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.EASY, puzzleResponse.getDifficulty());
    }

    @Test
    public void soleDigit() {
        int[] soleDigit = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,8,5,0,0,1,0,2,0,0,0,0,0,0,0,5,0,7,0,0,0,0,0,4,0,0,0,1,0,0,0,9,0,0,0,0,0,0,0,5,0,0,0,0,0,0,7,3,0,0,2,0,1,0,0,0,0,0,0,0,0,4,0,0,0,9};
        int[] expected = {9,8,7,6,5,4,3,2,1,2,4,6,1,7,3,9,8,5,3,5,1,9,2,8,7,4,6,1,2,8,5,3,7,6,9,4,6,3,4,8,9,2,1,5,7,7,9,5,4,6,1,8,3,2,5,1,9,2,8,6,4,7,3,4,7,2,3,1,9,5,6,8,8,6,3,7,4,5,2,1,9};

        String uri = uriBase + puzzleNumsAsString(soleDigit);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(soleDigit, puzzleResponse.getPuzzleNums());
        assertArrayEquals(expected, puzzleResponse.getSolution());
        assertEquals(NumberOfSolutions.SINGLE_SOLUTION, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.MEDIUM, puzzleResponse.getDifficulty());
    }

    @Test
    public void hiddenPair() {
        int[] hiddenPair = {4,0,0,0,0,0,8,0,5,0,3,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,2,0,0,0,0,0,6,0,0,0,0,0,8,0,4,0,0,0,0,0,0,1,0,0,0,0,0,0,0,6,0,3,0,7,0,5,0,0,2,0,0,0,0,0,1,0,4,0,0,0,0,0,0};
        int[] expected = {4,1,7,3,6,9,8,2,5,6,3,2,1,5,8,9,4,7,9,5,8,7,2,4,3,1,6,8,2,5,4,3,7,1,6,9,7,9,1,5,8,6,4,3,2,3,4,6,9,1,2,7,5,8,2,8,9,6,4,3,5,7,1,5,7,3,2,9,1,6,8,4,1,6,4,8,7,5,2,9,3};

        String uri = uriBase + puzzleNumsAsString(hiddenPair);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(hiddenPair, puzzleResponse.getPuzzleNums());
        assertArrayEquals(expected, puzzleResponse.getSolution());
        assertEquals(NumberOfSolutions.SINGLE_SOLUTION, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.FIENDISH, puzzleResponse.getDifficulty());
    }

    @Test
    public void backtrackingRequired() {
        int[] backtrackingRequired = {0,0,5,3,0,0,0,0,0,8,0,0,0,0,0,0,2,0,0,7,0,0,1,0,5,0,0,4,0,0,0,0,5,3,0,0,0,1,0,0,7,0,0,0,6,0,0,3,2,0,0,0,8,0,0,6,0,5,0,0,0,0,9,0,0,4,0,0,0,0,3,0,0,0,0,0,0,9,7,0,0};
        int[] expected = {1,4,5,3,2,7,6,9,8,8,3,9,6,5,4,1,2,7,6,7,2,9,1,8,5,4,3,4,9,6,1,8,5,3,7,2,2,1,8,4,7,3,9,5,6,7,5,3,2,9,6,4,8,1,3,6,7,5,4,2,8,1,9,9,8,4,7,6,1,2,3,5,5,2,1,8,3,9,7,6,4};

        String uri = uriBase + puzzleNumsAsString(backtrackingRequired);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(backtrackingRequired, puzzleResponse.getPuzzleNums());
        assertArrayEquals(expected, puzzleResponse.getSolution());
        assertEquals(NumberOfSolutions.SINGLE_SOLUTION, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.TOO_DIFFICULT, puzzleResponse.getDifficulty());
    }


    /*
    Invalid puzzles which should produce multiple solutions.
    */

    @Test
    public void has2Solutions() {
        int[] has2Solutions = {0,3,9,0,0,0,1,2,0,0,0,0,9,0,7,0,0,0,8,0,0,4,0,1,0,0,6,0,4,2,0,0,0,7,9,0,0,0,0,0,0,0,0,0,0,0,9,1,0,0,0,5,4,0,5,0,0,1,0,9,0,0,3,0,0,0,8,0,5,0,0,0,0,1,4,0,0,0,8,7,0};
        int[][] possibleSolutions = {{4,3,9,6,5,8,1,2,7,1,5,6,9,2,7,3,8,4,8,2,7,4,3,1,9,5,6,3,4,2,5,1,6,7,9,8,7,8,5,2,9,4,6,3,1,6,9,1,7,8,3,5,4,2,5,7,8,1,4,9,2,6,3,2,6,3,8,7,5,4,1,9,9,1,4,3,6,2,8,7,5},
                {4,3,9,6,5,8,1,2,7,1,5,6,9,2,7,3,8,4,8,2,7,4,3,1,9,5,6,6,4,2,5,1,3,7,9,8,7,8,5,2,9,4,6,3,1,3,9,1,7,8,6,5,4,2,5,7,8,1,4,9,2,6,3,2,6,3,8,7,5,4,1,9,9,1,4,3,6,2,8,7,5}};

        String uri = uriBase + puzzleNumsAsString(has2Solutions);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(has2Solutions, puzzleResponse.getPuzzleNums());
        assertThat(Arrays.asList(possibleSolutions), hasItems(puzzleResponse.getSolution()));
        assertEquals(NumberOfSolutions.MULTIPLE_SOLUTIONS, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.INVALID, puzzleResponse.getDifficulty());
    }

    @Test
    public void only16Clues() {
        // The accepted minimum number of clues necessary for a valid 3x3 puzzle is 17.
        int[] only16Clues = {0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,9,0,0,0,4,0,0,0,0,1,0,2,0,0,0,0,3,0,5,0,0,0,0,7,0,0,0,0,0,4,3,8,0,0,0,2,0,0,0,0,0,0,9,0,0,0,0,0,1,0,4,0,0,0,6,0,0,0,0,0,0,0,0,0,0};

        String uri = uriBase + puzzleNumsAsString(only16Clues);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(only16Clues, puzzleResponse.getPuzzleNums());
        assertEquals(NumberOfSolutions.MULTIPLE_SOLUTIONS, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.INVALID, puzzleResponse.getDifficulty());
    }

    @Test
    public void emptyPuzzle() {
        int[] emptyPuzzle = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        String uri = uriBase + puzzleNumsAsString(emptyPuzzle);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(emptyPuzzle, puzzleResponse.getPuzzleNums());
        assertEquals(NumberOfSolutions.MULTIPLE_SOLUTIONS, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.INVALID, puzzleResponse.getDifficulty());
    }


    /*
    Invalid puzzles which should produce no solutions.
     */

    @Test
    public void duplicateGivenColumn4() {
        int[] duplicateGivenColumn4 = {6,0,1,5,9,0,0,0,0,0,9,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,4,0,7,0,3,1,4,0,0,6,0,2,4,0,0,0,0,0,5,0,0,3,0,0,0,0,1,0,0,0,6,0,0,0,0,0,3,0,0,0,9,0,2,0,4,0,0,0,0,0,0,1,6,0,0};
        int[] zeros = new int[duplicateGivenColumn4.length];

        String uri = uriBase + puzzleNumsAsString(duplicateGivenColumn4);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(duplicateGivenColumn4, puzzleResponse.getPuzzleNums());
        assertArrayEquals(zeros, puzzleResponse.getSolution());
        assertEquals(NumberOfSolutions.NO_SOLUTIONS, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.INVALID, puzzleResponse.getDifficulty());
    }

    @Test
    public void noCandidatesLeftForIndex36() {
        int[] noCandidatesLeftForIndex36 = {0,0,9,0,2,8,7,0,0,8,0,6,0,0,4,0,0,5,0,0,3,0,0,0,0,0,4,6,0,0,0,0,0,0,0,0,0,2,0,7,1,3,4,5,0,0,0,0,0,0,0,0,0,2,3,0,0,0,0,0,5,0,0,9,0,0,4,0,0,8,0,7,0,0,1,2,5,0,3,0,0};
        int[] zeros = new int[noCandidatesLeftForIndex36.length];

        String uri = uriBase + puzzleNumsAsString(noCandidatesLeftForIndex36);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(noCandidatesLeftForIndex36, puzzleResponse.getPuzzleNums());
        assertArrayEquals(zeros, puzzleResponse.getSolution());
        assertEquals(NumberOfSolutions.NO_SOLUTIONS, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.INVALID, puzzleResponse.getDifficulty());
    }

    @Test
    public void noLocationsLeftFor2InColumn4() {
        int[] noLocationsLeftFor2InColumn4 = {0,0,0,0,4,1,0,0,0,0,6,0,0,0,0,0,2,0,0,0,2,0,0,0,0,0,0,3,2,0,6,0,0,0,0,0,0,0,0,0,5,0,0,4,1,7,0,0,0,0,0,0,0,2,0,0,0,0,0,0,2,3,0,0,4,8,0,0,0,0,0,0,5,0,1,0,0,2,0,0,0};
        int[] zeros = new int[noLocationsLeftFor2InColumn4.length];

        String uri = uriBase + puzzleNumsAsString(noLocationsLeftFor2InColumn4);
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse puzzleResponse = response.blockingFirst();

        assertArrayEquals(noLocationsLeftFor2InColumn4, puzzleResponse.getPuzzleNums());
        assertArrayEquals(zeros, puzzleResponse.getSolution());
        assertEquals(NumberOfSolutions.NO_SOLUTIONS, puzzleResponse.getNumberOfSolutions());
        assertEquals(Difficulty.INVALID, puzzleResponse.getDifficulty());
    }
}