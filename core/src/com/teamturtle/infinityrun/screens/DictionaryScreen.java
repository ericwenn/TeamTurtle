package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
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
        bg = new Texture("bg2.png");

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        imageButton = new ImageButton(skin, "back_button");

        Table listItems = new Table();

        ArrayList<Emoji> emojis = new ArrayList<Emoji>();
        for(int i = 0; i < 30; i++) {
            emojis.add(new Emoji("Äpple", "audio/apple.wav", new Texture("emoji/1f34e.png")));
        }

        //TODO get from json file
        for (Emoji emoji : emojis) {
            Table listItem = new Table();
            listItem.setTouchable(Touchable.enabled);
            Image image = new Image(emoji.getTexture());
            listItem.add(image).expandX().left().padLeft(50f);
            listItem.add(new Label(emoji.getName(), skin)).right().padRight(50f);
            listItem.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO changeScreen(id, emoji)
                }
            });
            listItems.add(listItem).expandX().fill();
            listItems.row();
        }

        ScrollPane scroller = new ScrollPane(listItems, skin);
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(scroller).fill().expand().pad(20f);
        rootTable.row();
        rootTable.add(imageButton).left().padLeft(20f).padBottom(20f);

        stage.addActor(rootTable);
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
