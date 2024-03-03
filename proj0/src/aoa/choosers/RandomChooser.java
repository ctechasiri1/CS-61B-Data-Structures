package aoa.choosers;

import edu.princeton.cs.algs4.StdRandom;
import aoa.utils.FileUtils;
import org.checkerframework.checker.units.qual.A;
import org.objectweb.asm.tree.analysis.Value;

import java.util.ArrayList;
import java.util.List;

import static aoa.utils.FileUtils.readWordsOfLength;

public class RandomChooser implements Chooser {
    private final String chosenWord;
    private String pattern;

    public List<String> words;

    public RandomChooser(int wordLength, String dictionaryFile) {
        List<String> words = new ArrayList<>(readWordsOfLength(dictionaryFile, wordLength));
        if (wordLength < 1) {
            throw new IllegalArgumentException("The word has to be larger than one letter");
        }
        else if (words.isEmpty()) {
            throw new IllegalStateException("The list can't be empty");
        }

        int numWords = words.size();
        int randomlyChosenWordNumber = StdRandom.uniform(numWords);
        chosenWord = words.get(randomlyChosenWordNumber);
        pattern = "";
        for (int count = 0; count < chosenWord.length(); count ++) {
            pattern += "-";
        }
    }

    @Override
    public int makeGuess(char letter) {
        int occurs = 0;
        char[] new_pat = pattern.toCharArray();
        for (int count = 0; count < chosenWord.length(); count ++) {
            if (chosenWord.charAt(count) == letter) {
                occurs ++;
                new_pat[count] = letter;
            }
        }
        pattern = new String(new_pat);
        return occurs;
    }

    @Override
    public String getPattern() {

        return pattern;
    }

    @Override
    public String getWord() {
        return chosenWord;
    }
}
