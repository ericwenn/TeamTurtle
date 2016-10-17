package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.sound.FeedbackSound;
import com.teamturtle.infinityrun.storage.PlayerData;

import java.util.List;

/**
 * Created by Henrik on 2016-10-03.
 */
public class LevelSelectScreen extends AbstractScreen{

    private static final int STAR_AMOUNT = 3;
    private static final float ROOT_TABLE_WIDTH = 600.0f, ROOT_TABLE_HEIGHT = 370.0f;
    private static final float ROOT_TABLE_POS_X = 100.0f, ROOT_TABLE_POS_Y = 50.0f;
    private static final float STAR_PAD = -20f, RETURN_BUTTON_PAD = 10f;

    private Stage stage;
    private Skin skin;
    private Table rootTable;
    private ImageButton backButton;
    private Texture bg;


    private IScreenObserver observer;
    private final List<Level> levels;
    private PlayerData mPlayerData;

    public LevelSelectScreen(SpriteBatch spriteBatch, IScreenObserver observer, List<Level> levels, PlayerData playerData) {
        super(spriteBatch);
        this.observer = observer;
        this.levels = levels;
        this.mPlayerData = playerData;
    }

    @Override
    public void buildStage() {
        bg = new Texture(PathConstants.BACKGROUND_PATH);

        skin = new Skin();

        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        rootTable = new Table();
        rootTable.setPosition(ROOT_TABLE_POS_X, ROOT_TABLE_POS_Y);
        rootTable.setSize(ROOT_TABLE_WIDTH, ROOT_TABLE_HEIGHT);

        int i = 1;
        // If the user has gotten atleast 1 star on the previous level, they can play this one.
        boolean progressedThisFar = true;
        for(final Level level: levels) {
            int playerScoreOnLevel = mPlayerData.getPlayerProgressOnLevel(level);

            Table levelButtonTable = new Table();
            // TODO Change button style if the level is unplayable
            if( progressedThisFar ) {
                TextButton button = new TextButton(i+ "", skin, "level_text_button");
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        FeedbackSound.playLevelSound(level.getId());
                        observer.playLevel(level);
                    }
                });
                levelButtonTable.add(button);
            } else {
                ImageButton button = new ImageButton(skin, "lock_button");
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        FeedbackSound.EJUPPLAST.play();
                    }
                });
                levelButtonTable.add(button);
            }
            levelButtonTable.row();


            Table starTabel = new Table();
            for(int j = 0; j < STAR_AMOUNT; j++) {
                if(j < playerScoreOnLevel) {
                    starTabel.add(new Image(new Texture("ui/small_star.png")));
                }else{
                    starTabel.add(new Image(new Texture("ui/small_no_star.png")));
                }
            }
            levelButtonTable.add(starTabel).pad(STAR_PAD);
            rootTable.add(levelButtonTable).pad(RETURN_BUTTON_PAD);

            if (i % 5 == 0){
                rootTable.row();
            }
            progressedThisFar = progressedThisFar && playerScoreOnLevel > 0;
            i++;
        }
        backButton = new ImageButton(skin, "home_button");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    FeedbackSound.HEM.play();
                    observer.changeScreen(InfinityRun.ScreenID.MAIN_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        rootTable.row();
        rootTable.add(backButton).colspan(5).left().padTop(17);
        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        if (stage != null) {

            getSpriteBatch().begin();
            getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
            getSpriteBatch().end();
            stage.draw();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        bg.dispose();
    }

}
