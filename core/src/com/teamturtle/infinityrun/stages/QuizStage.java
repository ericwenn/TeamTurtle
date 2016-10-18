package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordLoader;
import com.teamturtle.infinityrun.sound.FxSound;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Text om klassen
 *
 * @author David Söderberg
 * @since 2016-10-02
 */

public class QuizStage extends Stage {
    private final Skin skin;
    private final IQuizStageListener handler;
    private final Emoji emoji;
    private final int wordCategory;
    private List<TextButton> guessButtons;
    private List<ImageButton> soundButtons;
    private List<Sound> soundList;
    private final WordLoader wordLoader;
    private final int score;
    private boolean isStarFilled = false;
    private boolean delayStarted = false;
    private boolean didGuessRight = false;
    private float stageTime = 0;

    //    Components
    private Table parentTable, buttonTable;
    private final Table starTable;
    private final Table animatedStarTable;
    private final Texture star;
    private final Texture noStar;

    private static final float TEXT_BUTTON_PADDING = 5.0f;
    private static final float PARENT_TABLE_WIDTH = 600.0f, PARENT_TABLE_HEIGHT = 370.0f;
    private static final float PARENT_TABLE_POS_X = 100.0f, PARENT_TABLE_POS_Y = 50.0f;
    private static final float ROW_PADDING = 20.0f;
    private static final int MAX_STARS = 3;

    private static final int QUESTION_EMOJI_DIMENSION = 80, STAR_DIMENSION = 70;

