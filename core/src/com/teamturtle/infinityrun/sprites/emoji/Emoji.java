package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sprites.AbstractEntity;

/**
 * Created by Henrik on 2016-09-21.
 */
public class Emoji extends AbstractEntity {

    private static final float EXPLOSION_SCALE = 1.3f;
    private static final float EMOJI_SIZE = 32;
    private static final int FONT_SIZE = 25;
    private static final String FONT_URL = "fonts/Boogaloo-Regular.ttf";

    private String emojiName;
    private Sound emojiSound;

    private Texture texture;

    private boolean isExploded = false;
    private Body mBody;

    private BitmapFont font;
    private float textLength;

    public Emoji(String emojiName, String soundURL, Texture texture){
        this.emojiName = emojiName;
        this.texture = texture;

        //Create font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_URL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        font = generator.generateFont(parameter);
        generator.dispose();

        GlyphLayout layout = new GlyphLayout(font, emojiName);
        textLength = layout.width;

        emojiSound = Gdx.audio.newSound(Gdx.files.internal(soundURL));
    }

    public void setBody(Body body) {
        mBody = body;
    }

    public void triggerExplode() {
        isExploded = true;
        emojiSound.play();
    }

    @Override
    public void update(float dt) {
        setPosition(mBody.getPosition().x - ((Emoji.EMOJI_SIZE / 2) / InfinityRun.PPM)
                , mBody.getPosition().y - ((Emoji.EMOJI_SIZE / 2) / InfinityRun.PPM));
    }

    @Override
    public void render(SpriteBatch sb) {
        if( isExploded ) {
            //If the emoji as the same position when it has expended, it will look like it moves to
            //the left, with dx, the emoji will "move" equally to left and right.
            float dx = ((EMOJI_SIZE * EXPLOSION_SCALE) - (EMOJI_SIZE)) / ( 2 * InfinityRun.PPM);
            sb.draw( texture, getX() - dx, getY(), EMOJI_SIZE*EXPLOSION_SCALE / InfinityRun.PPM
                    , EMOJI_SIZE*EXPLOSION_SCALE / InfinityRun.PPM);

            //A scaled projection is created to draw font
            Matrix4 scaledProjection = sb.getProjectionMatrix()
                    .scale(1 / (InfinityRun.PPM), 1 / (InfinityRun.PPM), 0);
            sb.setProjectionMatrix(scaledProjection);

            //Draws font above emoji
            float y = (getY() * InfinityRun.PPM) + EMOJI_SIZE * 2;
            float x = (getX() * InfinityRun.PPM) + (EMOJI_SIZE / 2) - textLength / 2;
            font.draw(sb, emojiName, x, y);

            //Reset the projection
            Matrix4 normalProjection = sb.getProjectionMatrix()
                    .scale(InfinityRun.PPM, InfinityRun.PPM, 0);
            sb.setProjectionMatrix(normalProjection);
        } else {
            sb.draw( texture, getX(), getY(), EMOJI_SIZE / InfinityRun.PPM, EMOJI_SIZE / InfinityRun.PPM);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        mBody.getWorld().destroyBody(mBody);
        emojiSound.dispose();
        font.dispose();
    }

}
