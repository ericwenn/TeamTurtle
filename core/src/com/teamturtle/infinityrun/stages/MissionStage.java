package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.Mission;
import com.teamturtle.infinityrun.models.words.Word;

import static com.badlogic.gdx.utils.Timer.schedule;


public class MissionStage extends Stage {

    private static final int TABLE_WIDTH = 200,
            TABLE_HEIGHT = 50,
            TABLE_OFFSET_BOTTOM = 20,
            TABLE_OFFSET_RIGHT = 10;
    private static final String CATCH_PREFIX = "Plocka ";

    private static final float SCALE_BY = 1.2f;
    private static final int SCALE_STEP_COUNT = 100;
    private static final float SCALE_STEP_SECONDS = .01f;
    private final Skin mSkin;

    private final Table mMissionTable;
    private Label emojiLabel;

    private final Timer.Task disappearTask;
    private final int[] index;

    public MissionStage() {
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));


        mSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        mMissionTable = new Table(mSkin);
        mMissionTable.setSize(TABLE_WIDTH, TABLE_HEIGHT);
        mMissionTable.setPosition(InfinityRun.WIDTH / 2 - TABLE_WIDTH / 2, TABLE_OFFSET_BOTTOM);
        mMissionTable.setTransform(true);
        mMissionTable.setOrigin(Align.bottom);

        index = new int[]{0};
        disappearTask = new Timer.Task() {
            @Override
            public void run() {
                mMissionTable.setScale(scaleDownToZero(index[0]++, mMissionTable.getScaleX()));
            }
        };

        this.addActor(mMissionTable.align(Align.center));

    }


    private void changeEmoji(Word emojiModel) {
        mMissionTable.clearChildren();

        Table table = new Table(mSkin);
        emojiLabel = new Label(CATCH_PREFIX + emojiModel.getArticle() + " " + emojiModel.getText(), mSkin);
        Image emojiImage = new Image(new Texture(Gdx.files.internal(emojiModel.getIconUrl())));
        emojiImage.setScaling(Scaling.fit);
        table.add(emojiLabel).height(50).expandX().padRight(10);
        table.add(emojiImage).height(40).width(40);

        mMissionTable.add(table).align(Align.center);

    }


    public void setMission(Mission mission) {
        this.index[0] = 0;
        disappearTask.cancel();
        Word correctWord = mission.getCorrectWord();
        if (correctWord != null) {
            changeEmoji(correctWord);
        }
        final int[] index = {0};
        schedule(new Timer.Task() {
            @Override
            public void run() {
                mMissionTable.setScale(scaleFn(index[0]++, SCALE_STEP_COUNT));
            }
        }, 0, SCALE_STEP_SECONDS, SCALE_STEP_COUNT);
    }

    public void onEmojiCollision(Color color) {
        emojiLabel.setColor(color);
        if (!disappearTask.isScheduled()) {
            Timer.schedule(disappearTask, 0, SCALE_STEP_SECONDS, SCALE_STEP_COUNT * 2);
        }
    }

    private float scaleDownToZero(int index, float currentScale) {
        if (index < 100) {
            return currentScale;
        }
        return currentScale * 0.9f;
    }

    private float scaleFn(int index, int steps) {
        float r;
        if (index < steps / 2) {
            r = 1 + (SCALE_BY - 1) * index * 2 / (float) steps;
        } else {
            r = SCALE_BY - (SCALE_BY - 1) * index / (float) steps;
        }
        return r;
    }

    @Override
    public void dispose() {
        super.dispose();
        mSkin.dispose();
    }
}
