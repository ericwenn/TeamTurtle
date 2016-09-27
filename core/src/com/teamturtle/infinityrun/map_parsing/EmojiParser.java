package com.teamturtle.infinityrun.map_parsing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.teamturtle.infinityrun.sprites.Entity;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.sprites.emoji.EmojiRandomizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericwenn on 9/23/16.
 */
public class EmojiParser implements MapParser {
    private final World world;
    private final TiledMap tiledMap;
    private String emojiPlaceholderName;
    private EmojiRandomizer emojiRandomizer;

    private List<Emoji> emojis = new ArrayList<Emoji>();


    public EmojiParser(World world, TiledMap tiledMap, SpriteBatch spriteBatch, String emojiPlaceholderName) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.emojiPlaceholderName = emojiPlaceholderName;
        this.emojiRandomizer = new EmojiRandomizer(spriteBatch);
    }


    /**
     * Creates a randomly generated {@link Emoji} in each rectangle defined in tilemap.
     */
    public void parse() {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        for(MapObject object : tiledMap.getLayers().get(emojiPlaceholderName).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect =((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
            Body body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            Fixture fixture = body.createFixture(fdef);
            fixture.setSensor(true);
            Emoji emoji = emojiRandomizer.getNext();

            emoji.setBody( body );
            fixture.setUserData(emoji);
            emojis.add(emoji);

        }

    }


    @Override
    public List<? extends Entity> getEntities() {
        return emojis;
    }
}
