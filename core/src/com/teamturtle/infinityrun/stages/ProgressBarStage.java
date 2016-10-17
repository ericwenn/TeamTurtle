package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
    private float levelWidth;
    private float startX;
    private ProgressBar mProgressBar;

    public enum MissionStatus {
        PASSED,
        FAILED,
    }

    public ProgressBarStage(TiledMap map, List<Mission> missions) {
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.missions = missions;



        levelWidth = missions.get( missions.size() - 1).getEndPosition() - missions.get(1).getStartPosition();
        startX = missions.get(1).getStartPosition();

        float[] beginningMarkers = new float[missions.size()];
        float[] endingMarkers = new float[missions.size()];
        int index = 0;
        for (Mission m : missions) {
            beginningMarkers[index] = InfinityRun.WIDTH * (m.getStartPosition() - startX)  / levelWidth;
            endingMarkers[index++] = InfinityRun.WIDTH * (m.getEndPosition() - startX)  / levelWidth;
        }
        mProgressBar = new ProgressBar(beginningMarkers, endingMarkers);
        addActor(mProgressBar);
    }


    public void updatePlayerProgress(float x) {
        mProgressBar.setProgress( InfinityRun.WIDTH * (x - startX) / levelWidth);
    }

    public void updateMissionStatus(Mission m, MissionStatus status) {
        mProgressBar.setMarkerStatus(missions.indexOf(m), status == MissionStatus.PASSED ? 1 : -1);
    }




    private static class ProgressBar extends Actor {
        private final float[] beginningMarkers;
        private final float[] endingMarkers;
        // Initialized as zeros.
        private int[] markerStatus;
        private ShapeRenderer shapeRenderer;
        private float progress = 0;

        public static final Color SUCCESS_COLOR = new Color((float) 50/255, (float) 205/255, (float) 50/255, 1);
        public static final Color FAILURE_COLOR = new Color((float) 194/255, (float) 59/255, (float) 34/255, 1);
        public static final Color BORDER_COLOR = new Color(0,0,0,0.2f);
        public static final Color PROGRESS_COLOR = new Color(1,1,1,.6f);


        private static final int HEIGHT = 20;

        public ProgressBar(float[] beginningMarkers, float[] endingMarkers) {
            this.beginningMarkers = beginningMarkers;
            this.endingMarkers = endingMarkers;
            this.markerStatus = new int[endingMarkers.length];

            shapeRenderer = new ShapeRenderer();
            shapeRenderer.setAutoShapeType(true);
        }

        protected void setProgress(float progress) {
            this.progress = progress;
        }
        protected void setMarkerStatus(int index, int status) {
            markerStatus[index] = status;
        }


        @Override
        public void draw(Batch batch, float parentAlpha) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

            batch.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


            shapeRenderer.setColor(PROGRESS_COLOR);
            shapeRenderer.rect(0, 0, progress, HEIGHT);

            // Fill success or failure missions
            for( int i = 0; i < beginningMarkers.length; i++) {
                if (markerStatus[i] != 0) {
                    Color c = markerStatus[i] < 0 ? FAILURE_COLOR : SUCCESS_COLOR;
                    shapeRenderer.setColor(c);
                    shapeRenderer.rect( beginningMarkers[i], 0, Math.min(endingMarkers[i] - beginningMarkers[i], progress - beginningMarkers[i]), HEIGHT);
                }
            }

            // Outline all missions
            shapeRenderer.set(ShapeRenderer.ShapeType.Line);
            Color c = BORDER_COLOR;
            shapeRenderer.setColor(c);
            for( int i = 0; i < beginningMarkers.length; i++) {
                shapeRenderer.rect( beginningMarkers[i], 0, endingMarkers[i] - beginningMarkers[i], HEIGHT);
            }

            shapeRenderer.end();

            batch.begin();
        }
    }
}
