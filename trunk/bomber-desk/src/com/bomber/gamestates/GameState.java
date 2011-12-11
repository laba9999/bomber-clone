package com.bomber.gamestates;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.bomber.GameScreen;
import com.bomber.input.Input;
import com.bomber.renderers.WorldRenderer;
import com.bomber.world.GameWorld;

public abstract class GameState {
	public GameState mPreviousGameState;
	public GameScreen mGameScreen;
	protected GameWorld mGameWorld;
	protected OrthographicCamera mUICamera;
	protected WorldRenderer mWorldRenderer;
	protected Vector3 mTouchPoint;
	protected SpriteBatch mBatcher;
	protected Input mInput;
	
	public GameState(GameScreen _gameScreen) {
		mGameScreen = _gameScreen;
		mGameWorld = _gameScreen.mWorld;
		mPreviousGameState = _gameScreen.getGameState();
		mUICamera = _gameScreen.mUICamera;
		mWorldRenderer = _gameScreen.mWorldRenderer;
		mTouchPoint = new Vector3();
		mBatcher = _gameScreen.mBatcher;
	}

	public abstract void update();

	public abstract void present(float _interpolation);
}