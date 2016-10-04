package com.teamturtle.infinityrun.models;


import com.teamturtle.infinityrun.models.words.Word;

/**
 * A mission is a portion of the game map, which contains n=[1,3] unique words.
 * One of these words is the correct one, that the user should try to catch.
 */
public interface Mission {

    boolean isCorrectWord(Word word);

    Word getCorrectWord();

    float getStartPosition();

    float getEndPosition();

    boolean haveWord(Word word);

    void addWord(Word word);

    void decideCorrectWord();
}
