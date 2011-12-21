package com.bomber.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.common.assets.Level;
import com.bomber.gamestates.GameState;
import com.bomber.world.GameWorld;

public class InputLevelCompletedState extends Input {

	private static final short INPUT_RELOAD = 0;
	private static final short INPUT_MENU = 1;
	private static final short INPUT_CONTINUE = 2;

	public InputLevelCompletedState(GameState _gameState) {
		super(_gameState);
		mInputZones = new Rectangle[3];
		mInputZones[INPUT_RELOAD] = new Rectangle(220, 75, 100, 100);
		mInputZones[INPUT_MENU] = new Rectangle(340, 75, 100, 100);
		mInputZones[INPUT_CONTINUE] = new Rectangle(480, 75, 100, 100);

	}

	@Override
	protected void parseKeyboardInput()
	{
		if (Gdx.input.isKeyPressed(Keys.ENTER))
			parseInputZone(INPUT_CONTINUE);
		else if(Gdx.input.isKeyPressed(Keys.R))
			parseInputZone(INPUT_RELOAD);
	}

	@Override
	protected void parseTouchInput()
	{
		if (Gdx.input.justTouched())
		{
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			for (short i = INPUT_RELOAD; i <= INPUT_CONTINUE; i++)
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
		GameWorld world = mGameState.mGame.mWorld;
		switch (_zone)
		{
		case INPUT_CONTINUE:
			
			int tempScore = world.getLocalPlayer().mPoints;
			
			world.getLocalPlayer().mStartLevelPoints = world.getLocalPlayer().mPoints;
			world.reset(Level.mInfo.mNextLevelName);	
			
			world.getLocalPlayer().mPoints = tempScore;
			
			mGameState.finish(mGameState.mPreviousGameState);
			break;
		case INPUT_RELOAD:
			world.getLocalPlayer().mPoints = world.getLocalPlayer().mStartLevelPoints;
			mGameState.mGame.mWorld.reset(Level.mInfo.mCurrentLevelName);
			mGameState.finish(mGameState.mPreviousGameState);
			break;
		case INPUT_MENU:
			break;
		}
	}
}