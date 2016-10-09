package com.teamturtle.infinityrun.stages.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by Henrik on 2016-10-09.
 */
public class PauseButtonStage extends Stage {

    private Skin skin;
    private ImageButton pauseButton;

    public PauseButtonStage() {
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        setUpButton();
    }

    private void setUpButton() {
        pauseButton = new ImageButton(skin, "pause_button");
        Table table = new Table();
        table.setSize((InfinityRun.WIDTH * 2) - pauseButton.getWidth()
                , (InfinityRun.HEIGHT * 2) - pauseButton.getHeight());
        table.add(pauseButton);
        pauseButton.setChecked(true);
        addActor(table);
    }

}
