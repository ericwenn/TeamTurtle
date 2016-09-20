package com.teamturtle.infinityrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InfinityRun extends Game {

	private SpriteBatch mSpriteBatch;

	@Override
	public void create () {
		mSpriteBatch = new SpriteBatch();
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
