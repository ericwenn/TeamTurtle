package com.teamturtle.infinityrun.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.teamturtle.infinityrun.models.Word;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    private static final String STORAGE_DIR = ".TeamTurtle";
    private static final String PATH_SEPARATOR = "/";

    private static final String WORDS_COLLECTED_FILE = "words_collected.json";
    private static final String LEVEL_PROGRESS_FILE = "level_progress.json";

    private FileHandle mWordsCollectedFile;
    private List<Integer> mWordsCollected;

    public PlayerData() {

        mWordsCollectedFile = Gdx.files.local(STORAGE_DIR + PATH_SEPARATOR + WORDS_COLLECTED_FILE);
        mWordsCollected = readWordsCollectedFromFile(mWordsCollectedFile);

    }


    @SuppressWarnings("unchecked")
    private List<Integer> readWordsCollectedFromFile(FileHandle fileHandle) {
        if (!mWordsCollectedFile.exists()) {
            return new ArrayList<Integer>();
        } else {
            Json json = new Json();
            return json.fromJson(List.class, Integer.class, mWordsCollectedFile);
        }
    }



    public boolean playerHasCollectedWord(Word word) {

    }
    public void playerCollectedWord(Word word) {

    }





}
