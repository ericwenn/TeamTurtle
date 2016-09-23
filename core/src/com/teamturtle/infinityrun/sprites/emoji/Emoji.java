package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by Henrik on 2016-09-21.
 */
public class Emoji extends Sprite{

    private static final int SHOW_TIME = 10;
    private static final float TEXT_OFFSET = 150f;

    private String emojiName;
    private Sound emojiSound;
    private BitmapFont font;
    private SpriteBatch sb;
    private Texture texture;
    private boolean show;
    private int time = 0;

    public Emoji(String emojiName, String soundURL, Texture texture, SpriteBatch sb){
        super(texture);
        this.emojiName = emojiName;
        this.texture = texture;
        this.sb = sb;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2,2);

        emojiSound = Gdx.audio.newSound(Gdx.files.internal(soundURL));
        setPosition(400, InfinityRun.HEIGHT - 100);
    }

    public void show(){
        show = true;
    }

    public void render() {
        sb.begin();
        sb.draw( texture, getX(), getY());
        sb.end();
        /*
        if (show && time == 0) {
            emojiSound.stop();
            emojiSound.play();
            time++;
        }else if(show && time < SHOW_TIME) {
            sb.begin();
            sb.draw(texture, getX(), getY() - TEXT_OFFSET);
            font.draw(sb, emojiName, getX(), getY());
            sb.end();
            time++;
        }else{
            time = 0;
            show = false;
        }
        */
    }

}
