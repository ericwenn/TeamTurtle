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
    private HashMap<String, List<Sentence>> sentenceMap;
    private Json mJson;
    private FileHandle mFile;

    public SentenceLoader() {
        mFile = Gdx.files.internal(PathConstants.SENTENCE_JSON_PATH);
        mJson = new Json();
        sentenceMap = read();
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, List<Sentence>> read() {
        return mJson.fromJson(HashMap.class, List.class, mFile);

    }


    public List<Sentence> getSentences(Word word) {
        return sentenceMap.get( Integer.toString(word.getId()));
    }
}
