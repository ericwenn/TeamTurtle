package com.teamturtle.infinityrun.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ericwenn on 10/18/16.
 */
public class LoadingStage extends Stage {

    public static final String BG_URL = "new_background.png";
    public static final String TURTLE_URL = "emoji/good/1f422.png";



    private static final int FRAME_ROWS = 1;
    private static final int FRAME_COLS = 20;
    private static final int TURTLE_PAD = 18;


    private ProgressBar mProgressBar;


    public LoadingStage(Viewport viewport, Batch batch, AssetManager assetManager) {
        super(viewport, batch);

        Table table = new Table();
        table.setFillParent(true);

        Texture turtleTexture = assetManager.get(TURTLE_URL, Texture.class);
        Image turtleImage = new Image(turtleTexture);

        table.add(turtleImage).padRight(TURTLE_PAD).padBottom(100);



        Texture backgroundTexture = assetManager.get(BG_URL, Texture.class);
        Image backgroundImage = new Image(backgroundTexture);

        table.background(backgroundImage.getDrawable());



        Skin skin = assetManager.get("skin/uiskin.json", Skin.class);



        table.row();
        mProgressBar = new ProgressBar();
        table.add(mProgressBar);


        addActor(table);
    }

    /**
     * Updates the viewed progressbar.
     * @param newProgress [0,1]
     */
    public void updateProgress(float newProgress) {
        mProgressBar.setProgress(newProgress * InfinityRun.WIDTH);
    }

    private static class ProgressBar extends Actor {
        private final ShapeRenderer shapeRenderer;
        private float progress = 0;

        public static final Color PROGRESS_COLOR = new Color(1,1,1,.6f);

        private static final int HEIGHT = 20;

        public ProgressBar() {

            shapeRenderer = new ShapeRenderer();
            shapeRenderer.setAutoShapeType(true);
        }

        protected void setProgress(float progress) {
            this.progress = progress;
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


            shapeRenderer.end();

            batch.begin();
        }

        public void dispose() {
            shapeRenderer.dispose();
        }
    }
}
