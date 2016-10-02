package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Text om klassen
 *
 * @author David Söderberg
 * @since 2016-10-02
 */

public class QuizStage extends Stage {
    private Skin skin;
    private IQuizStageListener handler;
    private Label quizLabel;

    public QuizStage(IQuizStageListener handler) {
        super(new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.handler = handler;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        quizLabel = new Label("Quiz-dags!", skin);

        Gdx.input.setInputProcessor(this);
    }
}
