package com.teamturtle.infinityrun.models.sentences;

import java.util.List;

/**
 * Created by ericwenn on 10/5/16.
 */
public class SentenceList {
    public List<SentenceImpl> sentences;

    public SentenceList() {
    }

    public List<? extends Sentence> getSentences() {
        return sentences;
    }
}
