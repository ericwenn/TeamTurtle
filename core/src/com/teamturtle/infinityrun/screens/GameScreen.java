package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

/**
 * Created by ericwenn on 9/20/16.
 */
public class GameScreen implements Screen {
    public static final float GAME_SPEED = 0.1f, GRAVITY = -10;

    private OrthographicCamera cam;
    private SpriteBatch mSpriteBatch;
    private Texture bg;
    private float bg1, bg2;
    private FillViewport mFillViewport;
    private Player mPlayer;
    private Emoji emoji;

    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    public GameScreen( SpriteBatch mSpriteBatch ) {
        this.mSpriteBatch = mSpriteBatch;
        this.mFillViewport = new FillViewport(InfinityRun.WIDTH / InfinityRun.PPM,
                InfinityRun.HEIGHT / InfinityRun.PPM);
        this.cam = new OrthographicCamera( mFillViewport.getWorldWidth(), mFillViewport.getWorldHeight());
        this.cam.position.set(mFillViewport.getWorldWidth() / 2 , mFillViewport.getWorldHeight() / 2, 0);

        this.bg = new Texture("bg.jpg");
        bg1 = 0;
        bg2 = InfinityRun.WIDTH / InfinityRun.PPM;

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        Texture dalaHorse = new Texture("dalahorse_32_flipped.png");
        this.mPlayer = new Player(world, dalaHorse);
        emoji = new Emoji("Äpple", "audio/apple.wav", new Texture("emoji/1f34e.png"),mSpriteBatch);
        tmxMapLoader = new TmxMapLoader();
        tiledMap = tmxMapLoader.load("tilemap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / InfinityRun.PPM);

        //This chunk of code should be refactorized into some other class.
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        for(MapObject object : tiledMap.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect =((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(((rect.getX() + rect.getWidth() / 2) / InfinityRun.PPM),
                    ((rect.getY() + rect.getHeight() / 2)) / InfinityRun.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / InfinityRun.PPM,
                    rect.getHeight() / 2 / InfinityRun.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        world.step(1/60f, 6, 2);
        mPlayer.update(delta);
        handleInput();

        tiledMapRenderer.setView(cam);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mSpriteBatch.setProjectionMatrix(cam.combined);
        mSpriteBatch.begin();

        drawBackground();
        mSpriteBatch.draw( mPlayer, mPlayer.getX(), mPlayer.getY(), 32 / InfinityRun.PPM,
                32 / InfinityRun.PPM);

        mSpriteBatch.end();
        this.cam.position.set(mPlayer.getX(), mFillViewport.getWorldHeight() / 2, 0);
        cam.update();
        tiledMapRenderer.render();
        b2dr.render(world, cam.combined);
        emoji.render();
    }

    private void handleInput() {
        if((Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && mPlayer.getPlayerBody().getLinearVelocity().y == 0)
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

    }

    @Override
    public void dispose() {
        mSpriteBatch.dispose();
        bg.dispose();

    }

    public void drawBackground(){
        if(bg1 + InfinityRun.WIDTH / InfinityRun.PPM< cam.position.x - cam.viewportWidth/2)
            bg1 += (InfinityRun.WIDTH * 2) / InfinityRun.PPM;
        if(bg2 + InfinityRun.WIDTH / InfinityRun.PPM < cam.position.x - cam.viewportWidth/2)
            bg2 += (InfinityRun.WIDTH * 2) / InfinityRun.PPM;

        mSpriteBatch.draw(bg, bg1, 0, InfinityRun.WIDTH / InfinityRun.PPM,
                InfinityRun.HEIGHT / InfinityRun.PPM);
        mSpriteBatch.draw(bg, bg2, 0, InfinityRun.WIDTH / InfinityRun.PPM,
                InfinityRun.HEIGHT / InfinityRun.PPM);
    }
}
