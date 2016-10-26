package com.teamturtle.infinityrun.screens.endscreens;

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
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.level.LevelDataHandler;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.sound.FxSound;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;
import com.teamturtle.infinityrun.sprites.emoji.EmojiFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Henrik on 2016-10-02.
 */
public class GameWonScreen extends AbstractEndScreen {

    private static class ImageClickListener extends ChangeListener {

        private final IScreenObserver observer;
        private final Level level;

        public ImageClickListener(IScreenObserver observer, Level level) {

            this.observer = observer;
            this.level = level;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            FxSound.NASTABANA.play();
            observer.playLevelAfterThis(level);
        }
    }

    private static final String LB_LEVEL_LOST = "Bana klarad";
    private static final String DISCOVERD_LAYER_URL = "ui/discovered.png";
    private static final int LB_PAD = 20;
    private static final int EMOJI_DIMENSION = 70;
    private static final int EMOJI_PAD = 3;
    private static final int EMOJIES_MAX_AMOUNT = 10;
    private static final int SHADOW_OFFSET = 2;
    private static final float SHADOW_SCALE = 1.05f;

    private final Skin skin;
    private Table emojiTable;
    private final Level level;
    private final IScreenObserver observer;
    private final List<Word> oldWords;
    private final List<Word> discoveredWords;
    //TODO should be replaced with assetmanager
    private List<Sound> emojiSounds;
    private Texture discoverdTopLayer;
    private Label emojiLbl;
    private List<Image> emojiShadows;

    public GameWonScreen(SpriteBatch sb, IScreenObserver observer, Level level
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
            super.getButtonTable().add(nextButton).pad(BUTTON_PAD);
        }
    }

    private void createEmojiesTable() {
        emojiTable = new Table();
        final List<Word> allWords = new ArrayList<Word>();
        emojiShadows = new ArrayList<Image>();
        allWords.addAll(discoveredWords);
        allWords.addAll(oldWords);
        for(int i = 0; i < allWords.size(); i++) {
            Stack stack = createEmojiStack(allWords.get(i), i);
            emojiTable.add(stack).size(EMOJI_DIMENSION).pad(EMOJI_PAD);
            if (i == EMOJIES_MAX_AMOUNT) return;
        }
        emojiTable.row();
        emojiLbl = new Label("", skin);
        emojiLbl.setColor(Color.BLACK.BLACK);
        emojiTable.add(emojiLbl).colspan(allWords.size());
    }

    private Stack createEmojiStack(Word word, int emojiIndex) {
        final Sound emojiSound = Gdx.audio.newSound(Gdx.files.internal(word.getSoundUrl()));
        emojiSounds.add(emojiSound);
        final Emoji emoji = EmojiFactory.getInstance().getEmoji(word);
        final Image emojiImg = new Image(emoji.getTexture());
        final Image emojiShadow = new Image(emoji.getTexture());
        emojiShadows.add(emojiShadow);
        emojiShadow.setColor(Color.BLACK);
        final Stack stack = new Stack();
        stack.add(emojiShadow);
        if (emojiIndex < discoveredWords.size()) {
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
                String emojiStr = emoji.getName();
                emojiStr = emojiStr.substring(0, 1).toUpperCase(Locale.getDefault()) + emojiStr.substring(1);
                emojiLbl.setText(emojiStr);
                for (Image img : emojiShadows) {
                    img.setScale(0);
                }
                emojiShadow.setScale(SHADOW_SCALE);
                emojiShadow.setPosition(emojiImg.getX()-SHADOW_OFFSET
                        , emojiImg.getY()-SHADOW_OFFSET);
            }
        });
        return stack;
    }

    private void updateScoreTable() {
        getScoreTable().row();
        getScoreTable().add(emojiTable).padTop(LB_PAD);
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
