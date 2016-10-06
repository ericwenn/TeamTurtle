package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.sentences.Sentence;
import com.teamturtle.infinityrun.models.sentences.SentenceLoader;
import com.teamturtle.infinityrun.models.words.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rasmus on 2016-10-02.
 */

public class WordScreen extends AbstractScreen {

    private static final String FONT_URL = "fonts/Boogaloo-Regular.ttf", BACK_BUTTON_URL = "back_button";
    private static final int TITLE_SIZE = 50, DESCRIPTION_SIZE = 25, TABLE_WIDTH = 650,
    TABLE_HEIGHT = 400, TABLE_X = 75, TABLE_Y = 50;
    private static final Color FONT_COLOR = Color.WHITE, TABLE_COLOR = Color.GRAY;

    private Texture bg;
    private Stage stage;
    private BitmapFont titleFont, descriptionFont;
    private Table table;
    private Table descriptionTable;
    private Skin skin;
    private ImageButton returnButton;
    private TextButton soundButton;
    private Label titleLabel, descriptionLabel1, descriptionLabel2, descriptionLabel3;
    private Image image;
    private List<String> descriptionList;
    private Word word;
    private Sound sound;

    private IScreenObserver observer;

    public WordScreen(SpriteBatch sb, IScreenObserver observer, Word word){
        super(sb);
        this.observer = observer;

        this.bg = new Texture(PathConstants.BACKGROUND_PATH);
        this.stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.word = word;
        sound = Gdx.audio.newSound(Gdx.files.internal("audio/apple.wav"));

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

        SentenceLoader sl = new SentenceLoader();
        List<? extends Sentence> sentences = sl.getSentences(word);

        if (sentences != null) {
            for( Sentence s : sentences) {
                descriptionList.add(s.getText());
            }
        } else {
            Gdx.app.log("InfRun", "No words");
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

        titleLabel = new Label(word.getText().substring(0, 1).toUpperCase() +
                word.getText().substring(1), new Label.LabelStyle(titleFont, FONT_COLOR));
        descriptionLabel1 = new Label(descriptionList.get(0), new Label.LabelStyle(descriptionFont,
                FONT_COLOR));
        descriptionLabel2 = new Label(descriptionList.get(1), new Label.LabelStyle(descriptionFont,
                FONT_COLOR));
        descriptionLabel3 = new Label(descriptionList.get(2), new Label.LabelStyle(descriptionFont,
                FONT_COLOR));

        image = new Image(new Texture(word.getIconUrl()));

        soundButton = new TextButton("", skin, "level_text_button");
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
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

        table.add(titleLabel).left().padLeft(-80).padTop(25);
        table.add(soundButton).left().padTop(25).padLeft(30);
        table.row().padTop(10);
        table.add(image).left().padLeft(-100);
        table.add(descriptionTable).left().padLeft(40);
        table.row().padTop(25);
        table.add(returnButton).colspan(2).left().padLeft(-100);

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
