package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

/**
 * Created by Rasmus on 2016-10-02.
 */

public class WordScreen extends AbstractScreen {

    private static final String FONT_URL = "fonts/Boogaloo-Regular.ttf", BACK_BUTTON_URL = "back_button";
    private static final int TITLE_SIZE = 50, DESCRIPTION_SIZE = 25, TABLE_WIDTH = 600,
    TABLE_HEIGHT = 400, TABLE_X = 100, TABLE_Y = 50;
    private static final Color FONT_COLOR = Color.BLACK;

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

    private IScreenObserver observer;

    public WordScreen(SpriteBatch sb, IScreenObserver observer, Emoji emoji){
        super(sb);

        this.observer = observer;

        this.bg = new Texture("bg2.png");
        this.emoji = emoji;
        this.stage = new Stage();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_URL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = TITLE_SIZE;
        parameter.color = Color.RED;
        titleFont = generator.generateFont(parameter);
        parameter.size = DESCRIPTION_SIZE;
        descriptionFont = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void buildStage() {
        table = new Table();
        table.setSize(TABLE_WIDTH, TABLE_HEIGHT);
        table.setPosition(TABLE_X, TABLE_Y);

        descriptionTable = new Table();

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        titleLabel = new Label(emoji.getName(), new Label.LabelStyle(titleFont, FONT_COLOR));
        descriptionLabel1 = new Label("Ett äpple", new Label.LabelStyle(descriptionFont,
                FONT_COLOR));
        descriptionLabel2 = new Label("Flera äpplen", new Label.LabelStyle(descriptionFont,
                FONT_COLOR));
        descriptionLabel3 = new Label("Äpple är en frukt", new Label.LabelStyle(descriptionFont,
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
                    observer.changeScreen(InfinityRun.ScreenID.MAIN_MENU);
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

        table.add(titleLabel).right().padRight(20).padTop(25);
        table.add(soundButton).left().padLeft(20).padTop(25);
        table.row().padTop(10);
        table.add(emojiImage);
        table.add(descriptionTable).left();
        table.row().padTop(25);
        table.add(returnButton).colspan(2);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
