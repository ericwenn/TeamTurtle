package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.sound.SoundPlayer;

/**
 * Created by Alfred on 2016-09-22.
 */

public class StartScreen extends AbstractScreen {

    private static final float ROOT_TABLE_WIDTH = 600.0f, ROOT_TABLE_HEIGHT = 370.0f;
    private static final float ROOT_TABLE_POS_X = 100.0f, ROOT_TABLE_POS_Y = 50.0f;
    protected static final float BUTTON_PADDING = 5.0f;

    private Texture bg;
    private Stage stage;

    private IScreenObserver observer;

    public StartScreen(SpriteBatch sb, IScreenObserver observer){
        super(sb);

        this.observer = observer;

        this.bg = new Texture(PathConstants.BACKGROUND_PATH);
        this.stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
    }

    @Override
    public void show() {

    }

    @Override
    public void buildStage() {
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();

        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        ImageButton playButton = new ImageButton(skin, "play_button");
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    SoundPlayer.playSound("spela", "feedback");
                    observer.changeScreen(InfinityRun.ScreenID.LEVELS_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ImageButton dictionaryButton = new ImageButton(skin, "dictionary_button");
        dictionaryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    SoundPlayer.playSound("ordlista", "feedback");
                    observer.changeScreen(InfinityRun.ScreenID.DICTIONARY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Table rootTabel = new Table().center();
        rootTabel.setPosition(ROOT_TABLE_POS_X, ROOT_TABLE_POS_Y);
        rootTabel.setSize(ROOT_TABLE_WIDTH, ROOT_TABLE_HEIGHT);
        rootTabel.add(playButton).pad(BUTTON_PADDING);
        rootTabel.row();
        rootTabel.add(dictionaryButton);

        stage.addActor(rootTabel);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        getSpriteBatch().begin();

        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());

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
