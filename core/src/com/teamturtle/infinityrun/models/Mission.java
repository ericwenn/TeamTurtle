package com.teamturtle.infinityrun.models;


/**
 * A mission is a portion of the game map, which contains n=[1,3] unique words.
 * One of these words is the correct one, that the user should try to catch.
 */
public interface Mission {


    boolean isCorrectWord(Word word);

    int getStartPosition();

    int getEndPosition();
}