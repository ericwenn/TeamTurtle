package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ericwenn on 10/1/16.
 */
public class MissionStage extends Stage {

    public MissionStage() {
        super( new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));


        Table missionTable = new Table(new Skin(Gdx.files.internal("skin/uiskin.json")));

        missionTable.
        this.addActor(missionTable);
    }
}
