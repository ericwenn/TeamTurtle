package com.teamturtle.infinityrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamturtle.infinityrun.screens.AbstractScreen;
import com.teamturtle.infinityrun.screens.GameScreen;
import com.teamturtle.infinityrun.screens.IScreenObserver;
import com.teamturtle.infinityrun.screens.LevelSelectScreen;
import com.teamturtle.infinityrun.screens.StartScreen;
import com.teamturtle.infinityrun.screens.level_end_screens.EndLevelScreen;
import com.teamturtle.infinityrun.screens.level_end_screens.LostLevelScreen;
import com.teamturtle.infinityrun.screens.level_end_screens.WonLevelScreen;

public class InfinityRun extends Game implements IScreenObserver {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	public static final float PPM = 75;

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

			case WON_GAME:
				newScreen = new WonLevelScreen(getSpriteBatch(), this, EndLevelScreen.Rating.TWO);
				break;

            case LOST_GAME:
                newScreen = new LostLevelScreen(getSpriteBatch(), this);
                break;

            case LEVELS_MENU:
                newScreen = new LevelSelectScreen(getSpriteBatch(), this);
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
		MAIN_MENU, GAME, WON_GAME, LOST_GAME, LEVELS_MENU
	}
}
