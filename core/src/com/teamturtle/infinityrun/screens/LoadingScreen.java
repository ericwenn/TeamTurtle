package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sound.FxSound;

/**
 * Created by Henrik on 2016-10-11.
 */
public class LoadingScreen extends AbstractScreen {

    private static final int FRAME_ROWS = 1;
    private static final int FRAME_COLS = 20;
    private static final int LOGO_PAD = 18;
    private static final int LOADING_SIZE = 90;
    private static final float ANIMATION_SPEED = 0.040f;
    private static final String LOADING_SHEET_URL = "ui/loading_sheet.png";
    private static final String BG_URL = "new_background.png";
    private static final String LOGO_URL = "logo.png";

    private Image bgImg;
    private Image logoImg;
    private Texture bgTexture;
    private Texture logoTexture;

    private Animation loadingAnimation;
    private Texture loadingSheet;
    private TextureRegion[] loadingFrames;
    private TextureRegion[][] tmpRegions;
    private float stateTime;

    private Stage stage;
    private final IScreenObserver observer;

    public LoadingScreen(SpriteBatch sb, IScreenObserver observer) {
        super(sb);
        this.observer = observer;
        startLoading();
    }

    @Override
    public void buildStage() {
        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        bgTexture = new Texture(BG_URL);
        bgImg = new Image(bgTexture);
        logoTexture = new Texture(LOGO_URL);
        logoImg = new Image(logoTexture);
        Table table = new Table();
        table.setFillParent(true);
        table.add(logoImg).padRight(LOGO_PAD).padBottom(70);
        table.background(bgImg.getDrawable());
        setUpLoadingAnimation();
        stage.addActor(table);
    }

    private void startLoading() {
        FxSound.load();
    }

    private void setUpLoadingAnimation() {
        loadingSheet = new Texture(LOADING_SHEET_URL);
        loadingFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        tmpRegions = TextureRegion.split(loadingSheet, LOADING_SIZE, LOADING_SIZE);
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                loadingFrames[index++] = tmpRegions[i][j];
            }
        }
        loadingAnimation = new Animation(ANIMATION_SPEED, new Array<TextureRegion>(loadingFrames)
                , Animation.PlayMode.LOOP);
        stateTime = 0;
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        if (FxSound.updateLoading()) {
            try {
                observer.changeScreen(InfinityRun.ScreenID.MAIN_MENU);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stage.draw();
        stateTime += Gdx.graphics.getDeltaTime();
        getSpriteBatch().begin();
        getSpriteBatch().draw(loadingAnimation.getKeyFrame(stateTime)
                , (InfinityRun.WIDTH / 2 - LOADING_SIZE / 2) / InfinityRun.PPM
                , (InfinityRun.HEIGHT/ 2 - LOADING_SIZE * 2) / InfinityRun.PPM
                , LOADING_SIZE / InfinityRun.PPM, LOADING_SIZE / InfinityRun.PPM);
        getSpriteBatch().end();
    }

    @Override
    public void dispose() {
        super.dispose();
        bgTexture.dispose();
        logoTexture.dispose();
        loadingSheet.dispose();
        for (TextureRegion region : loadingFrames) {
            region.getTexture().dispose();
        }
        for (TextureRegion regions[] : tmpRegions) {
            for (TextureRegion region : regions) {
                region.getTexture().dispose();
            }
        }
    }
}
