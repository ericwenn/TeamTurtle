package com.teamturtle.infinityrun.models.words;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ericwenn on 9/23/16.
 */
public class WordRandomizer {

    private List<com.teamturtle.infinityrun.models.words.Word> possibleWords;
    private Random mRandomizer = new Random();
    private WordLoader mWordLoader = new WordLoader();

    public WordRandomizer() {
        possibleWords = new ArrayList<com.teamturtle.infinityrun.models.words.Word>();

        possibleWords.add(mWordLoader.getWords().get("1"));
        possibleWords.add(mWordLoader.getWords().get("2"));
        possibleWords.add(mWordLoader.getWords().get("3"));
        possibleWords.add(mWordLoader.getWords().get("4"));
//        possibleWords.add( new WordImpl("Äpple", "audio/apple.wav", "emoji/1f34e.png"));
//        possibleWords.add( new WordImpl("A", "audio/apple.wav", "emoji/1f1e6.png"));
//        possibleWords.add( new WordImpl("Elfenbenskusten", "audio/apple.wav", "emoji/1f1ee-1f1ea.png"));
//        possibleWords.add( new WordImpl("Vas", "audio/apple.wav", "emoji/1f3fa.png"));
    }

    public WordRandomizer(List<com.teamturtle.infinityrun.models.words.Word> possibleWords) {
        this.possibleWords = possibleWords;
    }

    public com.teamturtle.infinityrun.models.words.Word getNext() {

        int index = mRandomizer.nextInt(possibleWords.size());
        return possibleWords.get(index);
    }
}
