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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Timer;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordImpl;
import com.teamturtle.infinityrun.models.words.WordLoader;
import com.teamturtle.infinityrun.sprites.AbstractEntity;

import java.util.Map;

/**
 * Created by Henrik on 2016-09-21.
 */
public class Emoji extends AbstractEntity {

    private static final float EXPLOSION_SCALE = 1.3f;
    private static final float EMOJI_SIZE = 32;
    private static final int FONT_SIZE = 25, COUNTER_MAX = 120, OFFSET_BIG_EMOJI = -25;
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
        fontStage = new Stage();
        counter = 0;
    }

    public void setBody(Body body) {
        mBody = body;
    }

    public void triggerExplode() {
        if(!isExploded) {
            isExploded = true;
            if (hasSound)
                emojiSound.play();

            Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
            Label label = new Label(getName(), skin);
            label.setColor(FONT_COLOR);
            Image i = new Image(texture);

            label.setPosition(InfinityRun.WIDTH / 2, InfinityRun.HEIGHT / 2 + i.getHeight());
            i.setPosition(InfinityRun.WIDTH / 2 + OFFSET_BIG_EMOJI, InfinityRun.HEIGHT / 2);

            fontStage.addActor(label);
            fontStage.addActor(i);
        }
        isExploded = true;
        if (hasSound) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    emojiSound.play();
                }
            }, 0.3f);
        }
    }

    @Override
    public void update(float dt) {
        setPosition(mBody.getPosition().x - ((Emoji.EMOJI_SIZE / 2) / InfinityRun.PPM)
                , mBody.getPosition().y - ((Emoji.EMOJI_SIZE / 2) / InfinityRun.PPM));
    }

    @Override
    public void render(SpriteBatch sb) {
        if (isExploded) {
            //If the emoji as the same position when it has expended, it will look like it moves to
            //the left, with dx, the emoji will "move" equally to left and right.
            float dx = ((EMOJI_SIZE * EXPLOSION_SCALE) - (EMOJI_SIZE)) / (2 * InfinityRun.PPM);
            sb.draw(texture, getX() - dx, getY(), EMOJI_SIZE * EXPLOSION_SCALE / InfinityRun.PPM
                    , EMOJI_SIZE * EXPLOSION_SCALE / InfinityRun.PPM);

            //A scaled projection is created to draw font
            Matrix4 scaledProjection = sb.getProjectionMatrix()
                    .scale(1 / (InfinityRun.PPM), 1 / (InfinityRun.PPM), 0);
            sb.setProjectionMatrix(scaledProjection);

            //Draws font above emoji
            float y = (getY() * InfinityRun.PPM) + EMOJI_SIZE * 2;
            float x = (getX() * InfinityRun.PPM) + (EMOJI_SIZE / 2) - textLength / 2;
            font.draw(sb, wordModel.getText(), x, y);

            //Reset the projection
            Matrix4 normalProjection = sb.getProjectionMatrix()
                    .scale(InfinityRun.PPM, InfinityRun.PPM, 0);
            sb.setProjectionMatrix(normalProjection);
        } else {
            sb.draw(texture, getX(), getY(), EMOJI_SIZE / InfinityRun.PPM, EMOJI_SIZE / InfinityRun.PPM);
        }
    }

    public void drawExplodedText(){
        counter++;
        if(counter <= COUNTER_MAX)
            fontStage.draw();
    }

    public boolean shouldDraw(){
        return counter <= COUNTER_MAX;
    }

    public Texture getImage(){

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
