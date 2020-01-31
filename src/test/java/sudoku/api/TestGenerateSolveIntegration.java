package sudoku.api;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.logic.Difficulty;

import javax.inject.Inject;

import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.GET;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
Try generating a singe puzzle of each valid grid size and difficulty, then solve the puzzle and check responses match.
*/
@MicronautTest
public class TestGenerateSolveIntegration {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    /*
    Possible valid grid sizes and difficulties for generating and solving puzzles are:
    2x2      EASY only
    2x3 3x2  no FIENDISH
    2x4 4x2  all difficulties
    3x3      all difficulties
    2x5 5x2  all difficulties
    */
    private static Stream<Arguments> provideAllValidGenerateCombinations() {
        return Stream.of(
                Arguments.of(2, 2, Difficulty.EASY),

                Arguments.of(2, 3, Difficulty.EASY),
                Arguments.of(3, 2, Difficulty.EASY),
                Arguments.of(2, 3, Difficulty.MEDIUM),
                Arguments.of(3, 2, Difficulty.MEDIUM),
                Arguments.of(2, 3, Difficulty.TRICKY),
                Arguments.of(3, 2, Difficulty.TRICKY),

                Arguments.of(2, 4, Difficulty.EASY),
                Arguments.of(4, 2, Difficulty.EASY),
                Arguments.of(2, 4, Difficulty.MEDIUM),
                Arguments.of(4, 2, Difficulty.MEDIUM),
                Arguments.of(2, 4, Difficulty.TRICKY),
                Arguments.of(4, 2, Difficulty.TRICKY),
                Arguments.of(2, 4, Difficulty.FIENDISH),
                Arguments.of(4, 2, Difficulty.FIENDISH),

                Arguments.of(2, 5, Difficulty.EASY),
                Arguments.of(5, 2, Difficulty.EASY),
                Arguments.of(2, 5, Difficulty.MEDIUM),
                Arguments.of(5, 2, Difficulty.MEDIUM),
                Arguments.of(2, 5, Difficulty.TRICKY),
                Arguments.of(5, 2, Difficulty.TRICKY),
                Arguments.of(2, 5, Difficulty.FIENDISH),
                Arguments.of(5, 2, Difficulty.FIENDISH),

                Arguments.of(3, 3, Difficulty.EASY),
                Arguments.of(3, 3, Difficulty.EASY),
                Arguments.of(3, 3, Difficulty.MEDIUM),
                Arguments.of(3, 3, Difficulty.MEDIUM),
                Arguments.of(3, 3, Difficulty.TRICKY),
                Arguments.of(3, 3, Difficulty.TRICKY),
                Arguments.of(3, 3, Difficulty.FIENDISH),
                Arguments.of(3, 3, Difficulty.FIENDISH)
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
    @MethodSource("provideAllValidGenerateCombinations")
    public void generateThenSolve(int boxHeight, int boxWidth, Difficulty difficulty) {
        String uri = "/generate/" + boxHeight + "/" + boxWidth + "/" + difficulty;
        Flowable<PuzzleResponse> response = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse generated = response.blockingFirst();

        uri = "/solve/" + boxHeight + "/" + boxWidth + "/" + puzzleNumsAsString(generated.getPuzzleNums());
        Flowable<PuzzleResponse> response2 = (Flowable<PuzzleResponse>) client.retrieve(GET(uri), PuzzleResponse.class);
        PuzzleResponse solved = response2.blockingFirst();

        assertArrayEquals(generated.getPuzzleNums(), solved.getPuzzleNums());
        assertArrayEquals(generated.getSolution(), solved.getSolution());
        assertEquals(NumberOfSolutions.SINGLE_SOLUTION, generated.getNumberOfSolutions());
        assertEquals(NumberOfSolutions.SINGLE_SOLUTION, solved.getNumberOfSolutions());
        assertEquals(difficulty, generated.getDifficulty());
        assertEquals(difficulty, solved.getDifficulty());
    }
}