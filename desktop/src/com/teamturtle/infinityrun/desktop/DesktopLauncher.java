package com.teamturtle.infinityrun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.teamturtle.infinityrun.InfinityRun;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = InfinityRun.WIDTH;
		config.height = InfinityRun.HEIGHT;
		new LwjglApplication(new InfinityRun(), config);
	}
}
