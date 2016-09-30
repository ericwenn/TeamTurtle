package com.teamturtle.infinityrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.screens.AbstractScreen;
import com.teamturtle.infinityrun.screens.GameScreen;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.screens.StartScreen;

public class InfinityRun extends Game implements IScreenObserver{

	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	public static final float PPM = 16*5;

	private SpriteBatch mSpriteBatch;

	@Override
	public void create () {
		setSpriteBatch(new SpriteBatch());

		try{
			changeScreen(ScreenID.MAIN_MENU);
		}catch (Exception e){
			// This cannot fail...yet
		}
	}

	private void setSpriteBatch(SpriteBatch sb){
		this.mSpriteBatch = sb;
	}

	private SpriteBatch getSpriteBatch(){
		return this.mSpriteBatch;
	}

	/*@Override
	public void render () {
		super.render();
	}*/
	
	@Override
	public void dispose () {
		getSpriteBatch().dispose();
	}

	@Override
	public void changeScreen(ScreenID screen) throws Exception {
		AbstractScreen newScreen;

		switch (screen){
			case MAIN_MENU:
				newScreen = new StartScreen(getSpriteBatch(), this);
				break;

			case GAME:
				newScreen = new GameScreen(getSpriteBatch(),GameScreen.Level.LEVEL_1,this);
				break;

			default:
				throw new Exception("Unknown screen enum");
		}

		Screen oldScreen = getScreen();

		// Set the new screen
		newScreen.buildStage();
		setScreen(newScreen);

		// Dispose the old one
		oldScreen.dispose();


	}

	public enum ScreenID{
		MAIN_MENU, GAME
	}
}
