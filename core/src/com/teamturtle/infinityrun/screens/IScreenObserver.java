package com.teamturtle.infinityrun.screens;

import com.teamturtle.infinityrun.InfinityRun;
import com.teamturtle.infinityrun.sprites.emoji.Emoji;

/**
 * Created by ostmos on 2016-09-28.
 */
public interface IScreenObserver {

    void changeScreen(InfinityRun.ScreenID id) throws Exception;
    void changeScreen(Emoji emoji) throws Exception;

}
