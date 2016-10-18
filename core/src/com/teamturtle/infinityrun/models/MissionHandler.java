package com.teamturtle.infinityrun.models;

import java.util.ArrayList;
import java.util.List;

/**
 * MissionHandler holds all missions on a map.
 * {@link MissionHandler#getNextMission()} will return the next mission, and throw {@link IndexOutOfBoundsException} if there are no more missions on the map.
 */
public class MissionHandler {

    private final List<Mission> mMissions = new ArrayList<Mission>();
    private int iteratorIndex = 0;

    public void addMission(float startX, float endX) {
        mMissions.add( new MissionImpl(startX, endX));
    }

    public Mission getMissionAtPosition(float posX) {
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


    public List<Mission> getMissions() {
        return new ArrayList<Mission>(mMissions);
    }

}
