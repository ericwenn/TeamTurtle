package com.teamturtle.infinityrun.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.teamturtle.infinityrun.map_parsing.SensorParser;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

/**
 * Created by ericwenn on 9/25/16.
 */
public class EventHandler implements IEventHandler, ContactListener {


    private EmojiCollisionListener mEmojiCollisionListener;
    private ObstacleCollisionListener mObstacleCollisionHandler;
    private LevelFinishedListener mLevelFinishedListener;
    private QuestChangedListener mQuestChangedListener;

    public EventHandler() {
    }


    @Override
    public void onCollisionWithEmoji(EmojiCollisionListener l) {
        mEmojiCollisionListener = l;
    }

    @Override
    public void onCollisionWithObstacle(ObstacleCollisionListener l) {
        mObstacleCollisionHandler = l;
    }

    @Override
    public void onLevelFinished(LevelFinishedListener l) {
        mLevelFinishedListener = l;
    }

    @Override
    public void onQuestChanged(QuestChangedListener l) {
        mQuestChangedListener = l;
    }


    @Override
    public void beginContact(Contact contact) {
        Object obj1 = contact.getFixtureA().getUserData();
        Object obj2 = contact.getFixtureB().getUserData();

        if (obj1 == null || obj2 == null) {
            return;
        }


        if (mEmojiCollisionListener != null) {
            // Check emoji
            if (obj1 instanceof Player && obj2 instanceof Emoji) {
                mEmojiCollisionListener.onCollision((Player) obj1, (Emoji) obj2);
                return;
            } else if(obj1 instanceof Emoji && obj2 instanceof Player) {
                mEmojiCollisionListener.onCollision((Player) obj2, (Emoji) obj1);
                return;
            }
        }




        // Check obstacle
        if (mObstacleCollisionHandler != null) {
            if (SensorParser.Type.OBSTACLE.getName().equals(obj1) && obj2 instanceof Player) {
                mObstacleCollisionHandler.onCollision((Player) obj2);
                return;
            }
            if (SensorParser.Type.OBSTACLE.getName().equals(obj2) && obj1 instanceof Player) {
                mObstacleCollisionHandler.onCollision((Player) obj1);
                return;
            }
        }




        if (mQuestChangedListener != null) {
            // Check quest
            if (SensorParser.Type.QUEST.getName().equals(obj1) && obj2 instanceof Player) {
                mQuestChangedListener.onQuestChanged();
                return;
            }
            if (SensorParser.Type.QUEST.getName().equals(obj2) && obj1 instanceof Player) {
                mQuestChangedListener.onQuestChanged();
                return;
            }

        }



        // Check goal
        if (mLevelFinishedListener != null) {
            if (SensorParser.Type.GOAL.getName().equals(obj1) && obj2 instanceof Player) {
                mLevelFinishedListener.onLevelFinished();
                return;
            }
            if (SensorParser.Type.GOAL.getName().equals(obj2) && obj1 instanceof Player) {
                mLevelFinishedListener.onLevelFinished();
                return;
            }

        }



    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
