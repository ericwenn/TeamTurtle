package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.util.ArrayList;

/**
 * Created by Henrik on 2016-10-03.
 */
public class DictionaryScreen extends AbstractScreen {

    private Stage stage;
    private IScreenObserver observer;
    private ScrollPane wordList;
    private Skin skin;
    private Texture bg;
    private ImageButton imageButton;

    public DictionaryScreen(SpriteBatch spriteBatch, IScreenObserver observer) {
        super(spriteBatch);
        this.observer = observer;
        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
    }

    @Override
    public void buildStage() {
        bg = new Texture(PathConstants.BACKGROUND_PATH);

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

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

        ArrayList<Emoji> emojis = new ArrayList<Emoji>();
        for(int i = 0; i < 40; i++) {
            emojis.add(new Emoji("Äpple", "audio/apple.wav", "1f3d7"));
            emojis.add(new Emoji("xd", "audio/apple.wav", "1f4bb"));
            emojis.add(new Emoji("Clownfisk", "audio/apple.wav", "1f34e"));
        }

        Table grid = new Table();

        //TODO get all unlocked from json file and get all locked from json file
        for(int i = 0; i < emojis.size(); i++) {
            grid.add(createGridItem(emojis.get(i)));
            if ((i+1) % 5 == 0) {
                grid.row();
            }
        }

        ScrollPane scroller = new ScrollPane(grid, skin);
        scroller.setForceScroll(false, true);
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(scroller).fill().expand().pad(20f);
        rootTable.row();
        rootTable.add(imageButton).left().padLeft(20f).padBottom(20f);

        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
    }

    private Table createGridItem(Emoji emoji) {
        Table gridItem = new Table();
        gridItem.add(new Image(emoji.getTexture()));
        gridItem.row();
        gridItem.add(new Label(emoji.getName(), skin));
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
    public void render(float dt) {
        super.render(dt);
        stage.act(dt);
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().end();
        stage.draw();
    }
}
