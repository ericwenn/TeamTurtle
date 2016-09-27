package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.collisions.CollisionHandler;
import com.teamturtle.infinityrun.collisions.ICollisionHandler;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.EmojiFactory;

/**
 * Created by ericwenn on 9/20/16.
 */
public class GameScreen implements Screen {
    public static final int GAME_SPEED = 100, GRAVITY = -100;

    private OrthographicCamera cam;
    private SpriteBatch mSpriteBatch;
    private Texture bg;
    private int bgPosition1, bgPosition2;
    private FillViewport mFillViewport;
    private Player mPlayer;

    private ICollisionHandler mCollisionHandler;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Array<Body> emojiBodies;

    public GameScreen(SpriteBatch mSpriteBatch) {
        this.mSpriteBatch = mSpriteBatch;
        this.mFillViewport = new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT);
        this.cam = new OrthographicCamera(mFillViewport.getWorldWidth(), mFillViewport.getWorldHeight());
        this.cam.position.set(mFillViewport.getWorldWidth() / 2, mFillViewport.getWorldHeight() / 2, 0);

        this.bg = new Texture("bg.jpg");
        bgPosition1 = 0;
        bgPosition2 = InfinityRun.WIDTH;

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        setupContactHandler();


        Texture dalaHorse = new Texture("dalahorse_32_flipped.png");
        this.mPlayer = new Player(world, dalaHorse);
        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("tilemap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);


        //This chunk of code should be refactorized into some other class.
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
//        Creating ground objects
        for (MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


        EmojiFactory emojiFactory = new EmojiFactory(world, tiledMap, mSpriteBatch, 3);
        emojiFactory.create();

        emojiBodies = emojiFactory.getBodies();

//        Creating obstacles
        for (MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            fdef.isSensor = true;
            body.createFixture(fdef).setUserData("obstacle");
        }

    }

    @Override
    public void show() {
        world.setContactListener(mCollisionHandler);
    }


    @Override
    public void render(float delta) {
        world.step(1 / 60f, 6, 2);
        mPlayer.update(delta);


        tiledMapRenderer.setView(cam);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mSpriteBatch.setProjectionMatrix(cam.combined);
        mSpriteBatch.begin();

        if (bgPosition1 + InfinityRun.WIDTH < cam.position.x - cam.viewportWidth / 2)
            bgPosition1 += InfinityRun.WIDTH * 2;
        if (bgPosition2 + InfinityRun.WIDTH < cam.position.x - cam.viewportWidth / 2)
            bgPosition2 += InfinityRun.WIDTH * 2;
        mSpriteBatch.draw(bg, bgPosition1, 0, InfinityRun.WIDTH, InfinityRun.HEIGHT);
        mSpriteBatch.draw(bg, bgPosition2, 0, InfinityRun.WIDTH, InfinityRun.HEIGHT);


        mSpriteBatch.draw(mPlayer, mPlayer.getX(), mPlayer.getY());
        mSpriteBatch.end();
        this.cam.position.set(mPlayer.getX() + InfinityRun.WIDTH / 3, mFillViewport.getWorldHeight() / 2, 0);
        cam.update();
        tiledMapRenderer.render();
        handleInput();
        b2dr.render(world, cam.combined);

        for (Body body : emojiBodies) {
            Emoji emoji = (Emoji) body.getUserData();
            emoji.setPosition(body.getPosition().x - Emoji.EMOJI_SIZE / 2, body.getPosition().y - Emoji.EMOJI_SIZE / 2);
            emoji.render();
        }
    }

    private void handleInput() {

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
                System.out.println("Emoji collision");
                e.triggerExplode();
            }

        });

        mCollisionHandler = collisionHandler;
    }

}
