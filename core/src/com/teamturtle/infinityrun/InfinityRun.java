package com.teamturtle.infinityrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.level.LevelDataHandler;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.screens.AbstractScreen;
import com.teamturtle.infinityrun.screens.DictionaryScreen;
import com.teamturtle.infinityrun.screens.GameScreen;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.screens.LevelSelectScreen;
import com.teamturtle.infinityrun.screens.LoadingScreen;
import com.teamturtle.infinityrun.screens.QuizScreen;
import com.teamturtle.infinityrun.screens.StartScreen;
import com.teamturtle.infinityrun.screens.endscreens.GameOverScreen;
import com.teamturtle.infinityrun.screens.endscreens.GameWonScreen;
import com.teamturtle.infinityrun.sound.GameMusic;
import com.teamturtle.infinityrun.storage.PlayerData;

import java.util.List;

public class InfinityRun extends Game implements IScreenObserver {


    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float PPM = 75;

    private SpriteBatch mSpriteBatch;
    private PlayerData mPlayerData;
    private LevelDataHandler levelHandler;

    @Override
    public void create() {
        
        mPlayerData = new PlayerData();
        setSpriteBatch(new SpriteBatch());
        levelHandler = new LevelDataHandler();

        try {
            changeScreen(ScreenID.LOADING_SCREEN);
            GameMusic.THEME_1.playMusicLooping();
        } catch (Exception e) {
            Gdx.app.error("InfinityRun", "Could not change screen", e);
        }
    }

    private void setSpriteBatch(SpriteBatch sb) {
        this.mSpriteBatch = sb;
    }

    private SpriteBatch getSpriteBatch() {
        return this.mSpriteBatch;
    }

    @Override
    public void dispose() {
        getSpriteBatch().dispose();
    }

    @Override
    public void playLevel(Level level) {
        changeScreen(new GameScreen(getSpriteBatch(), this, level));
    }

    @Override
    public void levelCompleted(Level level, List<Word> oldWords, List<Word> discoveredWords, int score) {
        if (score > 0) {
            if (oldWords.size() > 0 || discoveredWords.size() > 0) {
                changeScreen(new QuizScreen(getSpriteBatch(), this, level, oldWords, discoveredWords, score));
            } else levelWon(level, oldWords, discoveredWords, score);
        } else {
            levelFailed(level);
        }
    }

    @Override
    public void levelWon(Level level, List<Word> collectedWords, List<Word> newWords, int score) {
        if (mPlayerData != null) {
            mPlayerData.setPlayerProgressOnLevel(level, score);
        }
        changeScreen(new GameWonScreen(getSpriteBatch(), this, level, collectedWords, newWords, score));
    }

    @Override
    public void levelFailed(Level level) {
        changeScreen(new GameOverScreen(getSpriteBatch(), this, level));
    }

    @Override
    public void playLevelAfterThis(Level level) {
        Level nextLevel = null;
        if (levelHandler != null) {
            nextLevel = levelHandler.getLevel(level.getId() + 1);
        }
        if (nextLevel != null) {
            playLevel(nextLevel);
        }
    }

    @Override
    public void changeScreen(ScreenID screen) throws Exception {
        AbstractScreen newScreen = null;

        switch (screen) {
            case MAIN_MENU:
                newScreen = new StartScreen(getSpriteBatch(), this);
                break;

            case LEVELS_MENU:
                if (levelHandler != null && mPlayerData != null) {
                    List<Level> levels = levelHandler.getLevels();
                    newScreen = new LevelSelectScreen(getSpriteBatch(), this, levels, mPlayerData);
                }
                break;

            case DICTIONARY:
                newScreen = new DictionaryScreen(getSpriteBatch(), this);
                break;

            case LOADING_SCREEN:
                newScreen = new LoadingScreen(getSpriteBatch(), this);
                break;

            default:
                throw new Exception("Unknown screen enum");
        }
        if (newScreen != null) {
            changeScreen(newScreen);
        }

    }

    private void changeScreen(AbstractScreen newScreen) {

        Screen oldScreen = getScreen();

        // Set the new screen
        newScreen.buildStage();
        setScreen(newScreen);

        //Cant dispose oldScreen when you launch the application
        if(oldScreen != null) {
            // Dispose the old one
            oldScreen.dispose();
        }
    }

    public enum ScreenID {
        MAIN_MENU, LEVELS_MENU, DICTIONARY, LOADING_SCREEN
    }
}
