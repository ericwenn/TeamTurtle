package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ericwenn on 10/1/16.
 */
public class MissionStage extends Stage {

    public MissionStage() {
        super( new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
    }
}
