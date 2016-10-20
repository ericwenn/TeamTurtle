package com.teamturtle.infinityrun.sprites.emoji;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teamturtle.infinityrun.models.words.Word;

/**
 * Created by Alfred on 2016-10-18.
 */

public class EmojiFactory {

    /*
        SINGLETON
     */
    private static EmojiFactory instance;

    public static EmojiFactory getInstance(){
        if (instance == null){
            instance = new EmojiFactory();
        }

        return instance;
    }

    /*
        Properties
     */

    private static final int FONT_SIZE = 25;
    private static final String FONT_URL = "fonts/Boogaloo-Regular.ttf";
    private static final Color FONT_COLOR = Color.BLACK;
    private BitmapFont font;

    private Skin skin;


    private EmojiFactory(){
        // Setup factory
        generateFont();
        fetchSkin();
    }

    private void generateFont(){
        //Create font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_URL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        parameter.color = FONT_COLOR;
        this.font = generator.generateFont(parameter);
        generator.dispose();
    }

    private void fetchSkin(){
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    }

    public Emoji getEmoji(Word word){
        return new Emoji(word, this.font, this.skin);
    }
}
