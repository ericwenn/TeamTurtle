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
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.screens.IScreenObserver;

/**
 * Created by ostmos on 2016-10-08.
 */
public class PauseStage extends Stage{

    private static final int ROW_PAD = 10;

    private Skin skin;
    private ImageButton continueBtn;
    private ImageButton retryBtn;
    private ImageButton levelsBtn;

    private IPauseStageHandler handler;
    private IScreenObserver observer;
    private Level level;

    public PauseStage(IPauseStageHandler handler, IScreenObserver observer, Level level) {
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));

        this.handler = handler;
        this.observer = observer;
        this.level = level;

        setUpStage();
    }

    private void setUpStage() {
        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));
        setUpButtons();
        Table table = new Table();
        table.setFillParent(true);
        table.add(continueBtn).padBottom(ROW_PAD);
        table.row();
        table.add(retryBtn).padBottom(ROW_PAD);
        table.row();
        table.add(levelsBtn);
        this.addActor(table);
        Gdx.input.setInputProcessor(this);
    }

    private void setUpButtons() {
        continueBtn = new ImageButton(skin, "continue_button");
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
                observer.playLevel(level);
            }
        });
        levelsBtn = new ImageButton(skin, "levels_button");
        levelsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    observer.changeScreen(InfinityRun.ScreenID.LEVELS_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
