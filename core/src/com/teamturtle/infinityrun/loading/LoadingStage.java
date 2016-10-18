package com.teamturtle.infinityrun.loading;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by ericwenn on 10/18/16.
 */
public class LoadingStage extends Stage {

    private final AssetManager assetManager;
    public static final String BG_URL = "new_background.png";
    public static final String TURTLE_URL = "emoji/good/1f422.png";


    private static final int FRAME_ROWS = 1;
    private static final int FRAME_COLS = 20;
    private static final int TURTLE_PAD = 18;


    public LoadingStage(Viewport viewport, Batch batch, AssetManager assetManager) {
        super(viewport, batch);
        this.assetManager = assetManager;

        Table table = new Table();
        table.setFillParent(true);

        Texture turtleTexture = assetManager.get(TURTLE_URL, Texture.class);
        Image turtleImage = new Image(turtleTexture);

        table.add(turtleImage).padRight(TURTLE_PAD).padBottom(100);



        Texture backgroundTexture = assetManager.get(BG_URL, Texture.class);
        Image backgroundImage = new Image(backgroundTexture);

        table.background(backgroundImage.getDrawable());
        addActor(table);
    }


}
