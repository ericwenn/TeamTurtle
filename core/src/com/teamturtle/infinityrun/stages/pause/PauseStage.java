package com.teamturtle.infinityrun.stages.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ostmos on 2016-10-08.
 */
public class PauseStage extends Stage{

    private Skin skin;
    private TextButton continueBtn;
    private ImageButton retryBtn;
    private ImageButton levelsBtn;

    private IPauseStageHandler handler;

    public PauseStage(IPauseStageHandler handler) {
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));

        this.handler = handler;

        setUpStage();
    }

    private void setUpStage() {
        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));
        setUpButtons();
        Table table = new Table();
        table.setFillParent(true);
        table.add(continueBtn);
        table.row();
        table.add(retryBtn);
        table.row();
        table.add(levelsBtn);
        this.addActor(table);
    }

    private void setUpButtons() {
        continueBtn = new TextButton("Fortsätt", skin, "text_button");
        continueBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handler.continueButtonClick();
            }
        });
        retryBtn = new ImageButton(skin, "retry_button");
        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        levelsBtn = new ImageButton(skin, "levels_button");
        levelsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
    }


}