    public QuizStage(IQuizStageListener handler, List<? extends Word> collectedWords, int score) {
        super(new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        this.handler = handler;

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        wordLoader = new WordLoader();
        wordCategory = collectedWords.get(0).getCategory();
        this.score = score;
        star = new Texture("ui/star.png");
        noStar = new Texture("ui/no_star.png");

        starTable = getStarsTable();
        animatedStarTable = getAnimatedStarsTable();

        List<Word> guesses = getRandomGuesses(collectedWords);
        emoji = new Emoji(guesses.get(0));
        createButtons(guesses);
        createTableUi(starTable);
        addActor(parentTable);
        Gdx.input.setInputProcessor(this);
        FxSound.getLastPlayedSound().stop();
        FxSound.HARARENFRAGA.play();
    }

    private List<Word> getRandomGuesses(List<? extends Word> collectedWords) {
        Random random = new Random();
        List<Word> returnList = new ArrayList<Word>();
        while (returnList.size() < 3) {
            if (collectedWords.size() > 0) {
                int index = random.nextInt((collectedWords.size() - 1) + 1);
                if (!returnList.contains(collectedWords.get(index))) {
                    returnList.add(collectedWords.get(index));
                }
                collectedWords.remove(index);
            } else {
                List<Word> wordsFromCategory = wordLoader.getWordsFromCategory(wordCategory);
                int index = random.nextInt((wordsFromCategory.size() - 1) + 1);
                if (!returnList.contains(wordsFromCategory.get(index))) {
                    returnList.add(wordsFromCategory.get(index));
                }
            }
        }
        return returnList;
    }

    private void createButtons(final List<? extends Word> guesses) {
        guessButtons = new ArrayList<TextButton>();
        soundButtons = new ArrayList<ImageButton>();
        soundList = new ArrayList<Sound>();
        while (guesses.size() > 0) {
            final int index = random.nextInt((guesses.size() - 1) + 1);
            TextButton button = new TextButton(guesses.get(index).getText().substring(0, 1).toUpperCase(Locale.getDefault()) +
                    guesses.get(index).getText().substring(1), skin, "text_button_black");
            final Word word = guesses.get(index);
            button.pad(TEXT_BUTTON_PADDING);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    delayStarted = true;
                    didGuessRight = word.equals(emoji.getWordModel());
                    getActors().clear();
                    if (didGuessRight) {
                        FxSound.RIGHT_ANSWER.play(0.3f);
                        showRightWord(animatedStarTable, word);
                    } else {
                        FxSound.WRONG_ANSWER.play(0.3f);
                        showRightWord(starTable, word);
                    }
                    Timer timer = new Timer();
                    timer.scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            handler.onGuessClick(word.equals(emoji.getWordModel()));
                        }
                    }, 1.7f);

                }
            });
            guessButtons.add(button);

            guesses.remove(index);

            final int i = soundList.size();
            ImageButton soundButton = new ImageButton(skin, "sound_button");
            soundList.add(Gdx.audio.newSound(Gdx.files.internal(word.getSoundUrl())));
            soundButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!FxSound.isFxMuted()) {
                        soundList.get(i).play();
                    }
                }
            });
            soundButtons.add(soundButton);
        }
    }

    private void showRightWord(Table starTable, Word guessedWord) {
        parentTable = new Table();
        parentTable.setSize(PARENT_TABLE_WIDTH, PARENT_TABLE_HEIGHT);
        parentTable.setPosition(PARENT_TABLE_POS_X, PARENT_TABLE_POS_Y);

        parentTable.add(starTable).center().top();
        parentTable.row();
        addEmojiToTable();
        parentTable.row();
        addButtonsToTable(true, guessedWord);
        addActor(parentTable);
    }

    private void createTableUi(Table starTable) {
        parentTable = new Table();
        parentTable.setSize(PARENT_TABLE_WIDTH, PARENT_TABLE_HEIGHT);
        parentTable.setPosition(PARENT_TABLE_POS_X, PARENT_TABLE_POS_Y);

        parentTable.add(starTable).center().top();
        isStarFilled = false;
        parentTable.row();
        addEmojiToTable();
        parentTable.row();
        addButtonsToTable(false, null);
    }

    private void addEmojiToTable() {
        Table emojiTable = new Table();
        emojiTable.padBottom(ROW_PADDING * 2).padTop(ROW_PADDING * 2);
        Label fillLabel = new Label(" ", skin);

        emojiTable.add(fillLabel).width(25);
        Image emojiImage = new Image(emoji.getTexture());
        emojiImage.setScaling(Scaling.fit);
        emojiTable.add(emojiImage).width(QUESTION_EMOJI_DIMENSION).height(QUESTION_EMOJI_DIMENSION);
        Label questionLabel = new Label("?", skin, "text-large");
        questionLabel.setFontScale(1.4f);
        emojiTable.add(questionLabel).width(25).padLeft(ROW_PADDING / 4);

        parentTable.add(emojiTable);
    }

    private void addButtonsToTable(boolean revealAnswer, Word guessedWord) {
        buttonTable = new Table();
        for (TextButton button : guessButtons) {
            if (revealAnswer && guessedWord != null) {
                if (!didGuessRight && button.getText().toString().equalsIgnoreCase(guessedWord.getText())) {
                    String buttonText = button.getText().toString();
                    button = new TextButton(buttonText, skin, "text_button_red");
                } else if (button.getText().toString().equalsIgnoreCase(emoji.getWordModel().getText())) {
                    String buttonText = button.getText().toString();
                    button = new TextButton(buttonText, skin, "quiz_text_button");
                }
            }
            buttonTable.add(button).padRight(ROW_PADDING / 2).padLeft(ROW_PADDING / 2).width(170).height(60);
        }
        buttonTable.row();
        for (ImageButton button : soundButtons)
            buttonTable.add(button).padRight(ROW_PADDING / 2).padLeft(ROW_PADDING / 2).width(60).height(60);
        parentTable.add(buttonTable);

    }

    private Table getStarsTable() {
        Table starTable = new Table();
        for (int i = 0; i < score; i++) {
            starTable.add(new Image(star)).width(STAR_DIMENSION).height(STAR_DIMENSION);
        }
        for (int i = 0; i < MAX_STARS - score; i++) {
            starTable.add(new Image(noStar)).width(STAR_DIMENSION).height(STAR_DIMENSION);
        }
        return starTable;
    }

    private Table getAnimatedStarsTable() {
        Table starTable = new Table();
        for (int i = 0; i <= score; i++) {
            starTable.add(new Image(star)).width(STAR_DIMENSION).height(STAR_DIMENSION);
        }
        for (int i = 0; i < MAX_STARS - score - 1; i++) {
            starTable.add(new Image(noStar)).width(STAR_DIMENSION).height(STAR_DIMENSION);
        }
        return starTable;
    }

    @Override
    public void draw() {
        Gdx.input.setInputProcessor(this);
        super.draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stageTime += delta;
        if (!delayStarted) {
            if (stageTime > 0.5f) {
                getActors().clear();
                if (isStarFilled) {
                    createTableUi(starTable);
                    isStarFilled = false;
                } else {
                    createTableUi(animatedStarTable);
                    isStarFilled = true;
                }
                addActor(parentTable);
                stageTime = 0;
            }
        }
    }

    @Override
    public void dispose() {
        skin.dispose();
        star.dispose();
        noStar.dispose();
        super.dispose();
    }
}
