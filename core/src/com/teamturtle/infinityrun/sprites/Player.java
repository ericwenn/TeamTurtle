package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.screens.GameScreen;

/**
 * Created by ericwenn on 9/20/16.
 */
public class Player extends Sprite {

    public Player(Texture t) {
        super( t );
        setPosition(100, InfinityRun.HEIGHT / 2);
    }


    public void update(float dt) {
        setPosition( getX() + GameScreen.GAME_SPEED * dt, getY());
    }
}
