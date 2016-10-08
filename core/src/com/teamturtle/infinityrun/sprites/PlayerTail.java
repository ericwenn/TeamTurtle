package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ericwenn on 10/7/16.
 */
public class PlayerTail extends AbstractEntity {
    private Player mPlayer;

    private float[] vertices;

    private static final int TAIL_LENGTH = 40;
    private static final int TAIL_INTERVAL = 1;


    private int addIndex = 0;


    private Color lineColor;
    private float lineWidth = 2f;

    public PlayerTail(Player player) {
        mPlayer = player;

        lineColor = new Color(1,1,1,1);
        vertices = new float[ TAIL_LENGTH * 2];
    }

    @Override
    public void update(float dt) {
        if( ++addIndex % TAIL_INTERVAL == 0) {
            addToVertices( mPlayer.getX() + Player.PLAYER_WIDTH / (2 * InfinityRun.PPM), mPlayer.getY() + Player.PLAYER_HEIGHT / (2*InfinityRun.PPM));
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.end();


        Gdx.gl.glLineWidth(lineWidth);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix( spriteBatch.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Line);
        Color c1 = new Color(lineColor);
        Color c2 = new Color(lineColor);


        for( int i = 0; i < vertices.length - 2; i = i+2) {
            c1.a = (vertices.length - i) / (float)vertices.length;
            c2.a = (vertices.length - i - 1) / (float)vertices.length;
            sr.line(vertices[i], vertices[i + 1], vertices[i + 2], vertices[i + 3], c1, c2);
        }
        sr.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        spriteBatch.begin();
    }

    @Override
    public void dispose() {

    }


    private void addToVertices(float x, float y) {


        for( int i = vertices.length - 1; i > 1; i--) {
            if (vertices[i - 2] == 0) {
                vertices[i] = i % 2 == 0 ? x : y;
            } else {
                vertices[i] = vertices[i - 2];
            }
        }
        vertices[0] = x;
        vertices[1] = y;
    }


    
    public void setColor(float r, float g, float b) {
        this.lineColor = new Color(r, g, b, 1);
    }

    public void setLineWidth(float width) {
        this.lineWidth = width;
    }
}
