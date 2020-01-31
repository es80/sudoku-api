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
import sudoku.logic.Difficulty;
import sudoku.logic.Generator;
import sudoku.logic.Grid;
import sudoku.logic.Puzzle;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller("/generate")
public class GenerateController {

    private Logger log = LoggerFactory.getLogger(GenerateController.class);

    final int MIN_DIMENSION = 4;
    final int MAX_DIMENSION = 10;

    @Get("/{boxHeight}/{boxWidth}/{difficulty}")
    public PuzzleResponse generate(@Min(2) @Max(5) int boxHeight,
                                   @Min(2) @Max(5) int boxWidth,
                                   Difficulty difficulty) throws IllegalArgumentException {

        log.trace("Received request generate");

        if (boxHeight * boxWidth < MIN_DIMENSION || boxHeight * boxWidth > MAX_DIMENSION) {
            throw new IllegalArgumentException("The boxHeight multiplied by boxWidth must be at " +
                    "least " + MIN_DIMENSION + " and no greater than " + MAX_DIMENSION + ".");
        }

        String err = "Unable to generate puzzle for boxHeight " + boxHeight + ", boxWidth "
                + boxWidth + ", difficulty " + difficulty;
        if (boxHeight == 2 && boxWidth == 2 && difficulty != Difficulty.EASY) {
            throw new IllegalArgumentException(err);
        } else if (boxHeight * boxWidth == 6 && (difficulty != Difficulty.EASY && difficulty != Difficulty.MEDIUM
                && difficulty != Difficulty.TRICKY)) {
            throw new IllegalArgumentException(err);
        } else if (difficulty != Difficulty.EASY && difficulty != Difficulty.MEDIUM && difficulty != Difficulty.TRICKY
                && difficulty != Difficulty.FIENDISH) {
            throw new IllegalArgumentException(err);
        }

        Puzzle puzzle = new Generator(new Grid(boxHeight, boxWidth)).generatePuzzle(difficulty);

        NumberOfSolutions numberOfSolutions;
        if (puzzle.getNumSolutions() == 1) {
            numberOfSolutions = NumberOfSolutions.SINGLE_SOLUTION;
        } else if (puzzle.getNumSolutions() > 1) {
            numberOfSolutions = NumberOfSolutions.MULTIPLE_SOLUTIONS;
        } else {
            numberOfSolutions = NumberOfSolutions.NO_SOLUTIONS;
        }

        return new PuzzleResponse().setPuzzleNums(puzzle.getPuzzleNums())
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
}