package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.stages.IQuizStageListener;
import com.teamturtle.infinityrun.stages.QuizStage;

import java.util.List;

/**
 * Text om klassen
 *
 * @author David Söderberg
 * @since 2016-10-02
 */

public class QuizScreen extends AbstractScreen implements IQuizStageListener {

    private Texture bg;
    private QuizStage stage;
    private IScreenObserver observer;
    private Level level;
    private int score;

    public QuizScreen(SpriteBatch spriteBatch, IScreenObserver observer, Level level
            , List<Word> collectedWords, int score) {
        super(spriteBatch);
        this.bg = new Texture(PathConstants.BACKGROUND_PATH);
        this.stage = new QuizStage(this, collectedWords);
        this.observer = observer;
        this.level = level;
        this.score = score;
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
        stage.draw();
    }

    @Override
    public void onGuessClick(boolean isChoiceRight) {
        if (isChoiceRight) {
            observer.levelWon(level, ++score);
        } else {
            observer.levelWon(level, score);
        }
    }
}
