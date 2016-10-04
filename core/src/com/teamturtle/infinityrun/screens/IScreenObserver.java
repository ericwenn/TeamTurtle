package com.teamturtle.infinityrun.screens;

import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.Word;
import com.teamturtle.infinityrun.models.level.Level;

import java.util.List;

/**
 * Created by ostmos on 2016-09-28.
 */
public interface IScreenObserver {

    void changeScreen(InfinityRun.ScreenID id) throws Exception;

    void playLevel(Level level);

    void levelCompleted(Level level, List<Word> missionWords, int score);

    void levelWon(int score);

    void levelFailed();

}

/*
public interface GameScreen {
    public GameScreen(Level level);
}

public interface QuizScreen {
    public QuizScreen(List<Word> possibleWords);
}
public interface WonGameScreen {
    public WonGameScreen(Level level, int score);
}

public interface LostGameScreen {
    public LostGameScreen(Level level);
}


public interface PlayerData {

    boolean haveCollectedWord(Word word);
    int scoreOnLevel(Level level);
}*/