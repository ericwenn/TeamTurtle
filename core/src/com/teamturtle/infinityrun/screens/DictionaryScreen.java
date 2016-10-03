package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by Henrik on 2016-10-03.
 */
public class DictionaryScreen extends AbstractScreen {

    private Stage stage;

    public DictionaryScreen(SpriteBatch spriteBatch) {
        super(spriteBatch);
    }

    @Override
    public void buildStage() {
        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
    }
}
