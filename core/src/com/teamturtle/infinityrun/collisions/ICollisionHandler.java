package com.teamturtle.infinityrun.collisions;

import com.teamturtle.infinityrun.map_parsing.SensorParser;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.sprites.Player;

/**
 * Created by ericwenn on 9/25/16.
 */
public interface ICollisionHandler {

    void onCollisionWithSensor(SensorCollisionListener l);
    void onCollisionWithEmoji( EmojiCollisionListener l);


    interface SensorCollisionListener {
        void onCollision(Player p, SensorParser.Type type);
    }

    interface EmojiCollisionListener {
        void onCollision(Player p, Emoji e);
    }
}
