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
import com.teamturtle.infinityrun.models.level.LevelDataHandler;
import com.teamturtle.infinityrun.storage.PlayerData;

/**
 * Created by Henrik on 2016-10-03.
 */
public class LevelSelectScreen extends AbstractScreen{

    private static final int LEVEL_AMOUNT = 15, STAR_AMOUNT = 3;
    private static final float ROOT_TABLE_WIDTH = 600.0f, ROOT_TABLE_HEIGHT = 370.0f;
    private static final float ROOT_TABLE_POS_X = 100.0f, ROOT_TABLE_POS_Y = 50.0f;
    private static final float STAR_PAD = -20f, RETURN_BUTTON_PAD = 10f;

    private Stage stage;
    private Skin skin;
    private Table rootTable;
    private ImageButton backButton;
    private Texture bg;

    LevelDataHandler handler;

    private IScreenObserver observer;
    private PlayerData mPlayerData;

    public LevelSelectScreen(SpriteBatch spriteBatch, IScreenObserver observer, PlayerData playerData) {
        super(spriteBatch);
        this.observer = observer;
        this.mPlayerData = playerData;
        handler = new LevelDataHandler();
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


        for(int i = 1; i <= LEVEL_AMOUNT; i++) {
            final Level level = handler.getLevel(i);
            Table levelButtonTable = new Table();
            TextButton button = new TextButton(i+ "", skin, "level_text_button");
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        observer.playLevel(level);
                    }
                });
            levelButtonTable.add(button);
            levelButtonTable.row();
            Table starTabel = new Table();
            int playerScoreOnLevel = mPlayerData.getPlayerProgressOnLevel(level);
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
        }
        backButton = new ImageButton(skin, "back_button");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
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
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().end();
        stage.draw();
    }
}
