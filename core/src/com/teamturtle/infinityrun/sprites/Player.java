package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.Color;
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
import com.teamturtle.infinityrun.screens.GameScreen;

/**
 * Created by ericwenn on 9/20/16.
 */
public class Player extends AbstractEntity {

    private final World world;
    private Body b2body;
    private float scale = 1;
    public static final int PLAYER_WIDTH = 16, PLAYER_HEIGHT = 16;
    private static final int COLLISION_RADIUS = PLAYER_WIDTH / 2, START_X = 150, START_Y = 300;
    private static final float JUMP_IMPULSE = 4.5f;
    private static final float SPEED_X_MIN = 2.5f;
    private static final float SPEED_X_MAX = 2.58f;
    private static final float LINEAR_SPEED_X = 2.5799992f;
    private static final float IMPULSE_X = 0.1f;

    private final TextureRegion neutralTexture;
    private final TextureRegion failTexture;
    private final TextureRegion successTexture;
    private Color fillColor;


    private int jumpsLeft = 2;

    public Player(World world) {
        this.world = world;

        setPosition(0, InfinityRun.HEIGHT / 2);
        definePlayer();

        this.neutralTexture = new TextureRegion(new Texture("player_sprite_neutral.png"), 0, 0, 32, 32);
        this.failTexture = new TextureRegion(new Texture("player_sprite_fail.png"), 0, 0, 32, 32);
        this.successTexture = new TextureRegion(new Texture("player_sprite_success.png"), 0, 0, 32, 32);
        this.setColor(GameScreen.NEUTRAL_PLAYER_COLOR);
    }

    public void update(float dt) {
        if (b2body.getLinearVelocity().x < SPEED_X_MIN) {
            b2body.applyLinearImpulse(new Vector2(IMPULSE_X, 0), b2body.getWorldCenter(), true);
        } else if (b2body.getLinearVelocity().x > SPEED_X_MAX) {
            b2body.setLinearVelocity(LINEAR_SPEED_X, b2body.getLinearVelocity().y);
        }

        setPosition(b2body.getPosition().x - scale * PLAYER_WIDTH / 2 / InfinityRun.PPM
                , b2body.getPosition().y - scale * PLAYER_HEIGHT / 2 / InfinityRun.PPM);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        TextureRegion currentTexture = neutralTexture;

        if (this.fillColor.equals(GameScreen.FAILURE_COLOR)){
            currentTexture = failTexture;
        }else if (this.fillColor.equals(GameScreen.SUCCESS_COLOR)){
            currentTexture = successTexture;
        }

        spriteBatch.draw(
                currentTexture,
                getX(),
                getY(),
                scale * PLAYER_WIDTH / InfinityRun.PPM,
                scale * PLAYER_HEIGHT / InfinityRun.PPM);
    }

    @Override
    public void dispose() {
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
        if (jumpsLeft == 2) {
            jumpStrength = JUMP_IMPULSE;
            jumpsLeft--;
        } else if(jumpsLeft == 1){
            if(b2body.getLinearVelocity().y <= 0) {
                b2body.setLinearVelocity(b2body.getLinearVelocity().x, 0);
            }
            else {
                b2body.setLinearVelocity(b2body.getLinearVelocity().x,
                        b2body.getLinearVelocity().y / 2);
            }
            jumpStrength = JUMP_IMPULSE;
            jumpsLeft--;
        }
        b2body.applyLinearImpulse(new Vector2(0, jumpStrength), b2body.getWorldCenter(), true);
        return Math.abs(jumpStrength - JUMP_IMPULSE) < 0.001;
    }

    public void setColor(float r, float g, float b) {
        this.fillColor = new Color(r, g, b, 1);
    }

    public void setColor(Color c) {
        this.fillColor = c;
    }

    public void resetJump() {
        jumpsLeft = 2;
    }

    public void setScale(float scale) {
        this.scale = this.scale * scale;
    }
}
