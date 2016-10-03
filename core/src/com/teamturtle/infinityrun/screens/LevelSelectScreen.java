package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Henrik on 2016-10-03.
 */
public class LevelSelectScreen extends AbstractScreen{

    private static final int LEVEL_AMOUNT = 15;
    private static final float ROOT_TABLE_WIDTH = 600.0f, ROOT_TABLE_HEIGHT = 370.0f;
    private static final float ROOT_TABLE_POS_X = 100.0f, ROOT_TABLE_POS_Y = 50.0f;

    private Stage stage;
    private Skin skin;
    private Table rootTable;
    private TextButton levelButton;

    private Texture bg;
    private Texture uiBg;

    public LevelSelectScreen(SpriteBatch spriteBatch) {
        super(spriteBatch);
    }

    @Override
    public void buildStage() {
        bg = new Texture("bg2.png");
        uiBg = new Texture("ui/ui_bg_big.png");

        skin = new Skin();

        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        stage = new Stage();
        rootTable = new Table();
        rootTable.setPosition(ROOT_TABLE_POS_X, ROOT_TABLE_POS_Y);
        rootTable.setSize(ROOT_TABLE_WIDTH, ROOT_TABLE_HEIGHT);
        for(int i = 0; i < LEVEL_AMOUNT; i++) {
            if (i % 5 == 0) {
                rootTable.row();
            }else{
                TextButton button = new TextButton(i+ "", skin, "level_text_button");
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Gdx.app.log("button", "click");
                    }
                });
                rootTable.add(button);
            }
        }
        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().draw(uiBg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().end();
        stage.draw();
    }
}
