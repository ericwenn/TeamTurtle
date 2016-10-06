package com.teamturtle.infinityrun.models;

import com.teamturtle.infinityrun.models.words.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ericwenn on 10/3/16.
 */
public class MissionImpl implements Mission {

    private List<Word> mWords = new ArrayList<Word>();
    private Word mCorrectWord = null;

    private Random mRandomizer = new Random();

    private float startX;
    private float endX;

    public MissionImpl(float startX, float endX) {
        this.startX = startX;
        this.endX = endX;
    }

    public boolean haveWord(Word word) {
        return mWords.contains(word);
    }

    public void addWord(Word word) {
        mWords.add(word);
    }

    @Override
    public boolean isCorrectWord(Word word) {
        return mCorrectWord != null && mCorrectWord.equals(word);
    }

    @Override
    public Word getCorrectWord() {
        return mCorrectWord;
    }

    @Override
    public float getStartPosition() {
        return startX;
    }

    @Override
    public float getEndPosition() {
        return endX;
    }

    public void decideCorrectWord() {

        int nWords = mWords.size();
        int correctWordIndex = mRandomizer.nextInt(nWords);

        mCorrectWord = mWords.get( correctWordIndex );
    }
}
