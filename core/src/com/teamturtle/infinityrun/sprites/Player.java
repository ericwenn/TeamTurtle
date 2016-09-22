package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.screens.GameScreen;

/**
 * Created by ericwenn on 9/20/16.
 */
public class Player extends Sprite {

    private Vector2 velo;

    public Player(Texture t) {
        super( t );
        velo = new Vector2( GameScreen.GAME_SPEED, -80);

        setPosition(100, InfinityRun.HEIGHT / 2);
    }




    public void update(float dt) {
        // Update x
        setPosition( getX() + velo.x * dt, getY() );

        // Update y
        float y;
        if( getY() > 64 ) {
            y = getY() + velo.y * dt;
        } else {
            y = getY();
        }
        setPosition( getX(), y );
    }
}
