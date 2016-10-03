package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.WordImpl;



public class MissionStage extends Stage {

    private static final int TABLE_WIDTH = 200,
            TABLE_HEIGHT = 50,
            TABLE_OFFSET_TOP = 10,
            TABLE_OFFSET_LEFT = 10;
    private Skin mSkin;

    private Table mMissionTable;
    public MissionStage() {
        super( new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));


        mSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        mMissionTable = new Table(mSkin);
        mMissionTable.setSize(TABLE_WIDTH, TABLE_HEIGHT);
        mMissionTable.setPosition( TABLE_OFFSET_TOP, InfinityRun.HEIGHT - TABLE_HEIGHT - TABLE_OFFSET_TOP);


        // TODO get emojimodel from mission
        WordImpl emojiModel = new WordImpl("Äpple", "audio/apple.wav", "emoji/1f34e.png");
        changeEmoji(emojiModel);

        this.addActor(mMissionTable);

    }


    private void changeEmoji(WordImpl emojiModel) {

        mMissionTable.clearChildren();

        Image emojiImage = new Image(new Texture(Gdx.files.internal(emojiModel.getIconUrl())));
        emojiImage.setScaling(Scaling.fit);
        mMissionTable.add(emojiImage).height(40).width(40).align(Align.center);

        Label emojiLabel = new Label( emojiModel.getEmojiName(), mSkin);
        mMissionTable.add(emojiLabel).height(50).expandX().align(Align.left).padLeft(10);
    }
}
