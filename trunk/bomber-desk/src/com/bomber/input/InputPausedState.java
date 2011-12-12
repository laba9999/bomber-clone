package com.bomber.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.gamestates.GameState;

public class InputPausedState extends Input {
	private static final short INPUT_CONTINUE = 0;

	public InputPausedState(GameState _gameState) {
		super(_gameState);

		mInputZones = new Rectangle[1];
		mInputZones[INPUT_CONTINUE] = new Rectangle(360, 200, 80, 80);
	}

	@Override
	protected void parseKeyboardInput()
	{
		if (Gdx.input.isKeyPressed(Keys.P))
			mGameState.mGameScreen.setGameState(mGameState.mPreviousGameState);
	}

	@Override
	protected void parseTouchInput()
	{
		if (Gdx.input.justTouched())
		{
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (mInputZones[INPUT_CONTINUE].contains(mTouchPoint.x, mTouchPoint.y))
			{
				mGameState.mGameScreen.setGameState(mGameState.mPreviousGameState);
				return;
			}
		}

	}

}
