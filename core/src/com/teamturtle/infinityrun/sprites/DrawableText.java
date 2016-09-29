package com.teamturtle.infinityrun.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by rasmus on 2016-09-27.
 */

public class DrawableText implements Disposable {

    private static final int OFFSET_Y = 35;
    private static final Color color = Color.RED;

    private Stage stage;
    private Viewport viewport;
    private Label textLabel;

    public DrawableText(String text, SpriteBatch sb, float x, float y){
        viewport = new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT, new OrthographicCamera());
        //Creates a stage
        stage = new Stage(viewport, sb);
        textLabel = new Label(text, new Label.LabelStyle(new BitmapFont(), color));

        //The parameters x and y must be multiplied with PPM cause it isn´t scaled down as the rest
        //of the world.
        x *= InfinityRun.PPM;
        y *= InfinityRun.PPM;
        y += OFFSET_Y;
        //Help variable which adjust the text after the black bar in the beginning
        //Should be removed when the black bar is removed.
        x += 140;

        textLabel.setPosition((int)x, (int)y);
        //Adds the text to the stage
        stage.addActor(textLabel);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
    public Stage getStage(){
        return stage;
    }
}
