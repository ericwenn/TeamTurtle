package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.internal.parser.JSONParser;

/**
 * Created by Rasmus on 2016-10-02.
 */

public class WordScreen extends AbstractScreen {

    private static final String FONT_URL = "fonts/Boogaloo-Regular.ttf", BACK_BUTTON_URL = "back_button";
    private static final int TITLE_SIZE = 50, DESCRIPTION_SIZE = 25, TABLE_WIDTH = 600,
    TABLE_HEIGHT = 400, TABLE_X = 100, TABLE_Y = 50;
    private static final Color FONT_COLOR = Color.WHITE, TABLE_COLOR = Color.GRAY;

    private Texture bg;
    private Emoji emoji;
    private Stage stage;
    private BitmapFont titleFont, descriptionFont;
    private Table table;
    private Table descriptionTable;
    private Skin skin;
    private ImageButton returnButton;
    private TextButton soundButton;
    private Label titleLabel, descriptionLabel1, descriptionLabel2, descriptionLabel3;
    private Image emojiImage;
    private List<String> descriptionList;

    private IScreenObserver observer;

    public WordScreen(SpriteBatch sb, IScreenObserver observer, Emoji emoji){
        super(sb);

        this.observer = observer;

        this.bg = new Texture(PathConstants.BACKGROUND_PATH);
        this.emoji = emoji;
        this.stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_URL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = TITLE_SIZE;
        parameter.color = FONT_COLOR;
        titleFont = generator.generateFont(parameter);
        parameter.size = DESCRIPTION_SIZE;
        descriptionFont = generator.generateFont(parameter);
        generator.dispose();

        descriptionList = new ArrayList<String>();

        FileHandle file = Gdx.files.internal("jsonfiles/json.json");

        JsonValue json = new JsonReader().parse(file);

        for(JsonValue item : json.get(emoji.getName())){
            descriptionList.add(item.toString().substring(14));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void buildStage() {
        table = new Table();
        table.setSize(TABLE_WIDTH, TABLE_HEIGHT);
        table.setPosition(TABLE_X, TABLE_Y);

        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGB565);
        map.setColor(TABLE_COLOR);
        map.fill();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(map))));

        descriptionTable = new Table();

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        titleLabel = new Label(emoji.getName(), new Label.LabelStyle(titleFont, FONT_COLOR));
        descriptionLabel1 = new Label(descriptionList.get(0), new Label.LabelStyle(descriptionFont,
                FONT_COLOR));
        descriptionLabel2 = new Label(descriptionList.get(1), new Label.LabelStyle(descriptionFont,
                FONT_COLOR));
        descriptionLabel3 = new Label(descriptionList.get(2), new Label.LabelStyle(descriptionFont,
                FONT_COLOR));

        emojiImage = new Image(emoji.getImage());

        soundButton = new TextButton("", skin, "level_text_button");
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                emoji.playSound();
            }
        });

        returnButton = new ImageButton(skin, BACK_BUTTON_URL);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    observer.changeScreen(InfinityRun.ScreenID.DICTIONARY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        descriptionTable = new Table();
        descriptionTable.add(descriptionLabel1).left();
        descriptionTable.row();
        descriptionTable.add(descriptionLabel2).left();
        descriptionTable.row();
        descriptionTable.add(descriptionLabel3).left();

        table.add(titleLabel).left().padLeft(-200).padTop(25);
        table.add(soundButton).left().padTop(25).padLeft(-50);
        table.row().padTop(10);
        table.add(emojiImage).left().padLeft(-200);
        table.add(descriptionTable).left().padLeft(-50);
        table.row().padTop(25);
        table.add(returnButton).colspan(2).left().padLeft(-200);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        getSpriteBatch().begin();

        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(),
                getViewport().getScreenHeight() / InfinityRun.PPM);

        getSpriteBatch().end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
