package sudoku.logic;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/*
Two methods to test the puzzle generator: sample the distribution of ratings of puzzles produced and
sample how long it takes to produce puzzles of particular target difficulties
 */
public class GeneratorStatistics {

    private static final List<String> log = new ArrayList<>();
    private static final Map<Difficulty, List<Duration>> difficultyDurations = new TreeMap<>();
    private static final Map<Integer, Map<Integer, Integer>> ratingsUnknownsCounts = new TreeMap<>();
    private static final Map<Difficulty, Integer> difficultyCounts = new TreeMap<>();
    private static final List<Puzzle> puzzlesList = new ArrayList<>();

    public static String difficultyTimeStatistics(int sampleSize) {
        /*
        For each valid grid size, and each valid difficulty rating, produce sampleSize many puzzles
        and return some summary statistics for how long it took to generate each puzzle.
         */
        generateByDifficulty(2, 2, sampleSize, Difficulty.EASY);
        generateByDifficulty(2, 3, sampleSize, Difficulty.EASY);
        generateByDifficulty(2, 3, sampleSize, Difficulty.MEDIUM);
        generateByDifficulty(2, 3, sampleSize, Difficulty.TRICKY);
        generateEachDifficulty(2, 4, sampleSize);
        generateEachDifficulty(2, 5, sampleSize);
        generateEachDifficulty(3, 3, sampleSize);

        StringBuilder sb = new StringBuilder();
        for (String s : log) {
            sb.append(s);
        }
        return sb.toString();
    }

    private static void generateEachDifficulty(int boxHeight, int boxWidth, int sampleSize) {
        generateByDifficulty(boxHeight, boxWidth, sampleSize, Difficulty.EASY);
        generateByDifficulty(boxHeight, boxWidth, sampleSize, Difficulty.MEDIUM);
        generateByDifficulty(boxHeight, boxWidth, sampleSize, Difficulty.TRICKY);
        generateByDifficulty(boxHeight, boxWidth, sampleSize, Difficulty.FIENDISH);
    }

    private static void generateByDifficulty(int boxHeight, int boxWidth, int sampleSize, Difficulty difficulty) {
        for (int n = 0; n < sampleSize; n++) {
            Instant before = Instant.now();
            Generator generator = new Generator(new Grid(boxHeight, boxWidth));
            generator.generatePuzzle(difficulty);
            Instant after = Instant.now();
            List<Duration> durations = difficultyDurations.getOrDefault(difficulty, new ArrayList<>());
            durations.add(Duration.between(before, after));
            difficultyDurations.put(difficulty, durations);
        }
        log.add(boxHeight + "x" + boxWidth + " grid, ");
        log.add("generated " + sampleSize + " puzzles of difficulty " + difficulty + ".\n");
        logStatistics(difficulty);
    }

    private static void logStatistics(Difficulty difficulty) {
        LongSummaryStatistics statistics = new LongSummaryStatistics();
        for (Duration duration : difficultyDurations.get(difficulty)) {
            statistics.accept(duration.toMillis());
        }

        double mean = statistics.getAverage();
        double sumSquares = 0;
        for (Duration duration : difficultyDurations.get(difficulty)) {
            sumSquares += Math.pow((duration.toMillis() - mean), 2);
        }
        double std = Math.sqrt(sumSquares / (statistics.getCount() - 1));

        String meanString = String.format("mean: %.2f", mean);
        String stdString = String.format("std: %.2f", std);
        log.add("Generation time statistics (ms) - \tmin: " + statistics.getMin() +
                ", \tmax: " + statistics.getMax() +
                ", \t" + meanString + ", \t" + stdString + ".\n");
    }

    public static String ratingsAndUnknownsStatistics(int sampleSize) {
        /*
        For each valid grid size, produce sampleSize many puzzles and return a string summary for
        the distribution of ratings, number of unknowns and difficulties for the generated puzzles.
         */
        for (int boxWidth = 2; boxWidth <= 5; boxWidth++) {
            generateByRating(2, boxWidth, sampleSize);
        }
        generateByRating(3, 3, sampleSize);

        StringBuilder sb = new StringBuilder();
        for (String s : log) {
            sb.append(s);
        }
        return sb.toString();
    }

    private static void generateByRating(int boxHeight, int boxWidth, int sampleSize) {
        Generator generator = new Generator(new Grid(boxHeight, boxWidth));
        ratingsUnknownsCounts.clear();
        difficultyCounts.clear();
        puzzlesList.clear();
        int count = sampleSize;

        Instant before = Instant.now();
        while (count > 0) {
            puzzlesList.add(generator.generatePuzzle(Difficulty.UNRATED));
            count--;
        }
        Instant after = Instant.now();
        Duration duration = Duration.between(before, after);

        countPuzzles();
        log.add(boxHeight + "x" + boxWidth + " grid, ");
        log.add("generated " + sampleSize + " puzzles in ");
        log.add(duration.toSeconds() + "." + duration.toMillisPart() + " seconds.\n");
        logSummary(sampleSize);
    }

    private static void countPuzzles() {
        for (Puzzle puzzle : GeneratorStatistics.puzzlesList) {
            int rating = puzzle.getRating();
            int numUnknowns = puzzle.getNumUnknowns();
            Map<Integer, Integer> unknownsCounts = ratingsUnknownsCounts.getOrDefault(rating, new TreeMap<>());
            unknownsCounts.put(numUnknowns, unknownsCounts.getOrDefault(numUnknowns, 0) + 1);
            ratingsUnknownsCounts.put(rating, unknownsCounts);

            Difficulty difficulty = puzzle.getDifficulty();
            Integer oldCount = difficultyCounts.getOrDefault(difficulty, 0);
            difficultyCounts.put(difficulty, oldCount + 1);
        }
    }

    private static void logSummary(int total) {
        log.add("\nRatings and unknowns.\n");
        int invalidCount = 0;
        if (ratingsUnknownsCounts.containsKey(0)) {
            for (Map.Entry<Integer, Integer> unknowns : ratingsUnknownsCounts.get(0).entrySet()) {
                invalidCount += unknowns.getValue();
            }
            log.add(String.format("Invalid\t\t%5.2f%% of total\t\tunknowns: ", (100.0 * invalidCount) / total));
            log.add(ratingsUnknownsCounts.get(0).entrySet().toString());
            log.add("\n");
        }

        int validCount = total - invalidCount;
        for (Map.Entry<Integer, Map<Integer, Integer>> rating : ratingsUnknownsCounts.entrySet()) {
            if (rating.getKey() == 0) {
                continue;
            }
            int count = 0;
            for (Map.Entry<Integer, Integer> unknowns : rating.getValue().entrySet()) {
                count += unknowns.getValue();
            }
            log.add(String.format("Rating %d\t%5.2f%% of valid\t\tunknowns: ",
                    rating.getKey(), (100.0 * count) / validCount));
            log.add(rating.getValue().entrySet().toString());
            log.add("\n");
        }
        log.add("\n");

        log.add("Difficulties.\n");
        for (Difficulty difficulty : difficultyCounts.keySet()) {
            if (difficulty != Difficulty.TOO_DIFFICULT) {
                log.add(String.format("Difficulty %s %.2f%% of valid.\n",
                        difficulty.toString(), (100.0 * difficultyCounts.get(difficulty)) / validCount));
            }
        }
        log.add("\n\n");
    }
}