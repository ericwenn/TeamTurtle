package com.teamturtle.infinityrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
<<<<<<< HEAD
import com.teamturtle.infinityrun.models.Word;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.level.LevelDataHandler;
=======
import com.teamturtle.infinityrun.map_parsing.EmojiParser;
>>>>>>> 4931ce6f42fb28a5dfae265f2732dfa1535a8278
import com.teamturtle.infinityrun.screens.AbstractScreen;
import com.teamturtle.infinityrun.screens.DictionaryScreen;
import com.teamturtle.infinityrun.screens.GameScreen;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.screens.LevelSelectScreen;
import com.teamturtle.infinityrun.screens.QuizScreen;
import com.teamturtle.infinityrun.screens.StartScreen;
import com.teamturtle.infinityrun.screens.WordScreen;
import com.teamturtle.infinityrun.screens.level_end_screens.EndLevelScreen;
import com.teamturtle.infinityrun.screens.level_end_screens.LostLevelScreen;
import com.teamturtle.infinityrun.screens.level_end_screens.WonLevelScreen;
<<<<<<< HEAD
import java.util.List;

=======
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
>>>>>>> 4931ce6f42fb28a5dfae265f2732dfa1535a8278

public class InfinityRun extends Game implements IScreenObserver {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float PPM = 75;

    private SpriteBatch mSpriteBatch;

    @Override
    public void create() {

        setSpriteBatch(new SpriteBatch());

        try {
<<<<<<< HEAD
            LevelDataHandler handler = new LevelDataHandler();
            handler.getLevels();
            changeScreen(ScreenID.MAIN_MENU);
=======
            changeScreen(ScreenID.WORD);
>>>>>>> 4931ce6f42fb28a5dfae265f2732dfa1535a8278
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

    @Override
    public void playLevel(Level level) {
        changeScreen(new GameScreen(getSpriteBatch(), this, level));
    }

    @Override
    public void levelCompleted(Level level, List<Word> missionWords, int score) {
        if (score > 0) {
            changeScreen(new QuizScreen(getSpriteBatch(), this, missionWords, score));
        }else{
            levelFailed();
        }
    }

    @Override
    public void levelWon(int score) {
        changeScreen(new WonLevelScreen(getSpriteBatch(), this, score));
    }

    @Override
    public void levelFailed() {
        changeScreen(new LostLevelScreen(getSpriteBatch(), this));
    }

    @Override
    public void changeScreen(ScreenID screen) throws Exception {
        AbstractScreen newScreen;

        switch (screen) {
            case MAIN_MENU:
                newScreen = new StartScreen(getSpriteBatch(), this);
                break;

            case LOST_GAME:
                newScreen = new LostLevelScreen(getSpriteBatch(), this);
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
        MAIN_MENU, GAME, WON_GAME, LOST_GAME, LEVELS_MENU, QUIZ, DICTIONARY, WORD
    }
}
