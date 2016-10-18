package com.teamturtle.infinityrun.stages.pause;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by Henrik on 2016-10-09.
 */
public class PauseButtonStage extends Stage {

    private static final String IMAGE_URL = "ui/pause_button.png";
    private Texture btnTexture;

    public PauseButtonStage() {
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        setUpButton();
    }

    private void setUpButton() {
        btnTexture = new Texture(IMAGE_URL);
        Image pauseButton = new Image(btnTexture);
        Table table = new Table();
        table.setSize((InfinityRun.WIDTH * 2) - pauseButton.getWidth()
                , (InfinityRun.HEIGHT * 2) - pauseButton.getHeight());
        table.add(pauseButton);
        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
        btnTexture.dispose();
    }

}
