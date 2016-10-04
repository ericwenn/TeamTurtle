package com.teamturtle.infinityrun.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.teamturtle.infinityrun.models.words.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {

    private static final String STORAGE_DIR = ".TeamTurtle";
    private static final String PATH_SEPARATOR = "/";

    private static final String WORDS_COLLECTED_FILE = "words_collected.json";
    private static final String LEVEL_PROGRESS_FILE = "level_progress.json";

    private FileHandle mWordsCollectedFile;
    private List<Integer> mWordsCollected;

    public PlayerData() {

        mWordsCollectedFile = Gdx.files.external(STORAGE_DIR + PATH_SEPARATOR + WORDS_COLLECTED_FILE);
        mWordsCollected = readWordsCollectedFromFile(mWordsCollectedFile);

    }


    @SuppressWarnings("unchecked")
    private List<Integer> readWordsCollectedFromFile(FileHandle fileHandle) {
        if (!fileHandle.exists()) {
            return new ArrayList<Integer>();
        } else {
            Json json = new Json();
            return json.fromJson(List.class, Integer.class, fileHandle);
        }
    }

    private void writeWordsCollectedToFile(FileHandle fileHandle, List<Integer> wordsCollected) {
        Json json = new Json();
        json.toJson(wordsCollected, List.class, Integer.class, fileHandle);
    }

    
    public boolean playerHasCollectedWord(Word word) {
        return mWordsCollected.contains( word.getId() );
    }
    public void playerCollectedWord(Word word) {
        if (!playerHasCollectedWord(word)) {
            mWordsCollected.add(word.getId());
            writeWordsCollectedToFile(mWordsCollectedFile, mWordsCollected);
        }
    }


    private Map<Integer, Integer> readLevelProgressFromFile(FileHandle fileHandle) {
        if( !fileHandle.exists()) {
            return new HashMap<Integer, Integer>();
        } else {
            Json json = new Json();
            return json.fromJson(Map.class, Integer.class)
        }
    }





}
