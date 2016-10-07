package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.teamturtle.infinityrun.InfinityRun;

import java.util.LinkedList;

/**
 * Created by ericwenn on 10/7/16.
 */
public class PlayerTail extends AbstractEntity {
    private Player mPlayer;
    private LinkedList<Vector2> tailList;
    private Texture mTexture;
    private TextureRegion[] frames;

    private static final int TAIL_LENGTH = 30;
    private static final int TAIL_INTERVAL = 1;

    private static final int FRAME_COLS = 5;
    private static final int FRAME_ROWS = 3;

    private int addIndex = 0;

    public PlayerTail(Player player) {
        mPlayer = player;
        tailList = new LinkedList<Vector2>();
        mTexture = new Texture( "player_tail.png");
        TextureRegion[][] tr = TextureRegion.split(mTexture, mTexture.getWidth() / FRAME_COLS, mTexture.getHeight() / FRAME_ROWS);
        frames = new TextureRegion[FRAME_COLS * FRAME_COLS];
        int index = 0;
        for(int i = 0; i < FRAME_ROWS; i++) {
            for( int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tr[i][j];
            }
        }

    }

    @Override
    public void update(float dt) {
        if( ++addIndex % TAIL_INTERVAL == 0) {
            tailList.addFirst( new Vector2(mPlayer.getX(), mPlayer.getY()) );
            if( tailList.size() > TAIL_LENGTH) {
                tailList.removeLast();
            }

        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        int i = 0;
        for (Vector2 v : tailList) {
            spriteBatch.draw(frames[(i++/2)], v.x, v.y, 32 / InfinityRun.PPM, 32/ InfinityRun.PPM);
        }
    }

    @Override
    public void dispose() {

    }
}
