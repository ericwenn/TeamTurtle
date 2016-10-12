package com.teamturtle.infinityrun.stages.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.sound.FeedbackSound;

/**
 * Created by ostmos on 2016-10-08.
 */
public class PauseStage extends Stage{

    private enum State {
        READY(0), GO(1), RUN_GAME(2);

        private int index;

        State(int index) {
            this.index = index;
        }
    }

    private static final int ROW_PAD = 10;
    private static final int LBL_PAD = 250;
    private static final int REPEAT_COUNT = 2;
    private static final int TIMER_DELAY = 1;
    private static final String READY_STR = "Redo?";
    private static final String GO_STR = "Kör!";
    private static final String SKIN_URL_JSON = "skin/uiskin.json";
    private static final String SKIN_URL_ATLAS = "skin/uiskin.atlas";

    private Skin skin;
    private ImageButton continueBtn;
    private ImageButton retryBtn;
    private ImageButton levelsBtn;
    private Label countDownLbl;

    private IPauseStageHandler handler;
    private IScreenObserver observer;
    private Level level;

    private TextureAtlas textureAtlas;

    private int sequenceState = 0;

    public PauseStage(IPauseStageHandler handler, IScreenObserver observer, Level level) {
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.handler = handler;
        this.observer = observer;
        this.level = level;

        setUpStage();
    }

    private void setUpStage() {
        skin = new Skin();
        textureAtlas = new TextureAtlas(Gdx.files.internal(SKIN_URL_ATLAS));
        skin.addRegions(textureAtlas);
        skin.load(Gdx.files.internal(SKIN_URL_JSON));
        countDownLbl = new Label("", skin, "text-large");
        setUpButtons();
        Table table = new Table();
        table.setFillParent(true);
        table.add(continueBtn).padBottom(ROW_PAD);
        table.row();
        table.add(retryBtn).padBottom(ROW_PAD);
        table.row();
        table.add(levelsBtn);
        this.addActor(table);
        Gdx.input.setInputProcessor(this);
    }

    private void setUpButtons() {
        continueBtn = new ImageButton(skin, "continue_button");
        continueBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                removeActors();
                runCountDownSeq();
            }
        });
        retryBtn = new ImageButton(skin, "retry_button");
        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                observer.playLevel(level);
            }
        });
        levelsBtn = new ImageButton(skin, "levels_button");
        levelsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    observer.changeScreen(InfinityRun.ScreenID.LEVELS_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void removeActors() {
        for (Actor actor : getActors()) {
            actor.remove();
        }
    }

    private void runCountDownSeq() {
        Table table = new Table();
        table.setFillParent(true);
        table.add(countDownLbl).padBottom(LBL_PAD);
        this.addActor(table);
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (sequenceState == State.READY.index) {
                    countDownLbl.setText(READY_STR);
                } else if (sequenceState == State.GO.index) {
                    FeedbackSound.KOR.play();
                    countDownLbl.setText(GO_STR);
                } else if (sequenceState == State.RUN_GAME.index) {
                    handler.continueBtnClick();
                }
                sequenceState++;
            }
        }, 0, TIMER_DELAY, REPEAT_COUNT);
    }

    @Override
    public void dispose() {
        super.dispose();
        textureAtlas.dispose();
    }

}
