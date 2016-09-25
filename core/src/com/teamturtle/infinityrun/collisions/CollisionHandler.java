package com.teamturtle.infinityrun.collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

/**
 * Created by ericwenn on 9/25/16.
 */
public class CollisionHandler implements ICollisionHandler, ContactListener {


    private ObstacleCollisionListener mObstacleCollisionListener;
    private EmojiCollisionListener mEmojiCollisionListener;

    public CollisionHandler() {
    }

    @Override
    public void onCollisionWithObstable(ObstacleCollisionListener l) {
        mObstacleCollisionListener = l;
    }

    @Override
    public void onCollisionWithEmoji(EmojiCollisionListener l) {
        mEmojiCollisionListener = l;
    }


    @Override
    public void beginContact(Contact contact) {
        
        Object obj1 = contact.getFixtureA().getUserData();
        Object obj2 = contact.getFixtureB().getUserData();

        if (obj1 == null || obj2 == null) {
            return;
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
