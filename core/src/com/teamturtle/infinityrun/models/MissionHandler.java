package com.teamturtle.infinityrun.models;

import com.badlogic.gdx.Gdx;

import java.util.LinkedList;

/**
 * MissionHandler holds all missions on a map.
 * {@link MissionHandler#getNextMission()} will return the next mission, and throw {@link IndexOutOfBoundsException} if there are no more missions on the map.
 */
public class MissionHandler {

    private LinkedList<Mission> mMissions = new LinkedList<Mission>();
    private int iteratorIndex = 0;

    public void addMission(float startX, float endX) {
        Gdx.app.log("MissionHandler", "addMission" + "start: " + Float.toString(startX) + " end: " + Float.toString(endX));
        mMissions.add( new MissionImpl(startX, endX));
    }

    public Mission getMissionAtPosition(float posX) {
        Gdx.app.log("MissionHandler", "getMissionAtPosition" + Float.toString(posX));
        for (Mission m : mMissions) {
            Gdx.app.log("MissionHandler", "mission_start: "+ Float.toString(m.getStartPosition()) + " mission_end: "+ Float.toString(m.getEndPosition()));
            if (m.getStartPosition() < posX && posX < m.getEndPosition()) {
                return m;
            }
        }

        throw new IndexOutOfBoundsException("No mission exists at this position");
    }


    public Mission getNextMission() throws IndexOutOfBoundsException {
        Mission nextMission = mMissions.get(iteratorIndex);
        iteratorIndex++;
        return nextMission;
    }

}
