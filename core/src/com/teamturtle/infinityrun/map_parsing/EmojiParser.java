package com.teamturtle.infinityrun.map_parsing;

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
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.Mission;
import com.teamturtle.infinityrun.models.MissionHandler;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.sprites.Entity;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.models.words.WordRandomizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericwenn on 9/23/16.
 */
public class EmojiParser implements MapParser {
    private final World world;
    private final TiledMap tiledMap;
    private String emojiPlaceholderName;
    private MissionHandler missionHandler;
    private WordRandomizer wordRandomizer;

    private List<Emoji> emojis = new ArrayList<Emoji>();


    public EmojiParser(World world, TiledMap tiledMap, String emojiPlaceholderName, MissionHandler missionHandler, List<Word> possibleWords) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.emojiPlaceholderName = emojiPlaceholderName;
        this.missionHandler = missionHandler;
        this.wordRandomizer = new WordRandomizer(possibleWords);
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
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / InfinityRun.PPM
                    , (rect.getY() + rect.getHeight() / 2) / InfinityRun.PPM);
            Body body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2) / InfinityRun.PPM
                    , (rect.getHeight() / 2) / InfinityRun.PPM);
            fdef.shape = shape;
            Fixture fixture = body.createFixture(fdef);
            fixture.setSensor(true);


            Mission mission = missionHandler.getMissionAtPosition( rect.getX() );

            Word word;
            do {
                word = wordRandomizer.getNext();
            } while (mission.haveWord(word));

            mission.addWord(word);
            mission.decideCorrectWord();

            Emoji emoji = new Emoji(word);
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
