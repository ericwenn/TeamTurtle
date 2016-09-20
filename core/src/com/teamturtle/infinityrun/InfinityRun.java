package com.teamturtle.infinityrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.screens.GameScreen;

public class InfinityRun extends Game {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;

	private SpriteBatch mSpriteBatch;

	@Override
	public void create () {
		mSpriteBatch = new SpriteBatch();
		setScreen(new GameScreen(mSpriteBatch));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		mSpriteBatch.dispose();
	}
}
