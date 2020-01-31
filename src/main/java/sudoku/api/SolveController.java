package sudoku.api;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.logic.Grid;
import sudoku.logic.Puzzle;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Controller("/solve")
public class SolveController {

    final int MIN_DIMENSION = 4;
    final int MAX_DIMENSION = 10;

    private Logger log = LoggerFactory.getLogger(GenerateController.class);

    @Get("/{boxHeight}/{boxWidth}/{puzzleNums}")
    public PuzzleResponse solve(@Min(2) @Max(5) int boxHeight,
                                @Min(2) @Max(5) int boxWidth,
                                String puzzleNums) throws IllegalArgumentException {

        if (boxHeight * boxWidth < MIN_DIMENSION || boxHeight * boxWidth > MAX_DIMENSION) {
            throw new IllegalArgumentException("The boxHeight multiplied by boxWidth must be at " +
                    "least " + MIN_DIMENSION + " and no greater than " + MAX_DIMENSION + ".");
        }

        int[] puzzleNumsArray = parsePuzzleNumsString(puzzleNums).stream().mapToInt(i -> i).toArray();

        Grid grid = new Grid(boxHeight, boxWidth);
        if (puzzleNumsArray.length != grid.getNumCells()) {
            throw new IllegalArgumentException("There must be exactly (boxHeight x boxWidth) squared "
                    + "many puzzle numbers.");
        }
        for (int i = 0; i < grid.getNumCells(); i++) {
            if (puzzleNumsArray[i] < 0 || puzzleNumsArray[i] > grid.getNumDigits()) {
                throw new IllegalArgumentException("The puzzle numbers must only contain digits "
                        + "between 0 and " + grid.getNumDigits() + " inclusively.");
            }
        }

        Puzzle puzzle = new Puzzle(grid, puzzleNumsArray);
        NumberOfSolutions numberOfSolutions;
        if (puzzle.getNumSolutions() == 1) {
            numberOfSolutions = NumberOfSolutions.SINGLE_SOLUTION;
        } else if (puzzle.getNumSolutions() > 1) {
            numberOfSolutions = NumberOfSolutions.MULTIPLE_SOLUTIONS;
        } else {
            numberOfSolutions = NumberOfSolutions.NO_SOLUTIONS;
        }

        return new PuzzleResponse().setPuzzleNums(puzzleNumsArray)
                .setSolution(puzzle.getSolution())
                .setNumberOfSolutions(numberOfSolutions)
                .setDifficulty(puzzle.getDifficulty());
    }

    @Error
    public HttpResponse<JsonError> invalidRequest(HttpRequest request, IllegalArgumentException ex) {
        JsonError error = new JsonError("Bad request: " + ex.getMessage())
                .link(Link.SELF, Link.of(request.getUri()));
        return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST).body(error);
    }

    private List<Integer> parsePuzzleNumsString(String puzzleNumsString) {
        // Acceptable strings of puzzle numbers:
        // 10506402 etc.
        // 1.5.64.2 etc.
        // 1,0,5,0,6,4,0,2, etc.
        String[] split = puzzleNumsString.split(",");
        List<Integer> puzzleNums = new ArrayList<>();
        if (split.length ==  1) {
            for (int i = 0; i < puzzleNumsString.length(); i++) {
                char c = puzzleNumsString.charAt(i);
                if (c == '.') {
                    puzzleNums.add(0);
                } else {
                    puzzleNums.add(Character.getNumericValue(c));
                }
            }
        } else {
            for (String s : split) {
                puzzleNums.add(Integer.parseInt(s));
            }
        }
        return puzzleNums;
    }
}