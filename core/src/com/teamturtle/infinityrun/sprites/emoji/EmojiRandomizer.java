package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ericwenn on 9/23/16.
 */
public class EmojiRandomizer {
    private SpriteBatch spriteBatch;

    public EmojiRandomizer(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public Emoji getNext() {
        return new Emoji("Äpple", "audio/apple.wav", new Texture("emoji/1f34e.png"), spriteBatch);
    }
}
