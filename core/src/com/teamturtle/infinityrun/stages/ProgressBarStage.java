package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.Mission;

import java.util.List;

public class ProgressBarStage extends Stage {

    private List<Mission> missions;
    private int levelWidth;
    private ProgressBar mProgressBar;

    public enum MissionStatus {
        PASSED,
        FAILED,
        UNFINISHED
    }

    public ProgressBarStage(TiledMap map, List<Mission> missions) {
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.missions = missions;

        int nHorizontalTiles = (Integer) map.getProperties().get("width");
        int tileWidth = (Integer) map.getProperties().get("tilewidth");


        levelWidth = nHorizontalTiles * tileWidth;
        Gdx.app.log("LevelWidth", Integer.toString(levelWidth));

        float[] progressSteps = new float[missions.size()];
        int index = 0;
        for (Mission m : missions) {
            progressSteps[index++] = 100 * m.getStartPosition()  / levelWidth;
        }
        mProgressBar = new ProgressBar(progressSteps);
        addActor(mProgressBar);
    }


    public void updatePlayerProgress(float x) {
        Gdx.app.log("PlayerProgg", "x: " + Float.toString(x) + " levelWidth: "+ levelWidth + " perc: "+ Float.toString(x / levelWidth));
        mProgressBar.setProgress( x / levelWidth);
    }

    public void updateMissionStatus(Mission m, MissionStatus status) {

    }




    private class ProgressBar extends Actor {
        private ShapeRenderer shapeRenderer;
        private float[] steps;
        private float progress = 0;

        private static final int HEIGHT = 20;

        public ProgressBar(float[] steps) {
            this.steps = steps;
            shapeRenderer = new ShapeRenderer();
        }

        public void setProgress(float progress) {
            this.progress = progress;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

            batch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, InfinityRun.WIDTH * progress, HEIGHT);
            shapeRenderer.end();

            batch.begin();
        }
    }
}
