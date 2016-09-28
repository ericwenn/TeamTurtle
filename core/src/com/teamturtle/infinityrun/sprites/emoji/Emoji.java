package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sprites.AbstractEntity;

/**
 * Created by Henrik on 2016-09-21.
 */
public class Emoji extends AbstractEntity {

    private static final float EXPLOSION_SCALE = 1.3f;
    public static final float EMOJI_SIZE = 32;


    private String emojiName;
    private Sound emojiSound;

    private Texture texture;


    private boolean isExploded = false;
    private Body mBody;

    public Emoji(String emojiName, String soundURL, Texture texture){
        this.emojiName = emojiName;
        this.texture = texture;

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
        setPosition(mBody.getPosition().x - ((Emoji.EMOJI_SIZE / 2) / InfinityRun.PPM)
                , mBody.getPosition().y - ((Emoji.EMOJI_SIZE / 2) / InfinityRun.PPM));
    }

    @Override
    public void render(SpriteBatch sb) {
        if( isExploded ) {
            //If the emoji as the same position when it has expended, it will look like it moves to
            //the left, with dx, the emoji will "move" equally to left and right.
            float dx = ((EMOJI_SIZE * EXPLOSION_SCALE) - (EMOJI_SIZE)) / ( 2 * InfinityRun.PPM);
            sb.draw( texture, getX() - dx, getY(), EMOJI_SIZE*EXPLOSION_SCALE / InfinityRun.PPM
                    , EMOJI_SIZE*EXPLOSION_SCALE / InfinityRun.PPM);
        } else {
            sb.draw( texture, getX(), getY(), EMOJI_SIZE / InfinityRun.PPM, EMOJI_SIZE / InfinityRun.PPM);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        mBody.getWorld().destroyBody(mBody);
        emojiSound.dispose();
    }
    public boolean getIsExploded(){
        return isExploded;
    }
    public String getEmojiName(){
        return emojiName;
    }
}
