package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.sound.FxSound;
import com.teamturtle.infinityrun.stages.IQuizStageListener;
import com.teamturtle.infinityrun.stages.QuizStage;

import java.util.ArrayList;
import java.util.List;

/**
 * Text om klassen
 *
 * @author David Söderberg
 * @since 2016-10-02
 */

public class QuizScreen extends AbstractScreen implements IQuizStageListener {

    private final Texture bg;
    private final QuizStage stage;
    private final IScreenObserver observer;
    private final Level level;
    private int score;
    private final List<Word> oldWords;
    private final List<Word> discoveredWords;

    public QuizScreen(SpriteBatch spriteBatch, IScreenObserver observer, Level level, List<Word> oldWords, List<Word> discoveredWords, int score) {
        super(spriteBatch);
        this.bg = new Texture(PathConstants.BACKGROUND_PATH);
        List<Word> collectedWords = new ArrayList<Word>();
        collectedWords.addAll(oldWords);
        collectedWords.addAll(discoveredWords);
        this.stage = new QuizStage(this, collectedWords, score);
        this.observer = observer;
        this.level = level;
        this.score = score;
        this.oldWords = oldWords;
        this.discoveredWords = discoveredWords;
    }

    @Override
    public void buildStage() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void onGuessClick(boolean isChoiceRight) {
        if (isChoiceRight) {
            FxSound.RATTGISSAT.play();
            observer.levelWon(level, oldWords, discoveredWords, ++score);
        } else {
            FxSound.FELGISSAT.play();
            observer.levelWon(level, oldWords, discoveredWords, score);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        bg.dispose();
        super.dispose();
    }
}
