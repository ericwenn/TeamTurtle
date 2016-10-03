package com.teamturtle.infinityrun.screens.level_end_screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.screens.IScreenObserver;

/**
 * Created by Henrik on 2016-10-02.
 */
public class WonLevelScreen extends EndLevelScreen{

    private static final String LB_LEVEL_LOST = "Bana klarad";

    private ImageButton nextButton;

    public WonLevelScreen(SpriteBatch sb, final IScreenObserver observer, Rating rating) {
        super(sb, observer, new Texture("ui/ui_bg_big.png"), LB_LEVEL_LOST, rating);
        Skin skin = super.getSkin();
        nextButton = new ImageButton(skin, "next_button");
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    observer.changeScreen(InfinityRun.ScreenID.GAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        super.getButtonTable().add(nextButton).pad(BUTTON_PADDING);
    }

}
