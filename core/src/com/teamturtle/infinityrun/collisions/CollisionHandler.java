package com.teamturtle.infinityrun.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.teamturtle.infinityrun.map_parsing.SensorParser;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.sprites.Player;

/**
 * Created by ericwenn on 9/25/16.
 */
public class CollisionHandler implements ICollisionHandler, ContactListener {


    private SensorCollisionListener mSensorCollisionListener;
    private EmojiCollisionListener mEmojiCollisionListener;

    public CollisionHandler() {
    }

    @Override
    public void onCollisionWithSensor(SensorCollisionListener l) {
        mSensorCollisionListener = l;
    }

    @Override
    public void onCollisionWithEmoji(EmojiCollisionListener l) {
        mEmojiCollisionListener = l;
    }


    @Override
    public void beginContact(Contact contact) {
        System.out.println("Begin contact");
        Object obj1 = contact.getFixtureA().getUserData();
        Object obj2 = contact.getFixtureB().getUserData();

        if (obj1 == null || obj2 == null) {
            return;
        }

        for (SensorParser.Type sensorType : SensorParser.Type.values()) {
            if (obj1 == sensorType.getName() && obj2 instanceof Player) {
                mSensorCollisionListener.onCollision((Player) obj2, sensorType);
            }
            if (obj2 == sensorType.getName() && obj1 instanceof Player) {
                mSensorCollisionListener.onCollision((Player) obj1, sensorType);
            }
        }

        if (obj1 instanceof Player && obj2 instanceof Emoji) {
            mEmojiCollisionListener.onCollision((Player) obj1, (Emoji) obj2);
        } else if(obj1 instanceof Emoji && obj2 instanceof Player) {
            mEmojiCollisionListener.onCollision((Player) obj2, (Emoji) obj1);
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
