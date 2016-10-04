package com.teamturtle.infinityrun.screens.level_end_screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.screens.IScreenObserver;

/**
 * Created by Henrik on 2016-10-02.
 */
public class LostLevelScreen extends EndLevelScreen {

    private static final String LB_LEVEL_LOST = "Bana förlorad";

    public LostLevelScreen(SpriteBatch sb, IScreenObserver observer) {
        super(sb, observer, new Texture("ui/ui_bg.png"), LB_LEVEL_LOST, 0);
    }
}
