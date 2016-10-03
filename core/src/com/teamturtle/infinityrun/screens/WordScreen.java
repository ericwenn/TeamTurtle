package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    private static final int X_OFFSET_SOUND_BUTTON = 220;
    private static final float IMAGE_SCALE = 2;
    private static final String FONT_URL = "fonts/Boogaloo-Regular.ttf";
    private static final int FONT_SIZE = 50;

    private Texture bg;
    private Emoji emoji;
    private Stage stage;
    private BitmapFont font;
    private float textLength;

    private IScreenObserver observer;

    public WordScreen(SpriteBatch sb, IScreenObserver observer, Emoji emoji){
        super(sb);

        this.observer = observer;

        this.bg = new Texture("bg2.png");
        this.emoji = emoji;
        font = new BitmapFont();

        this.stage = new Stage();


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_URL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        parameter.color = Color.RED;
        font = generator.generateFont(parameter);
        generator.dispose();

        GlyphLayout layout = new GlyphLayout(font, "Äpple");
        textLength = layout.width;
    }

    @Override
    public void show() {

    }

    @Override
    public void buildStage() {
        Gdx.input.setInputProcessor(stage);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("play_button.png")));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("play_button_pressed.png")));
        textButtonStyle.font = new BitmapFont();

        TextButton soundButton = new TextButton("", textButtonStyle);
        soundButton.setPosition(getViewport().getScreenWidth() / 2 + X_OFFSET_SOUND_BUTTON,
                (getHeight() / 2) * InfinityRun.PPM + emoji.getImage().getHeight(), Align.center);
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                emoji.playSound();
            }
        });

        TextButton returnButton = new TextButton("", textButtonStyle);
        returnButton.setPosition(0, 0);
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

        stage.addActor(soundButton);
        stage.addActor(returnButton);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        getSpriteBatch().begin();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        float x = (getWidth() / 2 - ((emoji.getImage().getWidth() * IMAGE_SCALE)) / InfinityRun.PPM);
        float y = (getHeight() / 2 - ((emoji.getImage().getHeight() * IMAGE_SCALE) - 150) / InfinityRun.PPM);

        getSpriteBatch().draw(emoji.getImage(), x, y, emoji.getImage().getWidth() * IMAGE_SCALE /
                InfinityRun.PPM, emoji.getImage().getHeight() * IMAGE_SCALE / InfinityRun.PPM);

        //A scaled projection is created to draw font
        Matrix4 scaledProjection = getSpriteBatch().getProjectionMatrix()
                .scale(1 / (InfinityRun.PPM), 1 / (InfinityRun.PPM), 0);
        getSpriteBatch().setProjectionMatrix(scaledProjection);

        font.draw(getSpriteBatch(), "Äpple", (getWidth() / 2) * InfinityRun.PPM,
                (getHeight() / 2) * InfinityRun.PPM + emoji.getImage().getHeight());

        //Reset the projection
        Matrix4 normalProjection = getSpriteBatch().getProjectionMatrix()
                .scale(InfinityRun.PPM, InfinityRun.PPM, 0);
        getSpriteBatch().setProjectionMatrix(normalProjection);
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
