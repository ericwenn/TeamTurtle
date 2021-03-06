package com.teamturtle.infinityrun.screens.endscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.screens.AbstractScreen;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.sound.FxSound;

/**
 * Created by Henrik on 2016-10-02.
 */
public abstract class AbstractEndScreen extends AbstractScreen {

    private static final int ROOT_TABLE_WIDTH = 600, ROOT_TABLE_HEIGHT = 460;
    private static final int ROOT_TABLE_POS_X = 100, ROOT_TABLE_POS_Y = 20;
    private static final int STAR_BOT_PAD = 20;
    private static final int MAX_STARS = 3;
    private static final int STAR_DIMENSION = 70;
    private static final int TOP_LABEL_PAD = 40;
    protected static final int BUTTON_PAD = 5;

    private Skin skin;
    private Stage stage;
    private Table rootTable;
    private Table scoreTable;
    private Table buttonTable;

    private ImageButton levelsButton;
    private ImageButton retryButton;
    private Label topLabel;
    private String topLabelStr;

    private Texture bg, star, no_star;

    private IScreenObserver observer;

    private Level level;
    private int score;

    public AbstractEndScreen(SpriteBatch sb, IScreenObserver observer, String topLabelStr, Level level
            , int score) {
        super(sb);
        this.observer = observer;
        this.topLabelStr = topLabelStr;
        this.level = level;
        this.score = score;

        skin = new Skin();

        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));

        star = new Texture("ui/star.png");
        no_star = new Texture("ui/no_star.png");

        initButtons();
        buildRootTable();
    }

    @Override
    public void buildStage() {
        Gdx.input.setInputProcessor(stage);
        bg = new Texture(PathConstants.BACKGROUND_PATH);
    }

    private void initButtons() {
        levelsButton = new ImageButton(skin, "levels_button");
        levelsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    FxSound.getLastPlayedSound().stop();
                    FxSound.BANOR.play();
                    observer.changeScreen(InfinityRun.ScreenID.LEVELS_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        retryButton = new ImageButton(skin, "retry_button");
        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FxSound.getLastPlayedSound().stop();
                FxSound.FORSOKIGEN.play();
                observer.playLevel(level);
            }
        });
    }

    private void buildRootTable() {
        rootTable = new Table();
        //This is tmp
        if (score != 0) {
            rootTable.setSize(ROOT_TABLE_WIDTH, ROOT_TABLE_HEIGHT);
            rootTable.setPosition(ROOT_TABLE_POS_X, ROOT_TABLE_POS_Y);
        } else {
            rootTable.setSize(600, 300);
            rootTable.setPosition(100, 100);
        }
        rootTable.center().top();
        topLabel = new Label(topLabelStr, skin, "title");
        topLabel.setColor(Color.BLACK);
        rootTable.add(topLabel).padTop(TOP_LABEL_PAD);
        rootTable.row();
        buildScoreTable();
        rootTable.add(scoreTable);
        rootTable.row();

        buttonTable = new Table();
        buttonTable.add(levelsButton).pad(BUTTON_PAD);
        buttonTable.add(retryButton).pad(BUTTON_PAD);
        rootTable.add(buttonTable).expandY().bottom();
        stage.addActor(rootTable);
    }

    private void buildScoreTable() {
        scoreTable = new Table();
        Table starTable = new Table();
        Image starImage = new Image();
        starImage.setScaling(Scaling.fill);
        for (int i = 0; i < MAX_STARS; i++) {
            if (i < score) {
                starImage = new Image(star);
            } else {
                starImage = new Image(no_star);
            }
            starTable.add(starImage).size(STAR_DIMENSION);
        }
        scoreTable.add(starTable).center().padBottom(STAR_BOT_PAD).colspan(2);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().end();
        stage.draw();
    }

    protected Table getButtonTable() {
        return buttonTable;
    }

    protected Table getScoreTable() {
        return scoreTable;
    }

    public Skin getSkin() {
        return skin;
    }
}
