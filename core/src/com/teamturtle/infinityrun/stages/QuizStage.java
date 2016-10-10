package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
    private Skin skin;
    private IQuizStageListener handler;
    private Emoji emoji;
    private int wordCategory;
    private List<Word> guesses;
    private List<TextButton> guessButtons;
    private List<ImageButton> soundButtons;
    private List<Sound> soundList;
    private WordLoader wordLoader;
    private int score;
    private boolean isStarFilled = false;
    private float stageTime = 0;

    //    Components
    private Table parentTable, buttonTable;
    private Table starTable, animatedStarTable;
    private Texture star, noStar;

    private static final float TEXT_BUTTON_PADDING = 5.0f;
    private static final float PARENT_TABLE_WIDTH = 600.0f, PARENT_TABLE_HEIGHT = 370.0f;
    private static final float PARENT_TABLE_POS_X = 100.0f, PARENT_TABLE_POS_Y = 50.0f;
    private static final float ROW_PADDING = 20.0f;
    private static final int MAX_STARS = 3;

    public QuizStage(IQuizStageListener handler, List<Word> collectedWords, int score) {
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

        guesses = getRandomGuesses(collectedWords);
        emoji = new Emoji(guesses.get(0));
        createButtons(guesses);
        createTableUi(starTable);
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
        soundList = new ArrayList<Sound>();
        while (guesses.size() > 0) {
            final int index = random.nextInt((guesses.size() - 1) + 1);

            TextButton button = new TextButton(guesses.get(index).getText().substring(0,1).toUpperCase(Locale.getDefault()) +
                    guesses.get(index).getText().substring(1), skin, "text_button");
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

            final int i = soundList.size();
            ImageButton soundButton = new ImageButton(skin, "sound_button");
            soundList.add(Gdx.audio.newSound(Gdx.files.internal(word.getSoundUrl())));
            soundButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y){
                    soundList.get(i).play();
                }
            });
            soundButtons.add(soundButton);
        }
    }

    private void createTableUi(Table starTable) {
        parentTable = new Table();
        parentTable.setSize(PARENT_TABLE_WIDTH, PARENT_TABLE_HEIGHT);
        parentTable.setPosition(PARENT_TABLE_POS_X, PARENT_TABLE_POS_Y);

//        parentTable.center().top();
        parentTable.add(starTable).center().top();
        isStarFilled = false;
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
        for(ImageButton button : soundButtons)
            buttonTable.add(button).padRight(ROW_PADDING / 2).padLeft(ROW_PADDING / 2);
        parentTable.add(buttonTable);

    }

    private Table getStarsTable() {
        Table starTable = new Table();
        for(int i = 0; i < score; i++) {
            starTable.add(new Image(star));
        }
        for(int i = 0; i < MAX_STARS - score; i++) {
            starTable.add(new Image(noStar));
        }
        return starTable;
    }

    private Table getAnimatedStarsTable() {
        Table starTable = new Table();
        for(int i = 0; i <= score; i++) {
            starTable.add(new Image(star));
        }
        for(int i = 0; i < MAX_STARS - score - 1; i++) {
            starTable.add(new Image(noStar));
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
        if (stageTime > 0.5f) {
            if (isStarFilled) {
                getActors().clear();
                createTableUi(starTable);
                isStarFilled = false;
            }
            else {
                getActors().clear();
                createTableUi(animatedStarTable);
                isStarFilled = true;
            }
            addActor(parentTable);
            stageTime = 0;
        }
    }

    public void hide() {
        buttonTable.remove();
        parentTable.remove();
    }
}
