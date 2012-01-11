package com.bomber.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.Game;
import com.bomber.common.assets.Level;
import com.bomber.gamestates.GameState;

public class InputGameOverState extends Input {
	private static final short INPUT_RELOAD = 0;
	private static final short INPUT_MENU = 1;

	public InputGameOverState(GameState _gameState) {
		super(_gameState);

		mInputZones = new Rectangle[2];
		mInputZones[INPUT_RELOAD] = new Rectangle(275, 75, 100, 100);
		mInputZones[INPUT_MENU] = new Rectangle(435, 75, 100, 100);
	}

	@Override
	protected void parseKeyboardInput()
	{
		if (Gdx.input.isKeyPressed(Keys.R))
			parseInputZone(INPUT_RELOAD);

	}

	@Override
	protected boolean parseTouchInput()
	{
		boolean hasTouched = false;
		
		if (Gdx.input.justTouched())
		{
			hasTouched = true;
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			for (short i = INPUT_RELOAD; i <= INPUT_MENU; i++)
			{
				if (mInputZones[i].contains(mTouchPoint.x, mTouchPoint.y))
				{
					parseInputZone(i);
					break;
				}
			}
		}
		
		return hasTouched;
	}

	@Override
	protected void parseInputZone(short _zone)
	{
		switch(_zone)
		{
		case INPUT_RELOAD:
			mGameState.mGame.mWorld.reset(Level.mInfo.mCurrentLevelName);
			mGameState.finish(mGameState.mPreviousGameState);
			break;
			
		case INPUT_MENU:
			Game.goBackToActivities();
			break;
		}
		
	}

}
