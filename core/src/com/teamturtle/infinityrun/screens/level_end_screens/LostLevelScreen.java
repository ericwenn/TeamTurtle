package com.teamturtle.infinityrun.screens.level_end_screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.screens.IScreenObserver;

/**
 * Created by Henrik on 2016-10-02.
 */
public class LostLevelScreen extends EndLevelScreen {

    private static final String LB_LEVEL_LOST = "Du misslyckades";

    public LostLevelScreen(SpriteBatch sb, IScreenObserver observer, Level level) {
        super(sb, observer, LB_LEVEL_LOST, level, 0);
    }
}
