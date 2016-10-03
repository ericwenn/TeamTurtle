package com.teamturtle.infinityrun.models;

import java.util.LinkedList;

/**
 * MissionHandler holds all missions on a map.
 * {@link MissionHandler#getNextMission()} will return the next mission, and throw {@link IndexOutOfBoundsException} if there are no more missions on the map.
 */
public class MissionHandler {

    private LinkedList<Mission> mMissions = new LinkedList<Mission>();
    private int iteratorIndex = 0;

    public void addMission(float startX, float endX) {
        mMissions.add( new MissionImpl(startX, endX));
    }

    public Mission getMissionAtPosition(int posX) {
        for (Mission m : mMissions) {
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
