package com.bomber.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.Game;
import com.bomber.common.Settings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.common.assets.Level;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStatePaused;
import com.bomber.world.GameWorld;

public class InputPausedState extends Input {
	private static final short INPUT_CONTINUE = 0;
	private static final short INPUT_RELOAD = 1;
	private static final short INPUT_SOUND = 2;
	private static final short INPUT_HELP = 3;
	private static final short INPUT_BACK = 4;
	private long mTimeWhenLastPress = 0;
	
	public InputPausedState(GameState _gameState) {
		super(_gameState);

		mInputZones = new Rectangle[5];
		mInputZones[INPUT_CONTINUE] = new Rectangle(635, 365, 95, 90);
		mInputZones[INPUT_RELOAD] = new Rectangle(635, 245, 95, 90);
		mInputZones[INPUT_BACK] = new Rectangle(635, 125, 95, 90);
		mInputZones[INPUT_SOUND] = new Rectangle(570, 15, 90, 90);
		mInputZones[INPUT_HELP] = new Rectangle(700, 15, 95, 90);
	}

	@Override
	protected void parseKeyboardInput()
	{
		

		
		if (Gdx.input.isKeyPressed(Keys.P))
			parseInputZone(INPUT_CONTINUE);
		else if (Gdx.input.isKeyPressed(Keys.R))
			parseInputZone(INPUT_RELOAD);
		else if (Gdx.input.isKeyPressed(Keys.S))
		{
			parseInputZone(INPUT_SOUND);			
		}
	}

	@Override
	protected void parseTouchInput()
	{
		if (Gdx.input.justTouched())
		{
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			for (short i = INPUT_CONTINUE; i <= INPUT_BACK; i++)
			{
				if (mInputZones[i].contains(mTouchPoint.x, mTouchPoint.y))
				{
					parseInputZone(i);
					break;
				}
			}
		}
	}

	@Override
	protected void parseInputZone(short _zone)
	{
		
		if(System.currentTimeMillis() - mTimeWhenLastPress < 500)
		{
		 return;	
		}
		
		GameWorld world = mGameState.mGame.mWorld;

		switch (_zone)
		{
		case INPUT_CONTINUE:
			mGameState.finish(mGameState.mPreviousGameState);
			break;
		case INPUT_RELOAD:
			
			world.reset(Level.mInfo.mCurrentLevelName);
			world.getLocalPlayer().mPoints = world.getLocalPlayer().mStartLevelPoints;

			mGameState.finish(mGameState.mPreviousGameState);
			break;
		case INPUT_SOUND:
			Settings.isSoundOn = !Settings.isSoundOn;			
			GameStatePaused.mOptionsPanel.getChild((short)0).mTexture = GfxAssets.getSoundButtonTexture();				
			mTimeWhenLastPress = System.currentTimeMillis();
			break;
		case INPUT_HELP:
			Game.goToHelpActivity();
			break;

		case INPUT_BACK:
			Game.goBackToActivities();
			break;
		}
	}
}
