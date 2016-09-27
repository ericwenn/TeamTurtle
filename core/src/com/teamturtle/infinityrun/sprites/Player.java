package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
public class Player extends Sprite {

    private World world;
    private Body b2body;
    private TextureRegion playerStand;
    private static final int PLAYER_WIDTH = 32, PLAYER_HEIGHT = 32,
            COLLISION_RADIUS = PLAYER_WIDTH / 2, START_X = 100, START_Y = 300;
    private static final float JUMP_IMPULSE = 5f;

    public Player(World world, Texture t) {
        super( t );
        this.world = world;
        definePlayer();
        b2body.setLinearVelocity(GameScreen.GAME_SPEED * 5, 0);

        playerStand = new TextureRegion(getTexture(), 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT);
        setBounds(0, 0, PLAYER_WIDTH / InfinityRun.PPM, PLAYER_HEIGHT / InfinityRun.PPM);
        setRegion(playerStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - PLAYER_WIDTH / 2 / InfinityRun.PPM,
                b2body.getPosition().y - PLAYER_HEIGHT / 2 / InfinityRun.PPM);
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
        b2body.applyLinearImpulse(new Vector2(0, JUMP_IMPULSE), b2body.getWorldCenter(), true);
    }

    public Body getPlayerBody(){
        return b2body;
    }
}
