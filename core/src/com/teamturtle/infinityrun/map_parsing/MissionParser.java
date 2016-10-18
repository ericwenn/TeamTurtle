package com.teamturtle.infinityrun.map_parsing;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.teamturtle.infinityrun.models.MissionHandler;

import java.io.Serializable;
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


    private final TiledMap tiledMap;
    private final String layerName;


    private final List<Rectangle> mMissionRectangles = new ArrayList<Rectangle>();

    public MissionParser(TiledMap tiledMap, String layerName) {
        this.tiledMap = tiledMap;
        this.layerName = layerName;
    }

    private void parse() {
        for (MapObject object : tiledMap.getLayers().get(layerName).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            mMissionRectangles.add(rect);
        }
    }

    private void sort() {
        Collections.sort(mMissionRectangles, new RectPositionComparator());
    }


    public MissionHandler getMissionHandler() {
        parse();
        sort();

        MissionHandler missionHandler = new MissionHandler();
        for( int i = 0; i<mMissionRectangles.size(); i++) {
            float startX = i == 0 ? 0 : mMissionRectangles.get(i - 1).getX();
            float endX = mMissionRectangles.get(i).getX();

            missionHandler.addMission(startX, endX);

        }

        return missionHandler;
    }

    private static class RectPositionComparator implements Comparator<Rectangle>, Serializable {

        private static final long serialVersionUID = 42L; // arbitrary number
        @Override
        public int compare(Rectangle o1, Rectangle o2) {
            return (int)(o1.getX() - o2.getX());
        }


    }

}
