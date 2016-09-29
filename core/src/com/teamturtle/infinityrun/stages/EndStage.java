package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teamturtle.infinityrun.InfinityRun;

import java.util.ArrayList;

/**
 * Created by ostmos on 2016-09-26.
 */
public class EndStage extends Stage {

    public enum EndStageType {
        LOST_LEVEL, COMPLETED_LEVEL;
    }

    private static final String LEVEL_LOST_SV = "Nivå förlorad :(";
    private static final String LEVEL_COMPLETED_SV = "Nivå klarad :D";
    private static final String POINTS_SV = "Poäng: ";
    private static final String WORD_COLLECTED_SV = "Ord samlade: ";
    private static final String MAIN_MENU_SV = "Huvudmeny";
    private static final String TRY_AGAIN_SV = "Försök igen";
    private static final String NEXT_SV = "Nästa";

    //Layout constants
    private static final float PARENT_TABLE_WIDTH = 600.0f, PARENT_TABLE_HEIGHT = 370.0f;
    private static final float PARENT_TABLE_POS_X = 100.0f, PARENT_TABLE_POS_Y = 50.0f;
    private static final float TEXT_BUTTON_PADDING = 5.0f;
    private static final float ROW_PADDING = 20.0f;
    private static final float EMOJI_TABLE_HEIGHT = 150.0f;
    private static final int EMOJIES_PER_ROW = 10;

    private Skin skin;
    private Table parentTable;
    private Label levelStatusLabel, pointsLabel, wordCollectedLabel;
    private TextButton mainMenuButton, rightButton;
    private ArrayList<String> emojiURLs;
    private IEndStageListener handler;
    private EndStageType type;

    public EndStage(final IEndStageListener handler, EndStageType type) {
        super(new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));

        this.handler = handler;
        this.type = type;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        //TODO get points from file
        int points = 501234;

        pointsLabel = new Label(POINTS_SV + points, skin);
        wordCollectedLabel = new Label(WORD_COLLECTED_SV, skin);

        createButtons();
        createTableUi();

        this.addActor(parentTable);
        Gdx.input.setInputProcessor(this);
    }

    private void createButtons(){
        mainMenuButton = new TextButton(MAIN_MENU_SV, skin);
        mainMenuButton.pad(TEXT_BUTTON_PADDING);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handler.onMainMenuButtonClick();
            }
        });

        if (type == EndStageType.COMPLETED_LEVEL) {
            levelStatusLabel = new Label(LEVEL_COMPLETED_SV, skin);
            rightButton = new TextButton(NEXT_SV, skin);
            rightButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    handler.onNextLevelButtonClick();
                }
            });
        }else if(type == EndStageType.LOST_LEVEL){
            levelStatusLabel = new Label(LEVEL_LOST_SV, skin);
            rightButton = new TextButton(TRY_AGAIN_SV, skin);
            rightButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    handler.onTryAgainButtonClick();
                }
            });
        }
        rightButton.pad(TEXT_BUTTON_PADDING);
    }

    private void createTableUi() {

        parentTable = new Table();
        parentTable.setSize(PARENT_TABLE_WIDTH, PARENT_TABLE_HEIGHT);
        parentTable.setPosition(PARENT_TABLE_POS_X, PARENT_TABLE_POS_Y);
        parentTable.center().top();
        addLabelsToTable();
        parentTable.row();
        addEmojiesToTable();
        parentTable.row();
        addButtonsToTable();
    }

    private void addLabelsToTable() {
        parentTable.add(levelStatusLabel).center().padBottom(ROW_PADDING);
        parentTable.row();
        parentTable.add(pointsLabel).left().padBottom(ROW_PADDING);
        parentTable.row();
        parentTable.add(wordCollectedLabel).left().padBottom(ROW_PADDING);
    }

    private void addEmojiesToTable() {
        Table emojiTable = new Table();
        emojiURLs = new ArrayList<String>();
        //TODO get emojis url from a file, this is temp
        for(int i = 0; i < 10; i++) {
            emojiURLs.add("emoji/1f40b.png");
            emojiURLs.add("emoji/1f43f.png");
            emojiURLs.add("emoji/1f48e.png");
        }
        int emojiCount = 0;
        for (String emojiURL : emojiURLs) {
            emojiCount++;
            if (emojiCount != 0 && emojiCount % EMOJIES_PER_ROW == 0) {
                emojiTable.row();
            }else{
                Image emojiImage = new Image(new Texture(Gdx.files.internal(emojiURL)));
                emojiImage.setScaling(Scaling.fit);
                emojiTable.add(emojiImage);
            }
        }
        parentTable.add(emojiTable).height(EMOJI_TABLE_HEIGHT);
    }

    private void addButtonsToTable() {
        Table buttonTable = new Table();
        buttonTable.add(mainMenuButton);
        buttonTable.add(rightButton).padLeft(PARENT_TABLE_WIDTH -
                (rightButton.getWidth() + mainMenuButton.getWidth()));
        buttonTable.padTop(ROW_PADDING);
        parentTable.add(buttonTable).bottom();
    }
}
