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
    private boolean canDoubleJump;
    private TextureRegion playerStand;
    public static final int PLAYER_WIDTH = 32, PLAYER_HEIGHT = 32;
    private static final int COLLISION_RADIUS = PLAYER_WIDTH / 2, START_X = 150, START_Y = 300;
    private static final float JUMP_IMPULSE = 4.5f;
    private static final float SPEED_X = 2.5f;
    private static final String TEXTURE_URL = "dalahorse_32_flipped.png";

    public Player(World world) {
        this.world = world;
        canDoubleJump = true;

        setPosition(0, InfinityRun.HEIGHT / 2);
        definePlayer();

        playerStand = new TextureRegion(new Texture(TEXTURE_URL), 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void update(float dt) {
        if (b2body.getLinearVelocity().x != SPEED_X) {
            b2body.setLinearVelocity(SPEED_X, b2body.getLinearVelocity().y);
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

    public boolean tryToJump(){
        float jumpStrength = 0;
        if (b2body.getLinearVelocity().y == 0) {
            jumpStrength = JUMP_IMPULSE;
            canDoubleJump = true;
        }
        else if(canDoubleJump){
            if(b2body.getLinearVelocity().y <= 0) {
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0);
            }
            else {
                b2body.setLinearVelocity(b2body.getLinearVelocity().x,
                        b2body.getLinearVelocity().y / 2);
            }
            jumpStrength = JUMP_IMPULSE;
            canDoubleJump = false;
        }
        b2body.applyLinearImpulse(new Vector2(0, jumpStrength), b2body.getWorldCenter(), true);
        return jumpStrength == JUMP_IMPULSE;
    }
}
