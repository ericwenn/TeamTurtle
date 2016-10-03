package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.stages.IQuizStageListener;
import com.teamturtle.infinityrun.stages.QuizStage;

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

    public QuizScreen(SpriteBatch spriteBatch, IScreenObserver observer) {
        super(spriteBatch);
        this.bg = new Texture(PathConstants.BACKGROUND_PATH);
        this.stage = new QuizStage(this);
        this.observer = observer;
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
            try {
                observer.changeScreen(InfinityRun.ScreenID.GAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
