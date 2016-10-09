package com.teamturtle.infinityrun;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Text om klassen
 *
 * @author David Söderberg
 * @since 2016-10-07
 */

public class AnimatedActor extends Image {
    private final AnimationDrawable drawable;

    public AnimatedActor(AnimationDrawable drawable) {
        super(drawable);
        this.drawable = drawable;
    }

    @Override
    public void act(float delta) {
        drawable.act(delta);
        super.act(delta);
    }
}
