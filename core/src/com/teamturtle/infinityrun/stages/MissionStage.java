package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ericwenn on 10/1/16.
 */
public class MissionStage extends Stage {

    private static final int TABLE_WIDTH = 100,
            TABLE_HEIGHT = 100;

    public MissionStage() {
        super( new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));


        Table missionTable = new Table(new Skin(Gdx.files.internal("skin/uiskin.json")));
        missionTable.setSize(TABLE_WIDTH, TABLE_HEIGHT);
        missionTable.setPosition( 0, InfinityRun.HEIGHT - TABLE_HEIGHT);


        Image emojiImage = new Image(new Texture(Gdx.files.internal("emoji/1f34e.png")));
        emojiImage.setScaling(Scaling.fit);
        missionTable.add(emojiImage).height(50);
        this.addActor(missionTable);
    }
}
