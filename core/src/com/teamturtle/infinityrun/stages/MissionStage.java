package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.EmojiModel;

/**
 * Created by ericwenn on 10/1/16.
 */
public class MissionStage extends Stage {

    private static final int TABLE_WIDTH = 200,
            TABLE_HEIGHT = 100;
    private Skin mSkin;

    private Table mMissionTable;
    public MissionStage() {
        super( new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));


        mSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        mMissionTable = new Table(mSkin);
        mMissionTable.setSize(TABLE_WIDTH, TABLE_HEIGHT);
        mMissionTable.setPosition( 0, InfinityRun.HEIGHT - TABLE_HEIGHT);


        // TODO get emojimodel from mission
        EmojiModel emojiModel = new EmojiModel("Äpple", "audio/apple.wav", "emoji/1f34e.png");
        insertEmoji(emojiModel);

        this.addActor(mMissionTable);
    }


    private void insertEmoji(EmojiModel emojiModel) {
        Image emojiImage = new Image(new Texture(Gdx.files.internal(emojiModel.getIconUrl())));
        emojiImage.setScaling(Scaling.fit);
        mMissionTable.add(emojiImage).height(50);

        Label emojiLabel = new Label( emojiModel.getEmojiName(), mSkin);
        mMissionTable.add(emojiLabel).height(50);
    }
}
