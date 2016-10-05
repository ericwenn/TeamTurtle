package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordLoader;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.storage.PlayerData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henrik on 2016-10-03.
 */
public class DictionaryScreen extends AbstractScreen {

    private Stage stage;
    private IScreenObserver observer;
    private Skin skin;
    private Texture bg;
    private ImageButton imageButton;

    public DictionaryScreen(final SpriteBatch spriteBatch, IScreenObserver observer) {
        super(spriteBatch);
        this.observer = observer;
        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));

        bg = new Texture(PathConstants.BACKGROUND_PATH);

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        createBackButton();
        createGridTable();

    }

    private void createBackButton() {
        imageButton = new ImageButton(skin, "back_button");
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    observer.changeScreen(InfinityRun.ScreenID.MAIN_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createGridTable() {
        WordLoader wordLoader = new WordLoader();
        PlayerData playerData = new PlayerData();

        Table grid = new Table();


        /*for(int i = 0; i < emojis.size(); i++) {
            grid.add(createGridItem(emojis.get(i)));
            if ((i+1) % 4 == 0) {
                grid.row();
            }
        }*/

        ScrollPane scroller = new ScrollPane(grid, skin);
        scroller.setForceScroll(false, true);
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(imageButton).left().top();
        rootTable.add(scroller).fill().expand().pad(5f);
        stage.addActor(rootTable);
    }

    private Table createGridItem(Word word) {
        Table gridItem = new Table();
        System.out.println(word.getIconUrl());
        gridItem.add(new Image(new Texture(word.getIconUrl())));
        gridItem.row();
        gridItem.add(new Label(word.getText(), skin));
        gridItem.setTouchable(Touchable.enabled);
        gridItem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO changeScreen(id, emoji)
            }
        });
        return gridItem;
    }

    @Override
    public void buildStage() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        stage.act(dt);
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().end();
        stage.draw();
    }
}
