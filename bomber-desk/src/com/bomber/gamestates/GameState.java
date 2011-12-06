package com.bomber.gamestates;

import com.bomber.GameScreen;

public abstract class GameState {
	private GameState mPreviousGameState;
	public GameScreen mGameScreen;

	public GameState(GameScreen _gameScreen) {
		mGameScreen = _gameScreen;
		mPreviousGameState = mGameScreen.mGameState;
		mGameScreen.mGameState = this;
	}

	public abstract void update();

	public abstract void parseInput();

	public abstract void present(float _interpolation);
}