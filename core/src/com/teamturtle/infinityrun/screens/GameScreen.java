package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sprites.Emoji;
import com.teamturtle.infinityrun.sprites.Player;

/**
 * Created by ericwenn on 9/20/16.
 */
public class GameScreen extends AbstractScreen {
    public static final int GAME_SPEED = 50;

    private OrthographicCamera cam;
    private SpriteBatch mSpriteBatch;
    private Texture bg;
    private int bg1, bg2;
    private FillViewport mFillViewport;
    private Player mPlayer;
    private Emoji emoji;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    public GameScreen( SpriteBatch mSpriteBatch ) {
        this.mSpriteBatch = mSpriteBatch;
        this.mFillViewport = new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT);
        this.cam = new OrthographicCamera( mFillViewport.getWorldWidth(), mFillViewport.getWorldHeight());
        this.cam.position.set(mFillViewport.getWorldWidth() / 2, mFillViewport.getWorldHeight() / 2, 0);

        this.bg = new Texture("bg.jpg");
        bg1 = 0;
        bg2 = InfinityRun.WIDTH;

        Texture dalaHorse = new Texture("dalahorse_32_flipped.png");
        this.mPlayer = new Player(dalaHorse);
        emoji = new Emoji("Äpple", "audio/apple.wav", new Texture("emoji/1f34e.png"),mSpriteBatch);
        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("tilemap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 2);
    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {

        mPlayer.update(delta);

        tiledMapRenderer.setView(cam);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mSpriteBatch.setProjectionMatrix(cam.combined);
        mSpriteBatch.begin();

        if(bg1 + InfinityRun.WIDTH < cam.position.x - cam.viewportWidth/2)
            bg1 += InfinityRun.WIDTH * 2;
        if(bg2 + InfinityRun.WIDTH< cam.position.x - cam.viewportWidth/2)
            bg2 += InfinityRun.WIDTH * 2;
        mSpriteBatch.draw(bg, bg1, 0, InfinityRun.WIDTH, InfinityRun.HEIGHT);
        mSpriteBatch.draw(bg, bg2, 0, InfinityRun.WIDTH, InfinityRun.HEIGHT);

        mSpriteBatch.draw( mPlayer, mPlayer.getX(), mPlayer.getY());
        mSpriteBatch.end();
        this.cam.position.set(mPlayer.getX() + InfinityRun.WIDTH / 3, mFillViewport.getWorldHeight() / 2, 0);
        cam.update();
        tiledMapRenderer.render();
        handleInput();
        emoji.render();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            emoji.show();
        }
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
