package com.bomber.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.GameScreen;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStatePlaying;
import com.bomber.world.Level;

public class InputGameOverState extends Input {
	private static final short INPUT_CONTINUE = 0;
	private static final short INPUT_MENU = 1;

	public InputGameOverState(GameState _gameState) {
		super(_gameState);

		mInputZones = new Rectangle[2];
		mInputZones[INPUT_CONTINUE] = new Rectangle(275, 75, 100, 100);
		mInputZones[INPUT_MENU] = new Rectangle(435, 74, 100, 100);
	}

	@Override
	protected void parseKeyboardInput()
	{
		if (Gdx.input.isKeyPressed(Keys.ENTER))
		{
			mGameState.mGameScreen.mWorld.reset(Level.mInfo.mCurrentLevelName);
			GameStatePlaying g = new GameStatePlaying(mGameState.mGameScreen);
			mGameState.mGameScreen.setGameState(g);
		}

	}

	@Override
	protected void parseTouchInput()
	{
		if (Gdx.input.justTouched())
		{
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (mInputZones[INPUT_CONTINUE].contains(mTouchPoint.x, mTouchPoint.y))
			{
				mGameState.mGameScreen.mWorld.reset(Level.mInfo.mCurrentLevelName);
				GameStatePlaying g = new GameStatePlaying(mGameState.mGameScreen);
				mGameState.mGameScreen.setGameState(g);

				return;
			} else if (mInputZones[INPUT_MENU].contains(mTouchPoint.x, mTouchPoint.y))
			{

				return;
			}
		}
	}

}
