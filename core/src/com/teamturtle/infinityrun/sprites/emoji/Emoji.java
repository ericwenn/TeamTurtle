package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sprites.Entity;

/**
 * Created by Henrik on 2016-09-21.
 */
public class Emoji extends Sprite implements Entity{

    private static final int SHOW_TIME = 10;
    private static final float TEXT_OFFSET = 150f;
    private static final float EXPLOSION_SCALE = 1.3f;
    public static final float EMOJI_SIZE = 32;


    private String emojiName;
    private Sound emojiSound;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    private SpriteBatch sb;
    private Texture texture;


    private boolean isExploded = false;
    private Body mBody;

    public Emoji(String emojiName, String soundURL, Texture texture, SpriteBatch sb){
        super(texture);
        this.emojiName = emojiName;
        this.texture = texture;
        this.sb = sb;


        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2,2);

        glyphLayout = new GlyphLayout( font, emojiName);



        emojiSound = Gdx.audio.newSound(Gdx.files.internal(soundURL));
        setPosition(400, InfinityRun.HEIGHT - 100);
    }




    public void setBody(Body body) {
        mBody = body;
    }



    public void triggerExplode() {
        isExploded = true;
        emojiSound.play();
    }

    @Override
    public void update(float dt) {
        setPosition(mBody.getPosition().x - Emoji.EMOJI_SIZE / 2, mBody.getPosition().y - Emoji.EMOJI_SIZE / 2);
    }

    @Override
    public void render(SpriteBatch sb) {
        if( isExploded ) {
            sb.draw( texture, getX(), getY(), EMOJI_SIZE*EXPLOSION_SCALE, EMOJI_SIZE*EXPLOSION_SCALE);
            font.draw( sb, glyphLayout, getX() + (EMOJI_SIZE - glyphLayout.width) / 2, getY() + EMOJI_SIZE + 50);
        } else {
            sb.draw( texture, getX(), getY(), EMOJI_SIZE, EMOJI_SIZE);
        }
    }

}
