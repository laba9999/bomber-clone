package com.bomber.gamestates;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.bomber.GameScreen;
import com.bomber.gameobjects.Player;

public class GameStatePlaying extends GameState {

	private boolean mJustPlacedBomb = false;

	public GameStatePlaying(GameScreen _gameScreen) {
		super(_gameScreen);
	}

	public void update()
	{
		parseInput();
		mGameScreen.mWorld.update();

	}

	public void parseInput()
	{
		Player localPlayer = mGameScreen.mWorld.getLocalPlayer();
		if (Gdx.app.getType() != Application.ApplicationType.Android)
		{

			if (Gdx.input.isKeyPressed(Keys.LEFT))
				localPlayer.moveLeft();

			else if (Gdx.input.isKeyPressed(Keys.RIGHT))
				localPlayer.moveRight();
			else if (Gdx.input.isKeyPressed(Keys.DOWN))
				localPlayer.moveDown();
			else if (Gdx.input.isKeyPressed(Keys.UP))
				localPlayer.moveUp();
			else
				localPlayer.stop();

			if (Gdx.input.isKeyPressed(Keys.SPACE))
			{
				if (!mJustPlacedBomb)
				{
					localPlayer.dropBomb();
					mJustPlacedBomb = true;
				}
			} else
				mJustPlacedBomb = false;

		}
	}

	public void present(float _interpolation)
	{
		mGameScreen.mWorldRenderer.render();
	}

}