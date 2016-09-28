package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ericwenn on 9/20/16.
 */
public class Player extends AbstractEntity {

    private World world;
    private Body b2body;
    private TextureRegion playerStand;
    private static final int PLAYER_WIDTH = 32, PLAYER_HEIGHT = 32,
            COLLISION_RADIUS = PLAYER_WIDTH / 2, START_X = 100, START_Y = 300;
    private static final float JUMP_IMPULSE = 4f;
    private static final float IMPULSE_X = 0.1f;
    private static final float SPEED_X = 1.5f;
    private static final String TEXTURE_URL = "dalahorse_32_flipped.png";

    public Player(World world) {
        this.world = world;

        setPosition(0, InfinityRun.HEIGHT / 2);
        definePlayer();

        playerStand = new TextureRegion(new Texture(TEXTURE_URL), 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void update(float dt) {
        if (b2body.getLinearVelocity().x <= SPEED_X) {
            b2body.applyLinearImpulse(new Vector2(IMPULSE_X, 0), b2body.getWorldCenter(), true);
        }

        setPosition(b2body.getPosition().x - PLAYER_WIDTH / 2 / InfinityRun.PPM
                , b2body.getPosition().y - PLAYER_HEIGHT / 2 / InfinityRun.PPM);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(playerStand, getX(), getY(), 32 / InfinityRun.PPM, 32 / InfinityRun.PPM);
    }

    @Override
    public void dispose() {
        playerStand = null;
        world.destroyBody(b2body);
    }

    private void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(START_X / InfinityRun.PPM, START_Y / InfinityRun.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.friction = 0;
        CircleShape shape = new CircleShape();
        shape.setRadius(COLLISION_RADIUS / InfinityRun.PPM);
        fdef.shape = shape;

        Fixture fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }

    public void jump(){
        if (b2body.getLinearVelocity().y == 0) {
            b2body.applyLinearImpulse(new Vector2(0, JUMP_IMPULSE), b2body.getWorldCenter(), true);
        }
    }
}
