package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ericwenn on 9/27/16.
 */
public interface Entity {
    void update(float dt);

    void render(SpriteBatch spriteBatch);
}
