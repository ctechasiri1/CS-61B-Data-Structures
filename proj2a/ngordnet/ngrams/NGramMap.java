package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 * <p>
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    private HashMap<String, TimeSeries> wordMap;
    private HashMap<Integer, TimeSeries> countMap;


    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        this.wordMap = new HashMap<>();
        this.countMap = new HashMap<>();

        In words = new In(wordsFilename);

        while (words.hasNextLine()) {
            String readAndSplitLine = words.readLine();

            String[] fields = readAndSplitLine.split("\t");
            String firstItemInFileWords = fields[0];
            int secondItemInFile = Integer.parseInt(fields[1]);
            double thirdItemInFile = Double.parseDouble(fields[2]);

            TimeSeries seriesForWords = wordMap.get(firstItemInFileWords);
            if (seriesForWords == null) {
                seriesForWords = new TimeSeries();
                wordMap.put(firstItemInFileWords, seriesForWords);
            }
            seriesForWords.put(secondItemInFile, thirdItemInFile);
        }

        In counts = new In(countsFilename);

        while (counts.hasNextLine()) {
            String readAndSplitLine = counts.readLine();

            String[] fields = readAndSplitLine.split(",");
            int firstItemInFileCounts = Integer.parseInt(fields[0]);
            double secondItemInFileCounts = Double.parseDouble(fields[1]);

            if (firstItemInFileCounts < MIN_YEAR || firstItemInFileCounts > MAX_YEAR) {
                continue;
            }

            TimeSeries seriesForCounts = countMap.get(firstItemInFileCounts);
            if (seriesForCounts == null) {
                seriesForCounts = new TimeSeries();
                countMap.put(firstItemInFileCounts, seriesForCounts);
            }
            seriesForCounts.put(firstItemInFileCounts, secondItemInFileCounts);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries toCountHistory = new TimeSeries();
        TimeSeries seriesForWord = wordMap.get(word);

        if (seriesForWord != null) {
            for (int i = startYear; i <= endYear; i++) {
                Double countForYear = seriesForWord.get(i);
                if (countForYear != null) {
                    toCountHistory.put(i, countForYear);
                }
            }
        }
        return new TimeSeries(toCountHistory, startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries totalHistoryCount = new TimeSeries();

        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            double sum = 0.0;
            if (countMap.containsKey(i)) {
                TimeSeries ts = countMap.get(i);
                for (int j = 0; j < ts.size(); j++) {
                    sum += ts.get(i);
                }
            }
            if (sum != 0.0) {
                totalHistoryCount.put(i, sum);
            }
        }
        return totalHistoryCount;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries frequency = new TimeSeries();
        TimeSeries historyOfWord = countHistory(word, startYear, endYear);
        TimeSeries totalNumberOfWords = totalCountHistory();
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            Double countsOfWords = historyOfWord.get(i);
            Double totalCountsOfWords = totalNumberOfWords.get(i);
            if (countsOfWords != null && totalCountsOfWords != null && totalCountsOfWords > 0) {
                double freq = countsOfWords / totalCountsOfWords;
                frequency.put(i, freq);
            }
        }
        return frequency;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries relativeFrequency = new TimeSeries();
        TimeSeries historyOfWord = countHistory(word);
        TimeSeries totalNumberOfWords = totalCountHistory();

        if (historyOfWord.size() == 0) {
            return relativeFrequency;
        }
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            Double countsOfWord = historyOfWord.get(i);
            Double totalCountsOfWords = totalNumberOfWords.get(i);
            if (countsOfWord != null && totalCountsOfWords != null && totalCountsOfWords > 0) {
                double freq = countsOfWord / totalCountsOfWords;
                relativeFrequency.put(i, freq);
            }
        }
        return relativeFrequency;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries summedRelativeFrequency = new TimeSeries();
        Map<Integer, Double> yearlySums = new HashMap<>();
        for (int i = startYear; i <= endYear; i++) {
            double yearlySum = 0.0;
            for (String word : words) {
                if (weightHistory(word).containsKey(i)) {
                    yearlySum += weightHistory(word).get(i);
                }
            }
            yearlySums.put(i, yearlySum);
        }
        System.out.println(summedRelativeFrequency);
        for (int i = startYear; i <= endYear; i++) {
            if (yearlySums.get(i) != 0.0) {
                summedRelativeFrequency.put(i, yearlySums.get(i));
            }
        }
        //System.out.println(summedRelativeFrequency);
        return summedRelativeFrequency;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries summedRelativeFrequency = new TimeSeries();
        Map<Integer, Double> yearlySums = new HashMap<>();
        for (String word : words) {
            Map<Integer, Double> wordWeights = weightHistory(word);
            for (Map.Entry<Integer, Double> entry : wordWeights.entrySet()) {
                Integer firstItemInWordWeights = entry.getKey();
                Double secondItemInWordWeights = entry.getValue();
                Double thirdItemInWordWeights = yearlySums.getOrDefault(firstItemInWordWeights, 0.0);
                yearlySums.put(firstItemInWordWeights, thirdItemInWordWeights + secondItemInWordWeights);
            }
        }
        for (Map.Entry<Integer, Double> entry : yearlySums.entrySet()) {
            Integer firstItemInWordWeights = entry.getKey();
            Double secondItemInWordWeights = entry.getValue();
            summedRelativeFrequency.put(firstItemInWordWeights, secondItemInWordWeights);
        }
        return summedRelativeFrequency;
    }
}
