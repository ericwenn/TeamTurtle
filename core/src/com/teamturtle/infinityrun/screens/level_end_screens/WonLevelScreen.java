package com.teamturtle.infinityrun.screens.level_end_screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.level.LevelDataHandler;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.sound.FeedbackSound;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henrik on 2016-10-02.
 */
public class WonLevelScreen extends EndLevelScreen{

    private static final String LB_LEVEL_LOST = "Bana klarad";
    private static final String LB_WORDS_COLLECTED = "Ord sammlade: ";
    private static final String DISCOVERD_LAYER_URL = "ui/discovered.png";
    private static final int EMOJI_DIMENSION = 70;
    private static final int DICTIONARY_BTN_PADDING = 20;
    private static final int EMOJI_CELLS_PER_ROW = 7;

    private Skin skin;
    private Level level;
    private IScreenObserver observer;
    private List<Word> oldWords, discoveredWords;

    public WonLevelScreen(SpriteBatch sb, IScreenObserver observer, Level level
            , List<Word> oldWords, List<Word> discoveredWords, int score) {
        super(sb, observer, LB_LEVEL_LOST, level, score);
        this.level = level;
        this.observer = observer;
        this.oldWords = oldWords;
        this.discoveredWords = discoveredWords;
        skin = super.getSkin();
        addNextButtonToButtonTable();
        addEmojiesToScoreTable();
    }

    private void addNextButtonToButtonTable() {
        List<Level> levels = new LevelDataHandler().getLevels();
        if(levels.size() > level.getId()) {
            ImageButton nextButton = new ImageButton(skin, "next_button");
            nextButton.addListener(new ImageClickListener(observer, level));
            super.getButtonTable().add(nextButton).pad(BUTTON_PADDING);
        }
    }

    private void addEmojiesToScoreTable() {
        Table emojiTable = new Table();
        final List<Word> allWords = new ArrayList<Word>();
        allWords.addAll(discoveredWords);
        allWords.addAll(oldWords);
        for(int i = 0; i < allWords.size(); i++) {
            Emoji emoji = new Emoji(allWords.get(i));
            Image emojiImg = new Image(emoji.getTexture());
            Actor actor;
            if (i < discoveredWords.size()) {
                Stack stack = new Stack();
                stack.add(emojiImg);
                Image emojiTopLayer = new Image(new Texture(DISCOVERD_LAYER_URL));
                stack.add(emojiTopLayer);
                actor = stack;
            }else{
                actor = emojiImg;
            }
            actor.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("xd");
                }
            });
            if (i % EMOJI_CELLS_PER_ROW == 0) emojiTable.row();
            emojiTable.add(actor).size(EMOJI_DIMENSION);
        }

        getScoreTable().row();
        Label wordsCollectedLb = new Label(LB_WORDS_COLLECTED, skin);
        getScoreTable().add(wordsCollectedLb);
        getScoreTable().row();
        ImageButton dictionaryBtn = new ImageButton(skin, "dictionary_button");
        dictionaryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    FeedbackSound.ORDLISTA.play();
                    observer.changeScreen(InfinityRun.ScreenID.DICTIONARY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getScoreTable().add(emojiTable);
        getScoreTable().add(dictionaryBtn).padLeft(DICTIONARY_BTN_PADDING);
    }

    private static class ImageClickListener extends ChangeListener {

        private final IScreenObserver observer;
        private final Level level;

        public ImageClickListener(IScreenObserver observer, Level level) {

            this.observer = observer;
            this.level = level;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            observer.playLevelAfterThis(level);
        }
    }
}
