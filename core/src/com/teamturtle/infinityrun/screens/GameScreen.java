package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.collisions.CollisionHandler;
import com.teamturtle.infinityrun.collisions.ICollisionHandler;
import com.teamturtle.infinityrun.map_parsing.EmojiParser;
import com.teamturtle.infinityrun.map_parsing.GroundParser;
import com.teamturtle.infinityrun.map_parsing.MapParser;
import com.teamturtle.infinityrun.map_parsing.ObstacleParser;
import com.teamturtle.infinityrun.sprites.Entity;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.util.List;

/**
 * Created by ericwenn on 9/20/16.
 */
public class GameScreen implements Screen {

    public enum Level {
        LEVEL_1("level1.tmx"), LEVEL_2("level2.tmx"), LEVEL_3("level3.tmx");

        private final String tmx;

        Level(String tmx) {
            this.tmx = tmx;
        }
    }

    public static final float GRAVITY = -10;

    private OrthographicCamera cam;
    private SpriteBatch mSpriteBatch;
    private Texture bg;
    private float bgPosition1, bgPosition2;
    private FillViewport mFillViewport;
    private Player mPlayer;

    private CollisionHandler mCollisionHandler;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private List<? extends Entity> emojiSprites;

    public GameScreen(SpriteBatch mSpriteBatch, Level level) {
        this.mSpriteBatch = mSpriteBatch;

        //Load tilemap
        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load(level.tmx);
    }

    @Override
    public void show() {

        // FillViewport "letterboxing"
        this.mFillViewport = new FillViewport(InfinityRun.WIDTH / InfinityRun.PPM
                , InfinityRun.HEIGHT / InfinityRun.PPM);

        // Setup camera and set it to center of the world
        this.cam = new OrthographicCamera(mFillViewport.getWorldWidth(), mFillViewport.getWorldHeight());
        this.cam.position.set(mFillViewport.getWorldWidth() / 2, mFillViewport.getWorldHeight() / 2, 0);

        // Init background from file and setup starting positions to have continous background.
        this.bg = new Texture("bg.jpg");

        bgPosition1 = 0;
        bgPosition2 = InfinityRun.WIDTH / InfinityRun.PPM;

        // TODO Remove this before production
        b2dr = new Box2DDebugRenderer();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / InfinityRun.PPM);

        //Creates box2d world and enteties
        setUpWorld();

        // Create CollisionHandler with actions. Still not connected to the world.
        setupContactHandler();

        world.setContactListener(mCollisionHandler);
    }

    private void gameUpdate(float delta) {
        handleInput();
        world.step(1 / 60f, 6, 2);
        mPlayer.update(delta);
    }


    @Override
    public void render(float delta) {
        gameUpdate(delta);

        tiledMapRenderer.setView(cam);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mSpriteBatch.setProjectionMatrix(cam.combined);
        mSpriteBatch.begin();

        renderBg();

        mPlayer.render(mSpriteBatch);
        for (Entity emoji: emojiSprites) {
            emoji.update(delta);
            emoji.render(mSpriteBatch);
        }

        mSpriteBatch.end();
        this.cam.position.set(mPlayer.getX() + (mFillViewport.getWorldWidth() / 3)
                , mFillViewport.getWorldHeight() / 2, 0);
        cam.update();
        tiledMapRenderer.render();
        b2dr.render(world, cam.combined);
    }

    private void handleInput() {
        if((Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)))
            mPlayer.jump();
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
        world.setContactListener(null);
    }

    @Override
    public void dispose() {
        Gdx.app.log("Dispose", "disposing");
        for( Entity ent : emojiSprites) {
            ent.dispose();
        }
        mPlayer.dispose();
        mSpriteBatch.dispose();
        bg.dispose();

    }

    private void renderBg() {
        if(bgPosition1 + InfinityRun.WIDTH / InfinityRun.PPM< cam.position.x - cam.viewportWidth/2)
            bgPosition1 += (InfinityRun.WIDTH * 2) / InfinityRun.PPM;
        if(bgPosition2 + InfinityRun.WIDTH / InfinityRun.PPM < cam.position.x - cam.viewportWidth/2)
            bgPosition2 += (InfinityRun.WIDTH * 2) / InfinityRun.PPM;
        mSpriteBatch.draw(bg, bgPosition1, 0, InfinityRun.WIDTH / InfinityRun.PPM,
                InfinityRun.HEIGHT / InfinityRun.PPM);
        mSpriteBatch.draw(bg, bgPosition2, 0, InfinityRun.WIDTH / InfinityRun.PPM,
                InfinityRun.HEIGHT / InfinityRun.PPM);
    }

    private void setUpWorld() {
        // Setup world with regular gravity, and sleeping bodies
        world = new World(new Vector2(0, GRAVITY), true);

        mPlayer = new Player(world);

        MapParser groundParser = new GroundParser(world, tiledMap, "ground");
        groundParser.parse();

        MapParser emojiParser = new EmojiParser(world, tiledMap,  "emoji_placeholders");
        emojiParser.parse();
        emojiSprites = emojiParser.getEntities();

        MapParser obstacleParser = new ObstacleParser(world, tiledMap, "obstacles");
        obstacleParser.parse();
    }

    private void setupContactHandler() {

        CollisionHandler collisionHandler = new CollisionHandler();
        collisionHandler.onCollisionWithObstable(new ICollisionHandler.ObstacleCollisionListener() {
            @Override
            public void onCollision(Player p) {
                Gdx.app.log("Collision", "Game over");
            }
        });

        collisionHandler.onCollisionWithEmoji(new ICollisionHandler.EmojiCollisionListener() {
            @Override
            public void onCollision(Player p, Emoji e) {
                Gdx.app.log("Collision", "Emoji collision");
                e.triggerExplode();
            }

        });

        mCollisionHandler = collisionHandler;
    }

}
