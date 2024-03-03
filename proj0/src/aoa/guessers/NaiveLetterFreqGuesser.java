package aoa.guessers;

import aoa.utils.FileUtils;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class NaiveLetterFreqGuesser implements Guesser {
    private final List<String> words;

    public NaiveLetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }

    @Override
    /** Makes a guess which ignores the given pattern. */
    public char getGuess(String pattern, List<Character> guesses) {
        return getGuess(guesses);
    }

    /** Returns a map from a given letter to its frequency across all words.
     *  This task is similar to something you did in hw0b! */
    public Map<Character, Integer> getFrequencyMap() {
        Map<Character, Integer> Match = new HashMap<>();
        for (String word : words) {
            for (int count = 0; count < word.length(); count ++) {
                char letter = word.charAt(count);
                if (Match.containsKey(letter)) {
                    int new_count = Match.get(letter);
                    Match.put(letter, new_count + 1);
                }
                else {
                    Match.put(letter, 1);
                }
            }
        }
        return Match;
    }

    /** Returns the most common letter in WORDS that has not yet been guessed
     *  (and therefore isn't present in GUESSES). */
    public char getGuess(List<Character> guesses) {
        Map<Character, Integer> letterDict = this.getFrequencyMap();
        int max_value = 0;
        char key = ' ';
        for (Map.Entry<Character, Integer> newDict : letterDict.entrySet()) {
            if (newDict.getValue() > max_value && !guesses.contains(newDict.getKey())) {
                key = newDict.getKey();
                max_value = newDict.getValue();
            }
        }
        return key;
    }

    public static void main(String[] args) {
        NaiveLetterFreqGuesser nlfg = new NaiveLetterFreqGuesser("data/example.txt");
        System.out.println("list of words: " + nlfg.words);
        System.out.println("frequency map: " + nlfg.getFrequencyMap());

        List<Character> guesses = List.of('e', 'l');
        System.out.println("guess: " + nlfg.getGuess(guesses));
    }
}
