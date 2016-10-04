package com.teamturtle.infinityrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teamturtle.infinityrun.models.Word;

public class PlayerData {

    private static String STORAGE_DIR = ".TeamTurtle";
    private static String PATH_SEPARATOR = "/";

    private static String WORDS_COLLECTED_FILE = "words_collected.json";
    private static String LEVEL_PROGRESS_FILE = "level_progress.json";

    private FileHandle mWordsCollectedFile;

    public PlayerData() {
        mWordsCollectedFile = Gdx.files.local( STORAGE_DIR + PATH_SEPARATOR + WORDS_COLLECTED_FILE);
        Gdx.app.log("PlayerData", mWordsCollectedFile.toString());
    }



    public boolean playerHasCollectedWord(Word word) {
        return true;
    }




}
