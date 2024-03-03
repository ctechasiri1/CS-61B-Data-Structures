package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.*;

public class PatternAwareLetterFreqGuesser implements Guesser {
    private final List<String> words;

    public PatternAwareLetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }

    /** Returns the most common letter in the set of valid words based on the current
     *  PATTERN. */
    public List <String> getFreeMapThatMatchesPattern(String pattern) {
        List<String> FreeMapThatMatchesPattern = new ArrayList<>();
        for (String word : words) {
            if (word.length() == pattern.length()) {
                boolean valid_word = true;
                for (int count = 0; count < word.length(); count ++) {
                    char char_letter = word.charAt(count);
                    char char_pattern = pattern.charAt(count);
                    if (char_pattern != '-' && char_pattern != '-' && char_pattern != char_letter) {
                        valid_word = false;
                        break;
                    }
                }
                if (valid_word) {
                    FreeMapThatMatchesPattern.add(word);
                }
            }
        }
        return FreeMapThatMatchesPattern;
    }

    public Map<Character, Integer> getFrequencyMap(String pattern) {
        Map<Character, Integer> Match = new TreeMap<>();
        for (String word : getFreeMapThatMatchesPattern(pattern)) {
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

    public char getGuess(String pattern, List<Character> guesses) {
        Map<Character, Integer> letterDict = this.getFrequencyMap(pattern);
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
        PatternAwareLetterFreqGuesser palfg = new PatternAwareLetterFreqGuesser("data/example.txt");
        System.out.println(palfg.getGuess("-e--", List.of('e')));
    }
}