package com.teamturtle.infinityrun.sprites;

/**
 * Created by ericwenn on 9/27/16.
 */
public abstract class AbstractEntity implements Entity {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }


}
