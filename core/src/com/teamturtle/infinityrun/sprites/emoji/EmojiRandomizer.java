package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by ericwenn on 9/23/16.
 */
public class EmojiRandomizer {

    public EmojiRandomizer() {

    }

    public Emoji getNext() {
        return new Emoji("Äpple", "audio/apple.wav", new Texture("emoji/1f34e.png"));
    }
}
