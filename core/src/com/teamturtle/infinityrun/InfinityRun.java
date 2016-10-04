package com.teamturtle.infinityrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.models.level.LevelDataHandler;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.screens.AbstractScreen;
import com.teamturtle.infinityrun.screens.DictionaryScreen;
import com.teamturtle.infinityrun.screens.GameScreen;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.screens.LevelSelectScreen;
import com.teamturtle.infinityrun.screens.QuizScreen;
import com.teamturtle.infinityrun.screens.StartScreen;
import com.teamturtle.infinityrun.screens.WordScreen;
import com.teamturtle.infinityrun.screens.level_end_screens.LostLevelScreen;
import com.teamturtle.infinityrun.screens.level_end_screens.WonLevelScreen;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import java.util.List;

public class InfinityRun extends Game implements IScreenObserver {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float PPM = 75;

    private SpriteBatch mSpriteBatch;
    private LevelDataHandler levelHandler;

    @Override
    public void create() {

        setSpriteBatch(new SpriteBatch());
        levelHandler = new LevelDataHandler();

        try {
            changeScreen(ScreenID.MAIN_MENU);
        } catch (Exception e) {
            // This cannot fail...yet
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

    public void changeScreen(Emoji emoji) throws Exception {
        AbstractScreen newScreen;
        newScreen = new WordScreen(getSpriteBatch(), this, emoji);

        Screen oldScreen = getScreen();

        // Set the new screen
        newScreen.buildStage();
        setScreen(newScreen);

        // Dispose the old one
        oldScreen.dispose();

    }

    @Override
    public void playLevel(Level level) {
        changeScreen(new GameScreen(getSpriteBatch(), this, level));
    }

    @Override
    public void levelCompleted(Level level, List<Word> missionWords, int score) {
        if (score > 0) {
            changeScreen(new QuizScreen(getSpriteBatch(), this, level, missionWords, score));
        }else{
            levelFailed(level);
        }
    }

    @Override
    public void levelWon(Level level, int score) {
        changeScreen(new WonLevelScreen(getSpriteBatch(), this, level, score));
    }

    @Override
    public void levelFailed(Level level) {
        changeScreen(new LostLevelScreen(getSpriteBatch(), this, level));
    }

    @Override
    public void playLevelAfterThis(Level level) {
        Level nextLevel = levelHandler.getLevel(level.getId() + 1);
        if (nextLevel != null) {
            playLevel(nextLevel);
        }
    }

    @Override
    public void changeScreen(ScreenID screen) throws Exception {
        AbstractScreen newScreen;

        switch (screen) {
            case MAIN_MENU:
                newScreen = new StartScreen(getSpriteBatch(), this);
                break;

            case LEVELS_MENU:
                newScreen = new LevelSelectScreen(getSpriteBatch(), this);
                break;

            case DICTIONARY:
                newScreen = new DictionaryScreen(getSpriteBatch(), this);
                break;

			case WORD:
				Emoji apple = new Emoji("Äpple","audio/apple.wav", new Texture("emoji/00a9.png"));
				newScreen = new WordScreen(getSpriteBatch(), this, apple);
				break;

            default:
                throw new Exception("Unknown screen enum");
        }

        changeScreen(newScreen);

    }

    private void changeScreen(AbstractScreen newScreen) {

        Screen oldScreen = getScreen();

        // Set the new screen
        newScreen.buildStage();
        setScreen(newScreen);

        // Dispose the old one
        oldScreen.dispose();
    }

    public enum ScreenID {
        MAIN_MENU, GAME, LEVELS_MENU, DICTIONARY, WORD
    }
}
