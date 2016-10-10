package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.sound.SoundPlayer;
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
    private Sound rightAnswerSound;

    public QuizScreen(SpriteBatch spriteBatch, IScreenObserver observer, Level level
            , List<Word> collectedWords, int score) {
        super(spriteBatch);
        this.bg = new Texture(PathConstants.BACKGROUND_PATH);
        this.stage = new QuizStage(this, collectedWords, score);
        this.observer = observer;
        this.level = level;
        this.score = score;
        rightAnswerSound = Gdx.audio.newSound(Gdx.files.internal("audio/right_answer.wav"));
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
            rightAnswerSound.play();
            SoundPlayer.playSound("rattgissat", "feedback");
            observer.levelWon(level, ++score);
        } else {
            SoundPlayer.playSound("felgissat", "feedback");
            observer.levelWon(level, score);
        }
    }
}
