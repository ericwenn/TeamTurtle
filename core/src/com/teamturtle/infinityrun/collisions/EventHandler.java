package com.teamturtle.infinityrun.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.teamturtle.infinityrun.map_parsing.SensorParser;
import com.teamturtle.infinityrun.sound.FxSound;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.util.Random;

/**
 * Created by ericwenn on 9/25/16.
 */
public class EventHandler implements IEventHandler, ContactListener {


    private EmojiCollisionListener mEmojiCollisionListener;
    private ObstacleCollisionListener mObstacleCollisionHandler;
    private LevelFinishedListener mLevelFinishedListener;
    private QuestChangedListener mQuestChangedListener;
    private GroundCollisionListener mGroundCollisionListener;

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
    public void onCollisionWithGround(GroundCollisionListener l) {
        mGroundCollisionListener = l;
    }


    @Override
    public void beginContact(Contact contact) {
        Object obj1 = contact.getFixtureA().getUserData();
        Object obj2 = contact.getFixtureB().getUserData();

        if (obj1 == null || obj2 == null) {
            return;
        }


        if (mGroundCollisionListener != null) {
            if(obj1 instanceof Player && "ground".equals(obj2)) {
                mGroundCollisionListener.onCollision(contact.getWorldManifold().getNormal().y >= 0 ? HitDirection.DOWNWARDS : HitDirection.UPWARDS);

            } else if( obj2 instanceof Player && "ground".equals(obj1)) {
                mGroundCollisionListener.onCollision(contact.getWorldManifold().getNormal().y >= 0 ? HitDirection.DOWNWARDS : HitDirection.UPWARDS);

            }
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
            FxSound[] sounds = {FxSound.MISSLYCKANDE1, FxSound.MISSLYCKANDE2,
                    FxSound.MISSLYCKANDE3, FxSound.MISSLYCKANDE4,
                    FxSound.MISSLYCKANDE5, FxSound.MISSLYCKANDE6,
                    FxSound.MISSLYCKANDE7, FxSound.MISSLYCKANDE8,
                    FxSound.MISSLYCKANDE9, FxSound.MISSLYCKANDE10};
            Random rand = new Random();
            int soundId = rand.nextInt(9);
            if (SensorParser.Type.OBSTACLE.getName().equals(obj1) && obj2 instanceof Player) {

                mObstacleCollisionHandler.onCollision((Player) obj2);
                sounds[soundId].play();
                return;
            }
            if (SensorParser.Type.OBSTACLE.getName().equals(obj2) && obj1 instanceof Player) {
                mObstacleCollisionHandler.onCollision((Player) obj1);
                sounds[soundId].play();
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
