package com.teamturtle.infinityrun.screens.level_end_screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.models.level.LevelDataHandler;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.sound.FeedbackSound;

import java.util.List;

/**
 * Created by Henrik on 2016-10-02.
 */
public class WonLevelScreen extends EndLevelScreen{

    private static final String LB_LEVEL_LOST = "Bana klarad";

    private ImageButton nextButton;

    public WonLevelScreen(SpriteBatch sb, IScreenObserver observer, Level level, int score) {
        super(sb, observer, new Texture("ui/ui_bg_big.png"), LB_LEVEL_LOST, level, score);
        Skin skin = super.getSkin();
        List<Level> levels = new LevelDataHandler().getLevels();
        if(levels.size() > level.getId()) {
            nextButton = new ImageButton(skin, "next_button");
            nextButton.addListener(new ImageClickListener(observer, level));
            nextButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    FeedbackSound.NASTABANA.play();
                }
            });
            super.getButtonTable().add(nextButton).pad(BUTTON_PADDING);
        }
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
