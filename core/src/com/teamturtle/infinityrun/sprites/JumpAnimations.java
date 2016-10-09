package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teamturtle.infinityrun.InfinityRun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericwenn on 10/9/16.
 */
public class JumpAnimations extends AbstractEntity {

    private static final int CIRCLE_END_RADIUS = 30;
    private static final int CIRCLE_START_RADIUS = 2;
    private static final int CIRCLE_ANIMATION_STEPS = 30;
    private static final int CIRCLE_SEGMENTS = 20;

    private ShapeRenderer shapeRenderer;
    private List<JumpAnimation> mAnimations;

    public JumpAnimations() {
        shapeRenderer = new ShapeRenderer();
        mAnimations = new ArrayList<JumpAnimation>();
    }


    public void createNew(float x, float y) {
        mAnimations.add(new JumpAnimation(x, y, CIRCLE_START_RADIUS, 1, 0));
    }

    @Override
    public void update(float dt) {
        List<JumpAnimation> ja = new ArrayList<JumpAnimation>();
        for (JumpAnimation a : mAnimations) {
            if( a.frame > CIRCLE_ANIMATION_STEPS) {
                ja.add(a);
            }
        }

        mAnimations.removeAll(ja);


        for (JumpAnimation a : mAnimations) {
            a.radius = CIRCLE_START_RADIUS + (a.frame / (float) CIRCLE_ANIMATION_STEPS) * (CIRCLE_END_RADIUS - CIRCLE_START_RADIUS);
            a.alpha = (CIRCLE_ANIMATION_STEPS - a.frame) / (float) CIRCLE_ANIMATION_STEPS;
            a.frame++;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

        spriteBatch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());

        for (JumpAnimation a : mAnimations) {
            shapeRenderer.setColor( new Color(1,1,1, a.alpha));
            shapeRenderer.circle(a.x, a.y, a.radius / InfinityRun.PPM, CIRCLE_SEGMENTS);
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        spriteBatch.begin();

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }


    private class JumpAnimation {
        public float x;
        public float y;
        public float radius;
        public int frame;
        public float alpha;

        public JumpAnimation(float x, float y, float radius, float alpha, int frame) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.frame = frame;
        }
    }
}

