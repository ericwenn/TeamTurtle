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

    private Texture bg;
    private int bg1, bg2;
    private Player mPlayer;
    private Emoji emoji;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    public GameScreen( SpriteBatch mSpriteBatch ) {
        super(mSpriteBatch);

        this.bg = new Texture("bg.jpg");
        bg1 = 0;
        bg2 = InfinityRun.WIDTH;

        Texture dalaHorse = new Texture("dalahorse_32_flipped.png");
        this.mPlayer = new Player(dalaHorse);
        emoji = new Emoji("Äpple", "audio/apple.wav", new Texture("emoji/1f34e.png"), getSpriteBatch());
        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("tilemap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 2);
    }


    @Override
    public void show() {

    }


    @Override
    public void buildStage() {

    }

    @Override
    public void render(float delta) {

        super.render(delta);

        OrthographicCamera cam = (OrthographicCamera) getCamera();
        SpriteBatch mSpriteBatch = getSpriteBatch();

        mPlayer.update(delta);

        tiledMapRenderer.setView(cam);
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
        cam.position.set(mPlayer.getX() + InfinityRun.WIDTH / 3, getViewport().getWorldHeight() / 2, 0);
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
        super.dispose();
        bg.dispose();
    }
}
