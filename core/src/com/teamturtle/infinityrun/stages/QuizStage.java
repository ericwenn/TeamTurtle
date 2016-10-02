package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
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
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.sprites.emoji.EmojiRandomizer;

/**
 * Text om klassen
 *
 * @author David Söderberg
 * @since 2016-10-02
 */

public class QuizStage extends Stage {
    private Skin skin;
    private IQuizStageListener handler;
    private Label quizLabel;
    private Emoji emoji;
    private String guess1, guess2, guess3;

    //    Components
    private TextButton guess1Button, guess2Button, guess3Button;
    private Table parentTable, buttonTable;

    private static final float TEXT_BUTTON_PADDING = 5.0f;
    private static final float PARENT_TABLE_WIDTH = 600.0f, PARENT_TABLE_HEIGHT = 370.0f;
    private static final float PARENT_TABLE_POS_X = 100.0f, PARENT_TABLE_POS_Y = 50.0f;
    private static final float ROW_PADDING = 20.0f;

    public QuizStage(IQuizStageListener handler) {
        super(new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.handler = handler;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        quizLabel = new Label("Quiz-dags!", skin);

//        Temporary strings and emoji
        guess1 = "Banan";
        guess2 = "Äpple";
        guess3 = "Päron";
        EmojiRandomizer emojiRandomizer = new EmojiRandomizer();
        emoji = emojiRandomizer.getNext();
        /*
        *   In near future guess-strings should be changed to emojis.
        *   Check if emoji.getName() == chosenGuess.getName()
        */

        createButtons();
        createTableUi();
        addActor(parentTable);
        Gdx.input.setInputProcessor(this);
    }

    private void createButtons() {
        guess1Button = new TextButton(guess1 + "?", skin);
        guess1Button.pad(TEXT_BUTTON_PADDING);
        guess1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handler.onGuessClick(guess1.equals(emoji.getName()));
            }
        });

        guess2Button = new TextButton(guess2 + "?", skin);
        guess2Button.pad(TEXT_BUTTON_PADDING);
        guess2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handler.onGuessClick(guess2.equals(emoji.getName()));
            }
        });

        guess3Button = new TextButton(guess3 + "?", skin);
        guess3Button.pad(TEXT_BUTTON_PADDING);
        guess3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handler.onGuessClick(guess3.equals(emoji.getName()));
            }
        });

    }

    private void createTableUi() {
        parentTable = new Table();
        parentTable.setSize(PARENT_TABLE_WIDTH, PARENT_TABLE_HEIGHT);
        parentTable.setPosition(PARENT_TABLE_POS_X, PARENT_TABLE_POS_Y);

        parentTable.center().top();
        parentTable.add(quizLabel).center().padBottom(ROW_PADDING);
        parentTable.row();
        addEmojiToTable();
        parentTable.row();
        addButtonsToTable();
    }

    private void addEmojiToTable() {
        Image emojiImage = new Image(emoji.getTexture());
        emojiImage.setScaling(Scaling.fit);
        parentTable.add(emojiImage);
    }

    private void addButtonsToTable() {
        buttonTable = new Table();
        buttonTable.add(guess1Button);
        buttonTable.add(guess2Button).padLeft(ROW_PADDING).padRight(ROW_PADDING);
        buttonTable.add(guess3Button);
        buttonTable.padTop(ROW_PADDING);
        parentTable.add(buttonTable).bottom();
    }

    @Override
    public void draw() {
        Gdx.input.setInputProcessor(this);
        super.draw();
    }

    public void hide() {
        buttonTable.remove();
        parentTable.remove();
    }
}
