package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ericwenn on 9/20/16.
 */
public class GameScreen implements Screen {
    private OrthographicCamera cam;
    private SpriteBatch mSpriteBatch;
    private Texture bg;
    private FillViewport mFillViewport;

    public GameScreen( SpriteBatch mSpriteBatch ) {
        this.mSpriteBatch = mSpriteBatch;
        this.mFillViewport = new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT);
        this.cam = new OrthographicCamera( mFillViewport.getWorldWidth(), mFillViewport.getWorldHeight());

        this.cam.position.set(mFillViewport.getWorldWidth() / 2, mFillViewport.getWorldHeight() / 2, 0);
        this.bg = new Texture("bg.jpg");
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.cam.position.add(1, 0, 0);
        cam.update();
        mSpriteBatch.setProjectionMatrix(cam.combined);
        mSpriteBatch.begin();

        mSpriteBatch.draw(bg, 0, 0, 800, 480);
        mSpriteBatch.end();
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
        mSpriteBatch.dispose();
        bg.dispose();

    }
}
