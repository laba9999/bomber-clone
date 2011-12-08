package com.bomber.gamestates;

import com.bomber.GameScreen;

public class GameStatePlaying extends GameState {

	public GameStatePlaying(GameScreen _gameScreen) {
		super(_gameScreen);
	}

	public void update()
	{
		mGameScreen.mWorld.update();
	}

	public void parseInput()
	{
		throw new UnsupportedOperationException();
	}

	public void present(float _interpolation)
	{
		throw new UnsupportedOperationException();
	}
}