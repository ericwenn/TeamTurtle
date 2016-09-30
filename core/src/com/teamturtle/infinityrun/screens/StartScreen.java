package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by Alfred on 2016-09-22.
 */

public class StartScreen extends AbstractScreen {

    private Texture bg;
    private Stage stage;

    private IScreenObserver observer;

    public StartScreen(SpriteBatch sb, IScreenObserver observer){
        super(sb);

        this.observer = observer;

        this.bg = new Texture("bg2.png");
        this.stage = new Stage();
    }

    @Override
    public void show() {

    }

    @Override
    public void buildStage() {
        Gdx.input.setInputProcessor(stage);

        // This can be used to render the font that is used in the button.
        /*BitmapFont font;
        String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzåäöABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Boogaloo-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.characters = FONT_CHARACTERS;
        font = generator.generateFont(param);*/

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("play_button.png")));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture("play_button_pressed.png")));
        //textButtonStyle.font = font;
        //temp font
        textButtonStyle.font = new BitmapFont();
        TextButton button = new TextButton("", textButtonStyle);
        button.setPosition(getViewport().getScreenWidth() / 2, getViewport().getScreenHeight() / 2, Align.center);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    observer.changeScreen(InfinityRun.ScreenID.GAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*button.addAction(new Action() {
            @Override
            public boolean act(float delta) {

                return true;
            }
        });*/

        stage.addActor(button);
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
