package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by Henrik on 2016-10-11.
 */
public class LoadingScreen extends AbstractScreen {

    private Image bg;
    private Image turtle;
    private Texture bgTexture;
    private Texture turtleTexture;
    private Stage stage;
    private IScreenObserver observer;

    public LoadingScreen(SpriteBatch sb, IScreenObserver observer) {
        super(sb);
        this.observer = observer;
    }



    @Override
    public void buildStage() {
        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        bgTexture = new Texture("new_background.png");
        turtleTexture = new Texture("ui/pause_button.png");
        turtle = new Image(turtleTexture);
        Table table = new Table();
        table.setFillParent(true);
        table.add(turtle);
        stage.addActor(table);
    }

    @Override
    public void draw() {
        super.draw();
        getSpriteBatch().draw(bgTexture, 0, 0, InfinityRun.WIDTH, InfinityRun.HEIGHT);
    }

    @Override
    public void dispose() {
        super.dispose();
        bgTexture.dispose();
        turtleTexture.dispose();
    }
}
