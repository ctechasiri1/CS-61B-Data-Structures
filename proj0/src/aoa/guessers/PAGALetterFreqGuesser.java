package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PAGALetterFreqGuesser implements Guesser {
    private final List<String> words;

    public PAGALetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }


    /** Returns the most common letter in the set of valid words based on the current
     *  PATTERN and the GUESSES that have been made. */

    public List <String> getFreeMapThatMatchesPattern(String pattern, List<Character> guesses) {
        List<String> FreeMapThatMatchesPattern = new ArrayList<>();
        for (String word : words) {
            if (word.length() == pattern.length()) {
                boolean valid_word = true;
                for (int count = 0; count < word.length(); count ++) {
                    char char_letter = word.charAt(count);
                    char char_pattern = pattern.charAt(count);
                    if (char_pattern != '-' && char_letter != char_pattern) {
                        valid_word = false;
                        break;
                    } else if (char_pattern == '-' && guesses.contains(char_letter)) {
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

    public Map<Character, Integer> getFrequencyMap(String pattern, List<Character> guesses) {
        Map<Character, Integer> Match = new TreeMap<>();
        for (String word : getFreeMapThatMatchesPattern(pattern, guesses)) {
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
        Map<Character, Integer> letterDict = this.getFrequencyMap(pattern, guesses);
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
        PAGALetterFreqGuesser pagalfg = new PAGALetterFreqGuesser("data/example.txt");
        System.out.println(pagalfg.getGuess("----", List.of('e')));
    }
}
