package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.screens.GameScreen;

/**
 * Created by ericwenn on 9/20/16.
 */
public class Player extends AbstractEntity {

    private World world;
    private Texture mTexture;
    private Body b2body;
    private TextureRegion playerStand;
    private static final int PLAYER_WIDTH = 32, PLAYER_HEIGHT = 32,
            COLLISION_RADIUS = PLAYER_WIDTH / 2;

    public Player(World world, Texture t) {
        this.world = world;
        this.mTexture = t;

        setPosition(100, InfinityRun.HEIGHT / 2);
        definePlayer();

        //Temporary velocity source, should be changed because the velocity decreases due to gravity
        b2body.setLinearVelocity(GameScreen.GAME_SPEED, 0);

        playerStand = new TextureRegion(mTexture, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - PLAYER_WIDTH / 2, b2body.getPosition().y - PLAYER_HEIGHT / 2);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(playerStand, getX(), getY());
    }

    @Override
    public void dispose() {
        mTexture.dispose();
        playerStand = null;

        world.destroyBody(b2body);
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(100, 300);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.friction = 0;
        CircleShape shape = new CircleShape();
        shape.setRadius(COLLISION_RADIUS);
        fdef.shape = shape;

        Fixture fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }
}
