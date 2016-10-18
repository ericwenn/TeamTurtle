package com.teamturtle.infinityrun.collisions;

import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

/**
 * Created by ericwenn on 9/25/16.
 */
public interface IEventHandler {

    void onCollisionWithEmoji( EmojiCollisionListener l);


    void onCollisionWithObstacle( ObstacleCollisionListener l);

    void onLevelFinished( LevelFinishedListener l);

    void onQuestChanged( QuestChangedListener l);

    void onCollisionWithGround(GroundCollisionListener l);


    interface ObstacleCollisionListener {
        void onCollision(Player p);
    }

    interface LevelFinishedListener {
        void onLevelFinished();
    }

    interface QuestChangedListener {
        void onQuestChanged();
    }


    interface EmojiCollisionListener {
        void onCollision(Player p, Emoji e);
    }

    interface GroundCollisionListener {
        void onCollision(HitDirection d);
    }

    enum HitDirection {
        UPWARDS,
        DOWNWARDS
    }
}
