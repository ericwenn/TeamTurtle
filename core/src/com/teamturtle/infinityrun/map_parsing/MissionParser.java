package com.teamturtle.infinityrun.map_parsing;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.teamturtle.infinityrun.models.MissionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A special kind of parser, as it doesnt implement {@link MapParser} and doesnt create any {@link com.teamturtle.infinityrun.sprites.Entity}.
 * What is does is create a {@link MissionHandler} based on the map.
 * So kind of a MapParser, but still not.
 */

public class MissionParser {


    private final World world;
    private final TiledMap tiledMap;
    private final String layerName;


    private List<Rectangle> mMissionRectangles = new ArrayList<Rectangle>();

    public MissionParser(World world, TiledMap tiledMap, String layerName) {
        this.world = world;
        this.tiledMap = tiledMap;
        this.layerName = layerName;
    }

    void parse() {
        for (MapObject object : tiledMap.getLayers().get(layerName).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            mMissionRectangles.add(rect);
        }
    }

    void sort() {
        Collections.sort(mMissionRectangles, new Comparator<Rectangle>() {
            @Override
            public int compare(Rectangle o1, Rectangle o2) {
                return (int)(o1.getX() - o2.getX());
            }
        });
    }


    public MissionHandler getMissionHandler() {
        parse();
        sort();

        MissionHandler missionHandler = new MissionHandler();
        for( int i = 0; i<mMissionRectangles.size(); i++) {
            float startX = mMissionRectangles.get(i).getX();
            float endX = i == mMissionRectangles.size() - 1 ? tiledMap.getProperties().get("width", Integer.class) : mMissionRectangles.get(i + 1).getX();

            missionHandler.addMission(startX, endX);

        }

        return missionHandler;
    }

}
