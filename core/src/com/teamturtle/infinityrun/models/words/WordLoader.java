package com.teamturtle.infinityrun.models.words;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Text om klassen
 *
 * @author David Söderberg
 * @since 2016-10-04
 */

public class WordLoader {
    private HashMap<String, Word> wordMap;

    public WordLoader() {
        FileHandle file = Gdx.files.internal("data/words.json");
        Json json = new Json();
        wordMap = json.fromJson(HashMap.class, WordImpl.class, file.readString());
    }

    public HashMap<String, Word> getWords() {
        return wordMap;
    }

    public ArrayList<Word> getWordsFromCategory(int category) {
        ArrayList<Word> returnList = new ArrayList<Word>();
        for (Word word : getWords().values()) {
            if (word.getCategory() == category) {
                returnList.add(word);
            }
        }
        return returnList;
    }
}
