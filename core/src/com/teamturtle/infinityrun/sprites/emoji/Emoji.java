package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Timer;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordImpl;
import com.teamturtle.infinityrun.models.words.WordLoader;
import com.teamturtle.infinityrun.sound.FxSound;
import com.teamturtle.infinityrun.sprites.AbstractEntity;

import java.util.Map;

/**
 * Created by Henrik on 2016-09-21.
 */
public class Emoji extends AbstractEntity {

    private static final float EMOJI_SIZE = 32;
    private static final int FONT_SIZE = 25, COUNTER_MAX = 120, OFFSET_TEXT = 30, X_OFFSET = 140,
        FADING_Y_POS = 300;
    private static final String FONT_URL = "fonts/Boogaloo-Regular.ttf";
    private static final Color FONT_COLOR = Color.BLACK;


    private Word wordModel;
    private Texture texture;
    private Sound emojiSound;

    private boolean isExploded = false;
    private Body mBody;
    private boolean hasSound;

    private BitmapFont font;
    private float textLength;
    private Stage fontStage;
    private int counter;
    private float distanceToTop;

    @Deprecated
    public Emoji(String emojiName, String soundURL, Texture texture) {
        this.texture = texture;
        setup();
    }

    public Emoji(String emojiName, String soundUrl, String iconUrl) {
        WordImpl w = new WordImpl();
        w.word = emojiName;
        w.filename = iconUrl;
        w.soundUrl = soundUrl;
        WordLoader wl = new WordLoader();
        Map<String, ? extends Word> words = wl.getWords();

        for(Word word : words.values()){
            if(word.getText().equals(w.getText()))
                w.id = word.getId() + "";
        }

        wordModel = w;

        setup();
    }

    public Emoji(Word word) {
        wordModel = word;
        setup();
    }

    private void setup() {
        texture = new Texture(wordModel.getIconUrl());
        //Create font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_URL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        parameter.color = FONT_COLOR;
        font = generator.generateFont(parameter);
        generator.dispose();

        GlyphLayout layout = new GlyphLayout(font, wordModel.getText());
        textLength = layout.width;

        if (!wordModel.getSoundUrl().equals("404")) {
            emojiSound = Gdx.audio.newSound(Gdx.files.internal(wordModel.getSoundUrl()));
            hasSound = true;
        } else {
            hasSound = false;
        }
        fontStage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        counter = 0;
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Label label = new Label(getName(), skin);
        label.setColor(FONT_COLOR);
        Image i = new Image(texture);
        i.setSize(EMOJI_SIZE, EMOJI_SIZE);

        i.setPosition(X_OFFSET, 0);
        label.setPosition(X_OFFSET + (EMOJI_SIZE / 2) - textLength / 2, 0);

        fontStage.addActor(i);
        fontStage.addActor(label);
    }

    public void setBody(Body body) {
        mBody = body;
    }

    public void triggerExplode() {

        if(!isExploded) {
            isExploded = true;
            if (!FxSound.isFxMuted()) {
                if (hasSound) {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            if (!FxSound.isFxMuted()) {
                                emojiSound.play();
                            }
                        }
                    }, 0.3f);
                }
            }
        }

    }

    @Override
    public void update(float dt) {
        setPosition(mBody.getPosition().x - ((Emoji.EMOJI_SIZE / 2) / InfinityRun.PPM)
                , mBody.getPosition().y - ((Emoji.EMOJI_SIZE / 2) / InfinityRun.PPM));
    }

    @Override
    public void render(SpriteBatch sb) {
        if(!isExploded) {
            sb.draw(texture, getX(), getY(), EMOJI_SIZE / InfinityRun.PPM, EMOJI_SIZE / InfinityRun.PPM);
        }
    }

    public void drawExplodedText(){
        counter++;
        if(counter <= COUNTER_MAX) {
            for(Actor actor : fontStage.getActors()) {
                actor.setY(actor.getY() + distanceToTop / COUNTER_MAX);
            }
            if(fontStage.getActors().get(1).getY() >= FADING_Y_POS) {
                float r = fontStage.getActors().get(0).getColor().r;
                float g = fontStage.getActors().get(0).getColor().g;
                float b = fontStage.getActors().get(0).getColor().b;
                fontStage.getActors().get(0).setColor(r, g, b, 1 - (counter / COUNTER_MAX));
                fontStage.getActors().get(1).setColor(0, 0, 0, 1 - (counter / COUNTER_MAX));
            }
            fontStage.draw();
        }
    }

    public boolean shouldDraw(){
        return counter <= COUNTER_MAX;
    }

    public Texture getImage() {
        return texture;
    }

    @Override
    public void dispose() {
        texture.dispose();
        mBody.getWorld().destroyBody(mBody);
        if (hasSound)
            emojiSound.dispose();
        font.dispose();
        fontStage.dispose();
    }

    public void setStartY(float y){
        y *= InfinityRun.PPM;
        for(Actor actor : fontStage.getActors()) {
            actor.setY(y);
            if(actor.getClass() == Label.class)
                actor.setY(actor.getY() + OFFSET_TEXT);
        }
        distanceToTop = InfinityRun.HEIGHT - y;
    }

    public String getName() {
        return wordModel.getText();
    }

    public Texture getTexture() {
        return texture;
    }

    public Word getWordModel() {
        return wordModel;
    }
    public boolean hasExploded(){
        return isExploded;
    }
}
