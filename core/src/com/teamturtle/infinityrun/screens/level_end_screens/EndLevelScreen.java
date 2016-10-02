package com.teamturtle.infinityrun.screens.level_end_screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.screens.AbstractScreen;
import com.teamturtle.infinityrun.screens.IScreenObserver;

/**
 * Created by Henrik on 2016-10-02.
 */
public abstract class EndLevelScreen extends AbstractScreen {

    private static final float ROOT_TABLE_WIDTH = 600.0f, ROOT_TABLE_HEIGHT = 370.0f;
    private static final float ROOT_TABLE_POS_X = 100.0f, ROOT_TABLE_POS_Y = 50.0f;
    protected static final float BUTTON_PADDING = 5.0f;

    private Skin skin;
    private Stage stage;
    private Table rootTable;
    private Table buttonTable;

    private ImageButton levelsButton;
    private ImageButton retryButton;
    private Label topLabel;
    private String topLabelStr;

    private Texture bg;

    private IScreenObserver observer;

    public EndLevelScreen(SpriteBatch sb, IScreenObserver observer, String topLabelStr) {
        super(sb);
        this.observer = observer;
        this.topLabelStr = topLabelStr;

        skin = new Skin();

        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));

        initButtons();
        initTable();
    }

    @Override
    public void buildStage() {
        Gdx.input.setInputProcessor(stage);
        bg = new Texture("bg2.png");
    }

    private void initButtons() {
        levelsButton = new ImageButton(skin, "levels_button");
        levelsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    observer.changeScreen(InfinityRun.ScreenID.LEVELS_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        retryButton = new ImageButton(skin, "retry_button");
        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    observer.changeScreen(InfinityRun.ScreenID.GAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTable() {
        rootTable = new Table();
        rootTable.setSize(ROOT_TABLE_WIDTH, ROOT_TABLE_HEIGHT);
        rootTable.setPosition(ROOT_TABLE_POS_X, ROOT_TABLE_POS_Y);
        rootTable.center().top();
        topLabel = new Label(topLabelStr, skin);
        rootTable.add(topLabel);
        rootTable.row();
        buttonTable = new Table();
        buttonTable.add(levelsButton).pad(BUTTON_PADDING);
        buttonTable.add(retryButton).pad(BUTTON_PADDING);
        rootTable.add(buttonTable);
        stage.addActor(rootTable);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().end();
        stage.draw();
    }

    public Table getButtonTable() {
        return buttonTable;
    }

    public Skin getSkin() {
        return skin;
    }
}
