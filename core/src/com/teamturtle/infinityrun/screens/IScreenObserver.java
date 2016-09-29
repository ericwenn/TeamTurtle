package com.teamturtle.infinityrun.screens;

import com.teamturtle.infinityrun.InfinityRun;

/**
 * Created by ostmos on 2016-09-28.
 */
public interface IScreenObserver {

    void changeScreen(InfinityRun.ScreenID id) throws Exception;

}
