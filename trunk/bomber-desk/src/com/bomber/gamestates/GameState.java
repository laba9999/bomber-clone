package com.bomber.gamestates;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.bomber.GameScreen;
import com.bomber.renderers.WorldRenderer;
import com.bomber.world.GameWorld;

public abstract class GameState {
	private GameState mPreviousGameState;
	protected GameWorld mGameWorld;
	protected OrthographicCamera mUICamera;
	protected WorldRenderer mWorldRenderer;
	protected Vector3 mTouchPoint;
	protected SpriteBatch mBatcher;

	public GameState(GameScreen _gameScreen) {
		mGameWorld = _gameScreen.mWorld;
		mPreviousGameState = _gameScreen.mGameState;
		mUICamera = _gameScreen.mUICamera;
		mWorldRenderer = _gameScreen.mWorldRenderer;
		mTouchPoint = new Vector3();
		mBatcher = _gameScreen.mBatcher;
	}

	public abstract void update();

	public abstract void parseInput();

	public abstract void present(float _interpolation);
}