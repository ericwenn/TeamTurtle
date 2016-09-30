package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by Alfred on 2016-09-22.
 */

public abstract class AbstractScreen extends Stage implements Screen {

    protected AbstractScreen(SpriteBatch spriteBatch) {

        /*Viewport mFillViewport = new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT);
        this.cam = new OrthographicCamera( mFillViewport.getWorldWidth(), mFillViewport.getWorldHeight());
        this.cam.position.set(mFillViewport.getWorldWidth() / 2, mFillViewport.getWorldHeight() / 2, 0);*/

        super( new FillViewport(InfinityRun.WIDTH / InfinityRun.PPM, InfinityRun.HEIGHT / InfinityRun.PPM,
                new OrthographicCamera(InfinityRun.WIDTH, InfinityRun.HEIGHT)
                ),
                spriteBatch );

        this.getCamera().position.set(
                getViewport().getWorldWidth() / 2,
                getViewport().getWorldHeight() / 2,
                0);
    }

    public SpriteBatch getSpriteBatch(){
        return (SpriteBatch) getBatch();
    }

    public OrthographicCamera getOrthoCam(){
        return (OrthographicCamera) getCamera();
    }

    // Subclasses must load actors in this method
    public abstract void buildStage();

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Calling to Stage methods
        super.act(delta);
        super.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {
        super.dispose();
    }
}
