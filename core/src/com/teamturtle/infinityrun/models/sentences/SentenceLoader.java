package com.teamturtle.infinityrun.models.sentences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.words.Word;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ericwenn on 10/5/16.
 */
public class SentenceLoader {
    private final HashMap<String, SentenceList> sentenceMap;
    private final Json mJson;
    private final FileHandle mFile;

    public SentenceLoader() {
        mFile = Gdx.files.internal(PathConstants.SENTENCE_JSON_PATH);
        mJson = new Json();
        sentenceMap = read();
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, SentenceList> read() {
        return mJson.fromJson(HashMap.class, SentenceList.class, mFile);

    }


    public List<? extends Sentence> getSentences(Word word) {
        return sentenceMap.get( Integer.toString(word.getId())).getSentences();
    }
}
