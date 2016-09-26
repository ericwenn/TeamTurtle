package com.teamturtle.infinityrun.collisions;

import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

/**
 * Created by ericwenn on 9/25/16.
 */
public interface ICollisionHandler {

    void onCollisionWithObstable(ObstacleCollisionListener l);
    void onCollisionWithEmoji( EmojiCollisionListener l);


    interface ObstacleCollisionListener {
        void onCollision(Player p);
    }

    interface EmojiCollisionListener {
        void onCollision(Player p, Emoji e);
    }
}
