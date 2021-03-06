package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.screens.GameScreen;

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

    private final ShapeRenderer shapeRenderer;
    private final List<JumpAnimation> mAnimations;
    private Color circleColor;

    public JumpAnimations() {
        shapeRenderer = new ShapeRenderer();
        mAnimations = new ArrayList<JumpAnimation>();
        setColor(GameScreen.NEUTRAL_PLAYER_COLOR);
    }


    public void createNew(float x, float y) {
        mAnimations.add(new JumpAnimation(x, y, CIRCLE_START_RADIUS, 1, 0));
    }

    public void setColor(float r, float g, float b) {
        this.circleColor = new Color(r, g, b, 1);
    }

    public void setColor(Color c) {
        this.circleColor = c;
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

        Color c = new Color(circleColor);

        for (JumpAnimation a : mAnimations) {
            c.a = a.alpha;
            shapeRenderer.setColor( c );
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


    private static class JumpAnimation {
        private float x;
        public float y;
        public float radius;
        public int frame;
        public float alpha;

        public JumpAnimation(float x, float y, float radius, float alpha, int frame) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.alpha = alpha;
            this.frame = frame;
        }
    }
}

