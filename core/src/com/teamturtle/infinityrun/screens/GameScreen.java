package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
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
import com.teamturtle.infinityrun.collisions.EventHandler;
import com.teamturtle.infinityrun.collisions.IEventHandler;
import com.teamturtle.infinityrun.map_parsing.EmojiParser;
import com.teamturtle.infinityrun.map_parsing.GroundParser;
import com.teamturtle.infinityrun.map_parsing.MapParser;
import com.teamturtle.infinityrun.map_parsing.SensorParser;
import com.teamturtle.infinityrun.sprites.Entity;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.util.List;

/**
 * Created by ericwenn on 9/20/16.
 */
public class GameScreen extends AbstractScreen {

    public enum Level {
        LEVEL_1("level1.tmx"), LEVEL_2("level2.tmx"), LEVEL_3("level3.tmx");

        private final String tmx;

        Level(String tmx) {
            this.tmx = tmx;
        }
    }

    private enum State {
        PLAY, PAUSE, LOST_GAME, WON_GAME
    }

    public static final float GRAVITY = -10;

    private Texture bg;

    private float bg1, bg2;
    private FillViewport mFillViewport;

    private Player mPlayer;

    private EventHandler mEventHandler;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private List<? extends Entity> emojiSprites;
    private IScreenObserver screenObserver;

    private State state;

    public GameScreen( SpriteBatch mSpriteBatch, Level level, IScreenObserver screenObserver) {
        super(mSpriteBatch);
        this.screenObserver = screenObserver;

        //Set state
        state = State.PLAY;

        //Load tilemap
        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load(level.tmx);
    }

    @Override
    public void show() {
        //Change input focus to this stage
        Gdx.input.setInputProcessor(this);

        // FillViewport "letterboxing"
        this.mFillViewport = new FillViewport(InfinityRun.WIDTH / InfinityRun.PPM
                , InfinityRun.HEIGHT / InfinityRun.PPM);

        // Init background from file and setup starting positions to have continous background.
        this.bg = new Texture("bg2.png");

        bg1 = 0;
        bg2 = InfinityRun.WIDTH / InfinityRun.PPM;

        // TODO Remove this before production
        b2dr = new Box2DDebugRenderer();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / InfinityRun.PPM);

        //Creates box2d world and enteties
        setUpWorld();

        // Create EventHandler with actions. Still not connected to the world.
        setupEventHandler();

        world.setContactListener(mEventHandler);
    }

    private void gameUpdate(float delta) {
        handleInput();
        world.step(1 / 60f, 6, 2);
        mPlayer.update(delta);
    }


    @Override
    public void render(float delta) {
        switch (state) {
            case PLAY:
                renderWorld(delta);
                break;
            case LOST_GAME:
                try {
                    screenObserver.changeScreen(InfinityRun.ScreenID.LOST_GAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case WON_GAME:
                try {
                    screenObserver.changeScreen(InfinityRun.ScreenID.WON_GAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case PAUSE:
                break;
        }
    }

    private void renderWorld(float delta) {
        gameUpdate(delta);

        tiledMapRenderer.setView(getOrthoCam());
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getSpriteBatch().setProjectionMatrix(getCamera().combined);

        getSpriteBatch().begin();
        drawBackground();

        mPlayer.render(getSpriteBatch());
        for (Entity entity : emojiSprites) {
            entity.update(delta);
            entity.render(getSpriteBatch());
        }
        getSpriteBatch().end();

        getCamera().position.set(mPlayer.getX() + (mFillViewport.getWorldWidth() / 3)
                , mFillViewport.getWorldHeight() / 2, 0);
        getCamera().update();
        tiledMapRenderer.render();
        //Use to show collision rectangles
        //b2dr.render(world, getOrthoCam().combined);
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
    public void buildStage() {

    }

    @Override
    public void dispose() {
        for( Entity ent : emojiSprites) {
            ent.dispose();
        }
        mPlayer.dispose();
        bg.dispose();
    }

    public void drawBackground(){
        if(bg1 + InfinityRun.WIDTH / InfinityRun.PPM< getOrthoCam().position.x - getOrthoCam().viewportWidth/2)
            bg1 += (InfinityRun.WIDTH * 2) / InfinityRun.PPM;
        if(bg2 + InfinityRun.WIDTH / InfinityRun.PPM < getOrthoCam().position.x - getOrthoCam().viewportWidth/2)
            bg2 += (InfinityRun.WIDTH * 2) / InfinityRun.PPM;

        getSpriteBatch().draw(bg, bg1, 0, InfinityRun.WIDTH / InfinityRun.PPM,
                InfinityRun.HEIGHT / InfinityRun.PPM);
        getSpriteBatch().draw(bg, bg2, 0, InfinityRun.WIDTH / InfinityRun.PPM,
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

        MapParser obstacleParser = new SensorParser(world, tiledMap, SensorParser.Type.OBSTACLE);
        obstacleParser.parse();

        MapParser goalParser = new SensorParser(world, tiledMap, SensorParser.Type.GOAL);
        goalParser.parse();

        MapParser questParser= new SensorParser(world, tiledMap, SensorParser.Type.QUEST);
        questParser.parse();
    }

    private void setupEventHandler() {

        EventHandler eventHandler = new EventHandler();

        eventHandler.onCollisionWithEmoji(new IEventHandler.EmojiCollisionListener() {
            @Override
            public void onCollision(Player p, Emoji e) {
                Gdx.app.log("Collision", "Emoji collision");
                e.triggerExplode();
            }

        });


        eventHandler.onCollisionWithObstacle(new IEventHandler.ObstacleCollisionListener() {
            @Override
            public void onCollision(Player p) {
                Gdx.app.log("Collision", "Obstacle collision");
                state = State.LOST_GAME;
            }
        });

        eventHandler.onLevelFinished(new IEventHandler.LevelFinishedListener() {
            @Override
            public void onLevelFinished() {
                state = State.WON_GAME;
            }
        });

        // TODO Implement quest listener


        this.mEventHandler = eventHandler;
    }
}
