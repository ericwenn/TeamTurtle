package com.teamturtle.infinityrun.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ericwenn on 9/23/16.
 */
public class WordRandomizer {

    private List<Word> possibleWords;
    private Random mRandomizer = new Random();

    public WordRandomizer() {
        possibleWords = new ArrayList<Word>();

        possibleWords.add( new WordImpl("Äpple", "audio/apple.wav", "emoji/1f34e.png"));
        possibleWords.add( new WordImpl("A", "audio/apple.wav", "emoji/1f1e6.png"));
        possibleWords.add( new WordImpl("Elfenbenskusten", "audio/apple.wav", "emoji/1f1ee-1f1ea.png"));
        possibleWords.add( new WordImpl("Vas", "audio/apple.wav", "emoji/1f3fa.png"));
    }

    public WordRandomizer(List<Word> possibleWords) {
        this.possibleWords = possibleWords;
    }

    public Word getNext() {

        int index = mRandomizer.nextInt(possibleWords.size());
        return possibleWords.get(index);
    }
}
