package com.teamturtle.infinityrun.collisions;

import com.badlogic.gdx.Gdx;
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
        System.out.println("Begin contact");
        Object obj1 = contact.getFixtureA().getUserData();
        Object obj2 = contact.getFixtureB().getUserData();

        if (obj1 == null || obj2 == null) {
            return;
        }


        // Check emoji
        if (obj1 instanceof Player && obj2 instanceof Emoji) {
            mEmojiCollisionListener.onCollision((Player) obj1, (Emoji) obj2);
            return;
        } else if(obj1 instanceof Emoji && obj2 instanceof Player) {
            mEmojiCollisionListener.onCollision((Player) obj2, (Emoji) obj1);
            return;
        }




        // Check obstacle
        Gdx.app.log("EventHandler1", obj1.toString());
        Gdx.app.log("EventHandler2", obj2.toString());
        if (obj1 == SensorParser.Type.OBSTACLE.getName() && obj2 instanceof Player) {
            mObstacleCollisionHandler.onCollision((Player) obj2);
            return;
        }
        if (obj2 == SensorParser.Type.OBSTACLE.getName() && obj1 instanceof Player) {
            mObstacleCollisionHandler.onCollision((Player) obj1);
            return;
        }




        if (mQuestChangedListener != null) {
            // Check quest
            if (obj1 == SensorParser.Type.QUEST.getName() && obj2 instanceof Player) {
                mQuestChangedListener.onQuestChanged();
                return;
            }
            if (obj2 == SensorParser.Type.QUEST.getName() && obj1 instanceof Player) {
                mQuestChangedListener.onQuestChanged();
                return;
            }

        }



        // Check goal
        if (obj1 == SensorParser.Type.GOAL.getName() && obj2 instanceof Player) {
            mLevelFinishedListener.onLevelFinished();
            return;
        }
        if (obj2 == SensorParser.Type.GOAL.getName() && obj1 instanceof Player) {
            mLevelFinishedListener.onLevelFinished();
            return;
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
