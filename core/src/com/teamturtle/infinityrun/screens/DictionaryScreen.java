package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordImpl;
import com.teamturtle.infinityrun.models.words.WordLoader;
import com.teamturtle.infinityrun.sound.SoundPlayer;
import com.teamturtle.infinityrun.sprites.Player;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.storage.PlayerData;

import java.util.ArrayList;
import java.util.List;

public class DictionaryScreen extends AbstractScreen {

    private static final String LABEL_BG_URL = "label_bg.png";
    private static final String LABEL_TEXT = "Ord hittade:";
    private static final String LABEL_UNKNOWN = "???";
    private static final float PAD_ROW = 20f;
    private static final float PAD_SCROLL = 5f;
    private static final float GRID_COLUMN_WIDTH = 4;

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

        initBackButton();
        initGridTable();

    }

    private void initBackButton() {
        imageButton = new ImageButton(skin, "back_button");
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    SoundPlayer.playSound("tillbaka", "feedback");
                    observer.changeScreen(InfinityRun.ScreenID.MAIN_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        imageButton.pad(10f);
    }


    private void initGridTable() {
        WordLoader wordLoader = new WordLoader();
        PlayerData playerData = new PlayerData();

        Table grid = new Table();

        int collectedWordsAmount = 0;
        List<WordImpl> allWords = wordLoader.getAllWords();
        for(int i = 0; i < allWords.size(); i++){
            Word word = allWords.get(i);
            if (playerData.hasPlayerCollectedWord(word)) {
                grid.add(createGridItem(word, false));
                collectedWordsAmount++;
            }else{
                grid.add(createGridItem(word, true));
            }
            if ((i+1) % GRID_COLUMN_WIDTH == 0) {
                grid.row().padTop(PAD_ROW);
            }
        }

        ScrollPane scroller = new ScrollPane(grid);
        scroller.setForceScroll(false, true);
        Table rootTable = new Table();
        rootTable.setFillParent(true);

        Table leftPanel = new Table();
        leftPanel.add(imageButton);
        leftPanel.row();
        Table labelBg = new Table();
        labelBg.add(new Label(
                 LABEL_TEXT + "\n" + collectedWordsAmount + " / " + allWords.size(), skin));
        labelBg.setBackground(
                new TextureRegionDrawable(new TextureRegion(new Texture(LABEL_BG_URL))));
        leftPanel.add(labelBg);
        rootTable.add(leftPanel).left().top();
        rootTable.add(scroller).fill().expand().pad(PAD_SCROLL);
        stage.addActor(rootTable);
    }

    private Table createGridItem(final Word word, boolean tinted) {
        final Table gridItem = new Table();
        Image image = new Image(new Texture(word.getIconUrl()));
        Label label;
        if (tinted) {
            image.setColor(Color.BLACK);
            label = new Label(LABEL_UNKNOWN, skin);
        }else{
            label = new Label(word.getText(), skin);
        }
        gridItem.add(image);
        gridItem.row();
        gridItem.add(label);
        gridItem.setTouchable(Touchable.enabled);
        gridItem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    observer.changeScreen(word);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
