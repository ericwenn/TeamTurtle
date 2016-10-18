package com.teamturtle.infinityrun.models.words;

import java.util.List;
import java.util.Random;

/**
 * Created by ericwenn on 9/23/16.
 */
public class WordRandomizer {

    private final List<Word> possibleWords;
    private final Random mRandomizer = new Random();

    public WordRandomizer(List<Word> possibleWords) {
        this.possibleWords = possibleWords;
    }

    public Word getNext() {

        int index = mRandomizer.nextInt(possibleWords.size());
        return possibleWords.get(index);
    }
}
