package com.teamturtle.infinityrun.loading;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.screens.AbstractScreen;

/**
 * Created by ericwenn on 10/18/16.
 */
public class LoadingScreen extends AbstractScreen {


    private final AssetManager assetManager;
    private LoadingStage loadingStage;

    public LoadingScreen(SpriteBatch spriteBatch, AssetManager assetManager) {
        super(spriteBatch);
        this.assetManager = assetManager;
        loadingStage = new LoadingStage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT), spriteBatch, assetManager);
    }

    @Override
    public void buildStage() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        loadingStage.draw();

    }
}
