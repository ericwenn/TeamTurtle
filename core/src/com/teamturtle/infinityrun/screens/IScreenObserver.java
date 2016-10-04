package com.teamturtle.infinityrun.screens;

import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.level.Level;

import java.util.List;

/**
 * Created by ostmos on 2016-09-28.
 */
public interface IScreenObserver {

    void changeScreen(InfinityRun.ScreenID id) throws Exception;

    void playLevel(Level level);

    void levelCompleted(Level level, List<Word> missionWords, int score);

    void levelWon(Level level, int score);

    void levelFailed(Level level);

    void playLevelAfterThis(Level level);

}