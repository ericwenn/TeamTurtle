package com.teamturtle.infinityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.PathConstants;
import com.teamturtle.infinityrun.models.category.CategoryLoader;
import com.teamturtle.infinityrun.models.words.Word;
import com.teamturtle.infinityrun.models.words.WordLoader;
import com.teamturtle.infinityrun.sound.FxSound;
import com.teamturtle.infinityrun.stages.WordStage;
import com.teamturtle.infinityrun.storage.PlayerData;

import java.util.List;

public class DictionaryScreen extends AbstractScreen {

    private static final String LABEL_BG_URL = "label_bg.png";
    private static final String LINE_IMAGE_URL = "ui/line.png";
    private static final String LABEL_TEXT = "Ord hittade:";
    private static final String LABEL_UNKNOWN = "???";
    private static final int ROW_PAD = 20;
    private static final int CATEGORY_PAD = 20;
    private static final int PAD_SCROLL = 5;
    private static final int GRID_COLUMN_WIDTH = 4;
    private static final int CATEGORY_ONE_OFFSET = -8;
    private static final int CATEGORY_ONE = 1;

    private final Stage stage;
    private WordStage wordStage;
    private final IScreenObserver observer;
    private final Skin skin;
    private final Texture bg;
    private final Texture line;
    private ImageButton imageButton;
    private boolean isDictionaryScreen;

    public DictionaryScreen(final SpriteBatch spriteBatch, IScreenObserver observer) {
        super(spriteBatch);
        this.observer = observer;
        stage = new Stage(new FillViewport(InfinityRun.WIDTH, InfinityRun.HEIGHT));

        bg = new Texture(PathConstants.BACKGROUND_PATH);
        line = new Texture(LINE_IMAGE_URL);

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
        skin.load(Gdx.files.internal("skin/uiskin.json"));

        initBackButton();
        initGridTable();

        isDictionaryScreen = true;
    }

    private void initBackButton() {
        imageButton = new ImageButton(skin, "home_button");
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    FxSound.HEM.play();
                    observer.changeScreen(InfinityRun.ScreenID.MAIN_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        imageButton.pad(10f);
    }


    private void initGridTable() {
        WordLoader wordLoader = new WordLoader();
        CategoryLoader categoryLoader = new CategoryLoader();
        PlayerData playerData = new PlayerData();

        Table grid = new Table();

        int collectedWordsAmount = 0;
        List<Word> allWords = wordLoader.getAllWords();
        int categoryBefore = 0;
        int columnCount = 0;
        for(int i = 0; i < allWords.size(); i++){
            Word word = allWords.get(i);
            columnCount++;
            int currentCategory = word.getCategory();
            if (categoryBefore != currentCategory) {
                grid.row();
                String category = categoryLoader.getCategoryName(word.getCategory());
                Label label = new Label(category, skin, "text-black");
                if (currentCategory == CATEGORY_ONE) {
                    grid.add(label).colspan(GRID_COLUMN_WIDTH).padTop(CATEGORY_ONE_OFFSET);
                }else{
                    grid.add(label).colspan(GRID_COLUMN_WIDTH).padTop(CATEGORY_PAD);

                }
                Image lineImg = new Image(line);
                grid.row();
                grid.add(lineImg).colspan(GRID_COLUMN_WIDTH).pad(ROW_PAD);
                grid.row();
                columnCount = 0;
            } else if (columnCount % GRID_COLUMN_WIDTH == 0) {
                grid.row().padTop(ROW_PAD);
            }

            categoryBefore = word.getCategory();

            if (playerData.hasPlayerCollectedWord(word)) {
                grid.add(createGridItem(word, false));
                collectedWordsAmount++;
            }else{
                grid.add(createGridItem(word, true));
            }
        }

        ScrollPane scroller = new ScrollPane(grid);
        scroller.setForceScroll(false, true);
        scroller.setScrollingDisabled(true, false);
        Table rootTable = new Table();
        rootTable.setFillParent(true);

        Table leftPanel = new Table();
        leftPanel.add(imageButton);
        leftPanel.row();
        Table labelBg = new Table();
        labelBg.add(new Label(
                 LABEL_TEXT + "\n" + collectedWordsAmount + " / " + allWords.size(), skin));
        labelBg.setBackground(
                new TextureRegionDrawable(new TextureRegion(new Texture(LABEL_BG_URL))));
        leftPanel.add(labelBg);
        rootTable.add(leftPanel).left().top();
        rootTable.add(scroller).fill().expand().pad(PAD_SCROLL);
        stage.addActor(rootTable);
    }

    private Table createGridItem(final Word word, final boolean tinted) {
        final Table gridItem = new Table();
        Image image = new Image(new Texture(word.getIconUrl()));
        Label label;
        if (tinted) {
            image.setColor(Color.BLACK);
            label = new Label(LABEL_UNKNOWN, skin, "text-black");
        }else{
            label = new Label(word.getText(), skin, "text-black");
        }
        gridItem.add(image);
        gridItem.row();
        gridItem.add(label);
        gridItem.setTouchable(Touchable.enabled);
        gridItem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if(!tinted) {
                        wordStage = new WordStage(word);
                        isDictionaryScreen = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return gridItem;
    }

    @Override
    public void buildStage()
    {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        getSpriteBatch().begin();
        getSpriteBatch().draw(bg, 0, 0, getViewport().getWorldWidth(), getViewport().getWorldHeight());
        getSpriteBatch().end();

        if(isDictionaryScreen) {
            stage.act(dt);
            stage.draw();
        }
        else if(wordStage.shouldGoBack()) {
            isDictionaryScreen = true;
            Gdx.input.setInputProcessor(stage);
        }
        else {
            wordStage.act(dt);
            wordStage.draw();
        }
    }
}
