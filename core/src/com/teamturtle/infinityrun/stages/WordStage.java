package com.teamturtle.infinityrun.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.models.sentences.Sentence;
import com.teamturtle.infinityrun.models.sentences.SentenceLoader;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.sound.FxSound;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rasmus on 2016-10-02.
 */

public class WordStage extends Stage {

    private static final String FONT_URL = "fonts/Boogaloo-Regular.ttf", BACK_BUTTON_URL = "back_button";
    private static final int TITLE_SIZE = 50, DESCRIPTION_SIZE = 25, TABLE_WIDTH = 650,
    TABLE_HEIGHT = 400, TABLE_X = 75, TABLE_Y = 50, SENTENCE_LENGTH = 45;
    private static final Color FONT_COLOR = Color.WHITE, TABLE_COLOR = Color.GRAY;

    private Sound sound;
    private Boolean hasSound;
    private boolean goBack;

    public WordStage(Word word){
        super(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));
        if(word.getSoundUrl() != null) {
            sound = Gdx.audio.newSound(Gdx.files.internal(word.getSoundUrl()));
            hasSound = true;
        }
        else
            hasSound = false;

        goBack = false;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_URL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter
                = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = TITLE_SIZE;
        parameter.color = FONT_COLOR;
        BitmapFont titleFont = generator.generateFont(parameter);
        parameter.size = DESCRIPTION_SIZE;
        BitmapFont descriptionFont = generator.generateFont(parameter);
        generator.dispose();

        List<String> descriptionList = new ArrayList<String>();

        SentenceLoader sl = new SentenceLoader();
        List<? extends Sentence> sentences = sl.getSentences(word);

        if (sentences != null) {
            for( Sentence s : sentences) {
                descriptionList.add(s.getText());
            }
        } else {
            Gdx.app.log("InfRun", "No words");
        }

        Table table = new Table();
        table.setSize(TABLE_WIDTH, TABLE_HEIGHT);
        table.setPosition(TABLE_X, TABLE_Y);

        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGB565);
        map.setColor(TABLE_COLOR);
        map.fill();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(map))));

        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        Label titleLabel = new Label(word.getText().substring(0, 1).toUpperCase(Locale.getDefault()) +
                word.getText().substring(1), new Label.LabelStyle(titleFont, FONT_COLOR));

        Table descriptionTable = new Table();

        for(String s : descriptionList){
            int rowOffset;
            for(int i = 0; i <s.length(); i+=SENTENCE_LENGTH){
                Label label;
                //If the sentence is to long, it needs to get cut into multiple rows.
                if(s.length() > i + SENTENCE_LENGTH){
                    //Variable to know which space is furthest to the right
                    int highestSpace = 0;
                    int index = 0;
                    //Find the higestSpace variable, is needed so one word isnt cut in half.
                    for(char c : s.substring(i, i + SENTENCE_LENGTH).toCharArray()){
                        if(c == ' '){
                            highestSpace = index;
                        }
                        index++;
                    }
                    label = new Label(s.substring(i, i + highestSpace),
                            new Label.LabelStyle(descriptionFont, FONT_COLOR));
                    //Sets i to the word after the last word added the label, -1 offset because
                    //"space" isnt requiereed to be printed when there is a new row.
                    i -= (SENTENCE_LENGTH-highestSpace-1);
                    rowOffset = 0;
                }else {
                    label = new Label(s.substring(i, s.length()),
                            new Label.LabelStyle(descriptionFont, FONT_COLOR));
                    rowOffset = 10;
                }
                descriptionTable.add(label).left().padBottom(rowOffset);
                descriptionTable.row();
            }
        }

        Image image = new Image(new Texture(word.getIconUrl()));

        ImageButton soundButton = new ImageButton(skin, "listen_button");
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(hasSound && !FxSound.isFxMuted())
                    sound.play();
            }
        });

        ImageButton returnButton = new ImageButton(skin, BACK_BUTTON_URL);
        returnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    FxSound.TILLBAKA.play();
                    goBack = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        table.add(titleLabel).left().padTop(25);
        table.add(soundButton).left().padTop(25).padLeft(30);
        table.row().padTop(10);
        table.add(image).left();
        table.add(descriptionTable).left().padLeft(40).top();
        table.row().padTop(25);
        table.add(returnButton).colspan(2).left();
        addActor(table);
        Gdx.input.setInputProcessor(this);
    }

    public boolean shouldGoBack() {
        return goBack;
    }
}
