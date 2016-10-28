package com.teamturtle.infinityrun.screens.endscreens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.models.level.Level;
import com.teamturtle.infinityrun.screens.IScreenObserver;

/**
 * Created by Henrik on 2016-10-02.
 */
public class GameOverScreen extends AbstractEndScreen {

    private static final String LB_LEVEL_LOST = "Du misslyckades";

    public GameOverScreen(SpriteBatch sb, IScreenObserver observer, Level level) {
        super(sb, observer, LB_LEVEL_LOST, level, 0);
    }
}
