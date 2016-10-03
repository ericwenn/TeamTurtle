package com.teamturtle.infinityrun.models;

import java.util.LinkedList;

/**
 * Created by ericwenn on 10/3/16.
 */
public class MissionHandler {

    private LinkedList<Mission> mMissions = new LinkedList<Mission>();
    private int currIndex = 0;

    void addMission(int startX, int endX) {
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


    public Mission getNextMission() {
        Mission nextMission = mMissions.get(currIndex);
        currIndex++;
        return nextMission;
    }

}
