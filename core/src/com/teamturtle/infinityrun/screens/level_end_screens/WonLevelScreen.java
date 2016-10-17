package com.teamturtle.infinityrun.screens.level_end_screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.Timer;
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

    private static class ImageClickListener extends ChangeListener {

        private final IScreenObserver observer;
        private final Level level;

        public ImageClickListener(IScreenObserver observer, Level level) {

            this.observer = observer;
            this.level = level;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            FeedbackSound.NASTABANA.play();
            observer.playLevelAfterThis(level);
        }
    }

    private static final String LB_LEVEL_LOST = "Bana klarad";
    private static final String LB_WORDS_COLLECTED = "Ord samlade: ";
    private static final String DISCOVERD_LAYER_URL = "ui/discovered.png";
    private static final int LB_PAD = -20;
    private static final int EMOJI_DIMENSION = 70;
    private static final int EMOJI_CELLS_PER_ROW = 10;
    private static final int SHADOW_OFFSET = 2;
    private static final float SHADOW_SCALE = 1.05f;

    private Skin skin;
    private Table emojiTable;
    private Level level;
    private IScreenObserver observer;
    private List<Word> oldWords, discoveredWords;
    //TODO should be replaced with assetmanager
    private List<Sound> emojiSounds;
    private Texture discoverdTopLayer;
    private Label emojiLbl;
    private List<Image> emojiShadows;

    public WonLevelScreen(SpriteBatch sb, IScreenObserver observer, Level level
            , List<Word> oldWords, List<Word> discoveredWords, int score) {
        super(sb, observer, LB_LEVEL_LOST, level, score);
        this.level = level;
        this.observer = observer;
        this.oldWords = oldWords;
        this.discoveredWords = discoveredWords;
        skin = super.getSkin();

        discoverdTopLayer = new Texture(DISCOVERD_LAYER_URL);
        emojiSounds = new ArrayList<Sound>();

        createEmojiesTable();
        updateButtonTable();
        updateScoreTable();
    }

    private void updateButtonTable() {
        List<Level> levels = new LevelDataHandler().getLevels();
        if(levels.size() > level.getId()) {
            ImageButton nextButton = new ImageButton(skin, "next_button");
            nextButton.addListener(new ImageClickListener(observer, level));
            super.getButtonTable().add(nextButton).pad(BUTTON_PADDING);
        }
    }

    private void createEmojiesTable() {
        emojiTable = new Table();
        final List<Word> allWords = new ArrayList<Word>();
        emojiShadows = new ArrayList<Image>();
        allWords.addAll(discoveredWords);
        allWords.addAll(oldWords);
        for(int i = 0; i < allWords.size(); i++) {
            Word word = allWords.get(i);
            final Sound emojiSound = Gdx.audio.newSound(Gdx.files.internal(word.getSoundUrl()));
            emojiSounds.add(emojiSound);
            final Emoji emoji = new Emoji(word);
            final Image emojiImg = new Image(emoji.getTexture());
            final Image emojiShadow = new Image(emoji.getTexture());
            emojiShadows.add(emojiShadow);
            emojiShadow.setColor(Color.BLACK);
            final Stack stack = new Stack();
            stack.add(emojiShadow);
            if (i < discoveredWords.size()) {
                stack.add(emojiImg);
                Image emojiTopLayer = new Image(discoverdTopLayer);
                stack.add(emojiTopLayer);
            }else{
                stack.add(emojiImg);
            }
            stack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    emojiSound.play();
                    emojiLbl.setText(emoji.getName());
                    for (Image img : emojiShadows) {
                        img.setScale(0);
                    }
                    emojiShadow.setScale(SHADOW_SCALE);
                    emojiShadow.setPosition(emojiImg.getX()-SHADOW_OFFSET
                            , emojiImg.getY()-SHADOW_OFFSET);
                }
            });
            if (i % EMOJI_CELLS_PER_ROW == 0) emojiTable.row();
            emojiTable.add(stack).size(EMOJI_DIMENSION).pad(3f);
        }
        emojiTable.row();
        emojiLbl = new Label("", skin, "title");
        emojiTable.add(emojiLbl).colspan(allWords.size());
    }

    private void updateScoreTable() {
        getScoreTable().row();
        Label wordsCollectedLb = new Label(LB_WORDS_COLLECTED, skin, "title");
        getScoreTable().add(wordsCollectedLb).padTop(LB_PAD);
        getScoreTable().row();
        getScoreTable().add(emojiTable);
    }

    @Override
    public void dispose() {
        super.dispose();
        discoverdTopLayer.dispose();
        for (Sound sound : emojiSounds) {
            sound.dispose();
        }
    }
}
