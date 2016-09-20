package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ericwenn on 9/20/16.
 */
public class GameScreen implements Screen {
    private OrthographicCamera cam;
    private SpriteBatch mSpriteBatch;
    private Texture bg;

    public GameScreen( SpriteBatch mSpriteBatch ) {

        this.mSpriteBatch = mSpriteBatch;
        this.cam = new OrthographicCamera();
        this.bg = new Texture("bg.jpg");
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

    }
}
