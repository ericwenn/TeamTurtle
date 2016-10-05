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
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordLoader;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private String rightGuess, guess2, guess3;
    private List<Word> collectedWords;
    private Word chosenWord;
    private int wordCategory;

    //    Components
    private TextButton guess1Button, guess2Button, guess3Button;
    private Table parentTable, buttonTable;
    private List<Word> guesses;
    private WordLoader wordLoader;

    private static final float TEXT_BUTTON_PADDING = 5.0f;
    private static final float PARENT_TABLE_WIDTH = 600.0f, PARENT_TABLE_HEIGHT = 370.0f;
    private static final float PARENT_TABLE_POS_X = 100.0f, PARENT_TABLE_POS_Y = 50.0f;
    private static final float ROW_PADDING = 20.0f;

    public QuizStage(IQuizStageListener handler, List<Word> collectedWords) {
        super(new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.handler = handler;
        this.collectedWords = collectedWords;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        quizLabel = new Label("Quiz-dags!", skin);
        wordLoader = new WordLoader();
        wordCategory = collectedWords.get(0).getCategory();

        guesses = getRandomGuesses(collectedWords);
        emoji = new Emoji(guesses.get(0));
        rightGuess = guesses.get(0).getText();
        guess2 = guesses.get(1).getText();
        guess3 = guesses.get(2).getText();
        createButtons();
        createTableUi();
        addActor(parentTable);
        Gdx.input.setInputProcessor(this);
    }

    private List<Word> getRandomGuesses(List<Word> collectedWords) {
        Random random = new Random();
        List<Word> returnList = new ArrayList<Word>();
        while (returnList.size() < 3) {
            if (collectedWords.size() > 0) {
                System.out.println("Adding a word from collectedWords");
                int index = random.nextInt((collectedWords.size() - 1) + 1);
                if (!returnList.contains(collectedWords.get(index))) {
                    returnList.add(collectedWords.get(index));
                }
                collectedWords.remove(index);
            }
            else {
                System.out.println("Adding a word from wordLoader");
                List<Word> wordsFromCategory = wordLoader.getWordsFromCategory(wordCategory);
                int index = random.nextInt((wordsFromCategory.size() - 1) + 1);
                if (!returnList.contains(wordsFromCategory.get(index))) {
                    returnList.add(wordsFromCategory.get(index));
                }
            }
        }
        return returnList;
    }

    private void createButtons() {
        guess1Button = new TextButton(rightGuess + "?", skin);
        guess1Button.pad(TEXT_BUTTON_PADDING);
        guess1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handler.onGuessClick(rightGuess.equals(emoji.getName()));
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
