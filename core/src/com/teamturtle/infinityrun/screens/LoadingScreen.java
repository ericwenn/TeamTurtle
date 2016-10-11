package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by Henrik on 2016-10-11.
 */
public class LoadingScreen extends AbstractScreen {

    private Image bgImg;
    private Image turtleImg;
    private Texture bgTexture;
    private Texture turtleTexture;
    private Stage stage;
    private IScreenObserver observer;
    private Label label;

    public LoadingScreen(SpriteBatch sb, IScreenObserver observer) {
        super(sb);
        this.observer = observer;
    }



    @Override
    public void buildStage() {
        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        bgTexture = new Texture("new_background.png");
        bgImg = new Image(bgTexture);
        turtleTexture = new Texture("emoji/good/1f422.png");
        turtleImg = new Image(turtleTexture);
        Table table = new Table();
        table.setFillParent(true);
        table.add(turtleImg);
        table.background(bgImg.getDrawable());
        stage.addActor(table);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bgTexture.dispose();
        turtleTexture.dispose();
    }
}
