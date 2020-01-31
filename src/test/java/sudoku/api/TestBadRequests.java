package sudoku.api;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.logic.Difficulty;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.GET;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
Try an assortment of badly formed requests to /solve and /generate and check HttpClientResponseException thrown.
*/
@MicronautTest
public class TestBadRequests {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    private static Stream<Arguments> provideSomeInvalidGenerateCombinations() {
        return Stream.of(
                Arguments.of(2, 2, Difficulty.MEDIUM),
                Arguments.of(2, 3, Difficulty.FIENDISH),
                Arguments.of(2, 4, Difficulty.UNRATED),
                Arguments.of(2, 5, Difficulty.TOO_DIFFICULT),
                Arguments.of(3, 3, Difficulty.INVALID),
                Arguments.of(3, 3, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSomeInvalidGenerateCombinations")
    public void badGenerateDifficultyRequests(int boxHeight, int boxWidth, Difficulty difficulty) {
        String uri = "/generate/" + boxHeight + "/" + boxWidth + "/" + difficulty;
        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }

    private static Stream<Arguments> someInvalidGridSizes() {
        return Stream.of(
                Arguments.of(-2, 2),
                Arguments.of(3, -1),
                Arguments.of(0, 0),
                Arguments.of(0, 2),
                Arguments.of(4, 0),
                Arguments.of(2, 1),
                Arguments.of(1, 5),
                Arguments.of(3, 4),
                Arguments.of(4, 5),
                Arguments.of(2, 6)
        );
    }

    @ParameterizedTest
    @MethodSource("someInvalidGridSizes")
    public void badGenerateGridSizeRequests(int boxHeight, int boxWidth) {
        String uri = "/generate/" + boxHeight + "/" + boxWidth + "/" + Difficulty.EASY;
        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }

    @ParameterizedTest
    @MethodSource("someInvalidGridSizes")
    void badSolveGridSizeRequests(int boxHeight, int boxWidth) {
        int numCells = boxHeight * boxWidth * boxHeight * boxWidth;
        int[] emptyPuzzle = new int[numCells];
        Arrays.fill(emptyPuzzle, 0);
        String uri = "/solve/" + boxHeight + "/" + boxWidth + "/" + puzzleNumsAsString(emptyPuzzle);

        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }

    private static Stream<Arguments> allValidGridSizes() {
        return Stream.of(
                Arguments.of(2, 2),
                Arguments.of(2, 3),
                Arguments.of(3, 2),
                Arguments.of(2, 4),
                Arguments.of(4, 2),
                Arguments.of(2, 5),
                Arguments.of(5, 2),
                Arguments.of(3, 3)
        );
    }

    private static String puzzleNumsAsString(int[] puzzleNums) {
        StringBuilder sb = new StringBuilder();
        for (int digit : puzzleNums) {
            sb.append(digit);
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);  // remove the last comma
        }
        return sb.toString();
    }

    @ParameterizedTest
    @MethodSource("allValidGridSizes")
    void badSolveRequestTooManyPuzzleNums(int boxHeight, int boxWidth) {
        int numCells = boxHeight * boxWidth * boxHeight * boxWidth;
        int[] emptyPuzzleExtraZero = new int[numCells + 1];
        Arrays.fill(emptyPuzzleExtraZero, 0);
        String uri = "/solve/" + boxHeight + "/" + boxWidth + "/" + puzzleNumsAsString(emptyPuzzleExtraZero);

        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }

    @ParameterizedTest
    @MethodSource("allValidGridSizes")
    void badSolveRequestTooFewPuzzleNums(int boxHeight, int boxWidth) {
        int numCells = boxHeight * boxWidth * boxHeight * boxWidth;
        int[] emptyPuzzleMissingAZero = new int[numCells - 1];
        Arrays.fill(emptyPuzzleMissingAZero, 0);
        String uri = "/solve/" + boxHeight + "/" + boxWidth + "/" + puzzleNumsAsString(emptyPuzzleMissingAZero);

        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }


    @ParameterizedTest
    @MethodSource("allValidGridSizes")
    void badSolveRequestPuzzleNumsContainsTooLargeDigit(int boxHeight, int boxWidth) {
        int numCells = boxHeight * boxWidth * boxHeight * boxWidth;
        int[] puzzle = new int[numCells];
        Arrays.fill(puzzle, 0);
        puzzle[0] = boxHeight * boxWidth + 1;  // too large a digit for a valid puzzle
        String uri = "/solve/" + boxHeight + "/" + boxWidth + "/" + puzzleNumsAsString(puzzle);

        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }


    @ParameterizedTest
    @MethodSource("allValidGridSizes")
    void badSolveRequestPuzzleNumsContainsNegativeDigit(int boxHeight, int boxWidth) {
        int numCells = boxHeight * boxWidth * boxHeight * boxWidth;
        int[] puzzle = new int[numCells];
        Arrays.fill(puzzle, 0);
        puzzle[0] = -1;
        String uri = "/solve/" + boxHeight + "/" + boxWidth + "/" + puzzleNumsAsString(puzzle);

        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }

    @ParameterizedTest
    @MethodSource("allValidGridSizes")
    void badSolveRequestPuzzleNumsContainsFloat(int boxHeight, int boxWidth) {
        int numCells = boxHeight * boxWidth * boxHeight * boxWidth;
        int[] puzzle = new int[numCells - 1];
        Arrays.fill(puzzle, 0);
        String uri = "/solve/" + boxHeight + "/" + boxWidth + "/" + puzzleNumsAsString(puzzle) + ",1.2";

        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }

    @ParameterizedTest
    @MethodSource("allValidGridSizes")
    void badSolveRequestPuzzleNumsContainsNonDigitChar(int boxHeight, int boxWidth) {
        int numCells = boxHeight * boxWidth * boxHeight * boxWidth;
        int[] puzzle = new int[numCells - 1];
        Arrays.fill(puzzle, 0);
        String uri = "/solve/" + boxHeight + "/" + boxWidth + "/" + puzzleNumsAsString(puzzle) + ",D";

        assertThrows(HttpClientResponseException.class, () -> {
            Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
            PuzzleResponse generated = response.blockingFirst();
        });
    }
}