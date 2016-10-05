package com.teamturtle.infinityrun.models.sentences;

/**
 * Created by ericwenn on 10/5/16.
 */
public class SentenceImpl implements Sentence {
    private String text;

    public SentenceImpl() {
    }

    @Override
    public String getText() {
        return text;
    }
}
