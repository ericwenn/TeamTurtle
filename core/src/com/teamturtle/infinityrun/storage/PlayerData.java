package com.teamturtle.infinityrun.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.teamturtle.infinityrun.models.level.Level;
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

    private FileHandle mLevelProgressFile;
    private Map<String,Integer> mLevelProgress;

    public PlayerData() {

        mWordsCollectedFile = Gdx.files.local(STORAGE_DIR + PATH_SEPARATOR + WORDS_COLLECTED_FILE);
        mWordsCollected = readWordsCollectedFromFile(mWordsCollectedFile);


        mLevelProgressFile = Gdx.files.local(STORAGE_DIR + PATH_SEPARATOR + LEVEL_PROGRESS_FILE);
        mLevelProgress = readLevelProgressFromFile(mLevelProgressFile);


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

    
    public boolean hasPlayerCollectedWord(Word word) {
        return mWordsCollected.contains( word.getId() );
    }

    public void playerCollectedWord(Word word) {
        if (!hasPlayerCollectedWord(word)) {
            mWordsCollected.add(word.getId());
            writeWordsCollectedToFile(mWordsCollectedFile, mWordsCollected);
        }
    }


    @SuppressWarnings("unchecked")
    private Map<String, Integer> readLevelProgressFromFile(FileHandle fileHandle) {
        if( !fileHandle.exists()) {
            return new HashMap<String, Integer>();
        } else {
            Json json = new Json();
            return json.fromJson(HashMap.class, Integer.class, fileHandle);
        }
    }

    public int getPlayerProgressOnLevel(Level level) {
        if(mLevelProgress.containsKey(Integer.toString(level.getId()))) {
            return mLevelProgress.get(Integer.toString(level.getId()));
        } else {
            return 0;
        }
    }

    public void setPlayerProgressOnLevel(Level level, int score) {

        if(mLevelProgress.containsKey(Integer.toString(level.getId()))) {
            mLevelProgress.remove(Integer.toString(level.getId()));
        }
        mLevelProgress.put(Integer.toString(level.getId()), score);
        writeLevelProgressToFile(mLevelProgressFile, mLevelProgress);
    }

    private void writeLevelProgressToFile(FileHandle mLevelProgressFile, Map<String, Integer> mLevelProgress) {
        Json json = new Json();
        json.toJson(mLevelProgress, HashMap.class, Integer.class, mLevelProgressFile);
    }


}
