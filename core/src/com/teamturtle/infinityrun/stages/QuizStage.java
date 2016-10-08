package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
import com.teamturtle.infinityrun.sound.SoundPlayer;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

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
    private int wordCategory;
    private List<Word> guesses;
    private List<TextButton> guessButtons;
    private WordLoader wordLoader;

    //    Components
    private Table parentTable, buttonTable;

    private static final float TEXT_BUTTON_PADDING = 5.0f;
    private static final float PARENT_TABLE_WIDTH = 600.0f, PARENT_TABLE_HEIGHT = 370.0f;
    private static final float PARENT_TABLE_POS_X = 100.0f, PARENT_TABLE_POS_Y = 50.0f;
    private static final float ROW_PADDING = 20.0f;

    public QuizStage(IQuizStageListener handler, List<Word> collectedWords) {
        super(new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.handler = handler;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        quizLabel = new Label("Fråga!", skin);
        wordLoader = new WordLoader();
        wordCategory = collectedWords.get(0).getCategory();

        guesses = getRandomGuesses(collectedWords);
        emoji = new Emoji(guesses.get(0));
        createButtons(guesses);
        createTableUi();
        addActor(parentTable);
        Gdx.input.setInputProcessor(this);
        SoundPlayer.playSound("hararenfraga", "feedback");
    }

    private List<Word> getRandomGuesses(List<Word> collectedWords) {
        Random random = new Random();
        List<Word> returnList = new ArrayList<Word>();
        while (returnList.size() < 3) {
            if (collectedWords.size() > 0) {
                int index = random.nextInt((collectedWords.size() - 1) + 1);
                if (!returnList.contains(collectedWords.get(index))) {
                    returnList.add(collectedWords.get(index));
                }
                collectedWords.remove(index);
            }
            else {
                List<Word> wordsFromCategory = wordLoader.getWordsFromCategory(wordCategory);
                int index = random.nextInt((wordsFromCategory.size() - 1) + 1);
                if (!returnList.contains(wordsFromCategory.get(index))) {
                    returnList.add(wordsFromCategory.get(index));
                }
            }
        }
        return returnList;
    }

    private void createButtons(final List<Word> guesses) {
        guessButtons = new ArrayList<TextButton>();
        while (guesses.size() > 0) {
            int index = random.nextInt((guesses.size() - 1) + 1);

            TextButton button = new TextButton(guesses.get(index).getText(), skin, "text_button");
            final Word word = guesses.get(index);
            button.pad(TEXT_BUTTON_PADDING);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    handler.onGuessClick(word.equals(emoji.getWordModel()));
                }
            });
            guessButtons.add(button);

            guesses.remove(index);
        }
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
        for (TextButton button : guessButtons) {
            buttonTable.add(button).padRight(ROW_PADDING / 2).padLeft(ROW_PADDING / 2);
        }
        buttonTable.row();
        for(int i = 0; i < guessButtons.size(); i++) {
            ImageButton soundButton = new ImageButton(skin, "sound_button");
            final int index = i;
            soundButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //Sound sound = Gdx.audio.newSound(Gdx.files.internal(guesses.get(index).getSoundUrl()));
                    //sound.play();
                }
            });
            buttonTable.add(soundButton);
        }
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
