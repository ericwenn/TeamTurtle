package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sprites.AbstractEntity;

/**
 * Created by Henrik on 2016-09-21.
 */
public class Emoji extends AbstractEntity {

    private static final float EXPLOSION_SCALE = 1.3f;
    public static final float EMOJI_SIZE = 32;
    private static final int TEXT_OFFSET = 135;

    private String emojiName;
    private Sound emojiSound;

    private Texture texture;

    private boolean isExploded = false;
    private Body mBody;

    private Stage stage;
    private Viewport viewport;
    private Label textLabel;

    public Emoji(String emojiName, String soundURL, Texture texture){
        this.emojiName = emojiName;
        this.texture = texture;

        emojiSound = Gdx.audio.newSound(Gdx.files.internal(soundURL));
        setPosition(400, InfinityRun.HEIGHT - 100);
    }




    public void setBody(Body body) {
        mBody = body;
    }



    public void triggerExplode(SpriteBatch sb) {
        isExploded = true;
        emojiSound.play();

        //Create stage for text
        viewport = new FitViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        textLabel = new Label(emojiName, new Label.LabelStyle(new BitmapFont(), Color.RED));

        //Adds the text to the stage
        stage.addActor(textLabel);
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
        } else {
            sb.draw( texture, getX(), getY(), EMOJI_SIZE / InfinityRun.PPM, EMOJI_SIZE / InfinityRun.PPM);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        mBody.getWorld().destroyBody(mBody);
        emojiSound.dispose();
        //stage.dispose();
    }
    public boolean getIsExploded(){
        return isExploded;
    }
    public void drawText(float playerX){
        //Calculates where the text should be drawn on the screen.
        int x = (int)((getX() - playerX) * InfinityRun.PPM + TEXT_OFFSET);
        //+40 is just a temporary variable to make up for the about 40 pixels voisd(black box) in the beginning.
        //Should be removed when the black box is fixed.
        int y = (int)((getY() * InfinityRun.PPM) + 40);
        textLabel.setPosition(x, y);
        stage.draw();
    }

}
