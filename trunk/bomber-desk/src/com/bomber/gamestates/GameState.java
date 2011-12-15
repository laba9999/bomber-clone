package com.bomber.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bomber.DebugSettings;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.input.Input;
import com.bomber.renderers.WorldRenderer;
import com.bomber.world.GameWorld;

public abstract class GameState {
	public GameState mPreviousGameState;
	public GameState mNextGameState;
	public GameScreen mGameScreen;
	protected GameWorld mGameWorld;
	protected OrthographicCamera mUICamera;
	protected WorldRenderer mWorldRenderer;
	protected Vector3 mTouchPoint;
	protected SpriteBatch mBatcher;
	protected Input mInput;

	protected boolean mIsFinishing;

	public GameState(GameScreen _gameScreen) {
		mGameScreen = _gameScreen;
		mGameWorld = _gameScreen.mWorld;
		mPreviousGameState = _gameScreen.getGameState();
		mUICamera = _gameScreen.mUICamera;
		mWorldRenderer = _gameScreen.mWorldRenderer;
		mTouchPoint = new Vector3();
		mBatcher = _gameScreen.mBatcher;

		mIsFinishing = false;
	}

	public void reset()
	{
		mIsFinishing = false;

	}

	public final void finish(GameState _newState)
	{
		mIsFinishing = true;
		mNextGameState = _newState;
		onFinish();
	}

	public final void update()
	{
		if (!mIsFinishing)
			onUpdate();
		else
			onUpdateFinishing();
	}

	public void present(float _interpolation)
	{
		mBatcher.begin();
		onPresent(_interpolation);

		// Informação de debug
		drawDebugInfo();

		mBatcher.end();
	}

	private final void drawDebugInfo()
	{
		BitmapFont font = Assets.mFont;
		if (DebugSettings.UI_DRAW_INPUT_ZONES)
		{
			Rectangle[] zones = mInput.getZones();
			for (int i = 0; i < zones.length; i++)
				mBatcher.draw(Assets.mAtlas.findRegion("tiles_", 123), zones[i].x, zones[i].y, zones[i].width, zones[i].height);
		}

		if (DebugSettings.UI_DRAW_FPS)
		{
			Integer fps = Gdx.graphics.getFramesPerSecond();
			font.draw(mBatcher, fps.toString(), 115, 470);
			font.draw(mBatcher, GameScreen.ticksPerSecond.toString(), 165, 470);
		}
	}

	public abstract void onPresent(float _interpolation);

	protected abstract void onUpdate();

	protected abstract void onUpdateFinishing();

	protected abstract void onFinish();
}