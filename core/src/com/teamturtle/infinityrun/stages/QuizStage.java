package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordLoader;
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
    private List<ImageButton> soundButtons;
    private WordLoader wordLoader;

    //    Components
    private Table parentTable, buttonTable, soundButtonTable;

    private static final float TEXT_BUTTON_PADDING = 5.0f;
    private static final float PARENT_TABLE_WIDTH = 600.0f, PARENT_TABLE_HEIGHT = 370.0f;
    private static final float PARENT_TABLE_POS_X = 100.0f, PARENT_TABLE_POS_Y = 50.0f;
    private static final float ROW_PADDING = 20.0f;

    public QuizStage(IQuizStageListener handler, List<Word> collectedWords) {
        super(new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.handler = handler;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        quizLabel = new Label("Quiz-dags!", skin);
        wordLoader = new WordLoader();
        wordCategory = collectedWords.get(0).getCategory();

        guesses = getRandomGuesses(collectedWords);
        emoji = new Emoji(guesses.get(0));
        createButtons(guesses);
        createTableUi();
        addActor(parentTable);
        Gdx.input.setInputProcessor(this);
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
        soundButtons = new ArrayList<ImageButton>();
        while (guesses.size() > 0) {
            int index = random.nextInt((guesses.size() - 1) + 1);

            TextButton button = new TextButton(guesses.get(index).getText() + "?", skin);
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

            ImageButton soundButton = new ImageButton(skin, "listen_button");
            soundButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y){
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/apple.wav"));
                    sound.play();
                }
            });
            soundButtons.add(soundButton);
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
        parentTable.row();
        addSoundButtonToTable();
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
        buttonTable.padTop(ROW_PADDING);
        parentTable.add(buttonTable).bottom();
    }

    public void addSoundButtonToTable(){
        soundButtonTable = new Table();
        for(ImageButton button : soundButtons){
            soundButtonTable.add(button).padRight(ROW_PADDING / 2).padLeft(ROW_PADDING / 2);
        }
        soundButtonTable.padTop(ROW_PADDING);
        parentTable.add(soundButtonTable).bottom();
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
