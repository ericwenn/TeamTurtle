package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by ericwenn on 9/23/16.
 */
public class EmojiFactory {
    private final World world;
    private final TiledMap tiledMap;
    private int emojiLayer;

    private EmojiRandomizer emojiRandomizer;
    private Array<Body> bodies;

    public EmojiFactory(World world, TiledMap tiledMap, SpriteBatch spriteBatch, int emojiLayer) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.emojiLayer = emojiLayer;
        this.bodies = new Array<Body>();
        this.emojiRandomizer = new EmojiRandomizer(spriteBatch);
    }


    /**
     * Creates a randomly generated {@link Emoji} in each rectangle defined in tilemap.
     */
    public void create() {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        for(MapObject object : tiledMap.getLayers().get(emojiLayer).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect =((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
            Body body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);

            body.setUserData( emojiRandomizer.getNext() );
            bodies.add(body);

        }

    }


    public Array<Body> getBodies() {
        return bodies;
    }
}
