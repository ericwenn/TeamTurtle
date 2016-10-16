package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.sound.FeedbackSound;

/**
 * Created by Alfred on 2016-09-22.
 */

public class StartScreen extends AbstractScreen {

    private static final int BUTTON_OFFSET = -45;
    private static final int BUTTON_PADDING = 5;

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

        ImageButton playBtn = new ImageButton(skin, "play_button");
        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    FeedbackSound.SPELA.play();
                    observer.changeScreen(InfinityRun.ScreenID.LEVELS_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton dictionaryBtn = new ImageButton(skin, "dictionary_button");
        dictionaryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    FeedbackSound.ORDLISTA.play();
                    observer.changeScreen(InfinityRun.ScreenID.DICTIONARY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton musicBtn = new ImageButton(skin, "music_button");

        ImageButton fxBtn = new ImageButton(skin, "fx_button");

        Table rootTabel = new Table();
        rootTabel.setFillParent(true);
        Table soundTable = new Table();
        soundTable.add().width(InfinityRun.WIDTH - fxBtn.getWidth() - musicBtn.getWidth());
        System.out.println(rootTabel.getWidth());
        soundTable.add(musicBtn);
        soundTable.add(fxBtn);
        rootTabel.add(soundTable);
        rootTabel.row();
        Table btnTable = new Table();
        btnTable.add(playBtn).padTop(BUTTON_OFFSET).padBottom(BUTTON_PADDING);
        btnTable.row();
        btnTable.add(dictionaryBtn);
        rootTabel.add(btnTable).expandY().fillY();

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
