package com.teamturtle.infinityrun.models.words;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.teamturtle.infinityrun.PathConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Text om klassen
 *
 * @author David Söderberg
 * @since 2016-10-04
 */

public class WordLoader {
    private HashMap<String, Word> wordMap;

    public WordLoader() {
        FileHandle file = Gdx.files.internal(PathConstants.WORD_JSON_PATH);
        Json json = new Json();
        wordMap = json.fromJson(HashMap.class, WordImpl.class, file);
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

    public ArrayList<WordImpl> getAllWords() {
        ArrayList<WordImpl> allWordsList = new ArrayList<WordImpl>();
        for (Word word : wordMap.values()) {
            allWordsList.add((WordImpl)word);
        }
        sortWordByCategory(allWordsList);
        return allWordsList;
    }

    private void sortWordByCategory(List<WordImpl> words) {
        Collections.sort(words, new WordCategoryComparator());

        for (Word word : words) {
            //System.out.println(word.getCategory());
        }
    }


    private static class WordCategoryComparator implements Comparator<WordImpl>, Serializable {
        @Override
        public int compare(WordImpl o1, WordImpl o2) {
            if (o1.getCategory() < o2.getCategory()) {
                return -1;
            }else{
                return 1;
            }
        }
    }
}
