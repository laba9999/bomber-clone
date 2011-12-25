package com.bomber.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.common.assets.GfxAssets;
import com.bomber.input.Input;
import com.bomber.remote.MessageContainer;
import com.bomber.remote.RemoteConnections;
import com.bomber.renderers.WorldRenderer;
import com.bomber.world.GameWorld;

public abstract class GameState {
	public GameState mPreviousGameState;
	public GameState mNextGameState;
	public Game mGame;
	protected GameWorld mGameWorld;
	protected OrthographicCamera mUICamera;
	protected WorldRenderer mWorldRenderer;
	protected Vector3 mTouchPoint;
	protected SpriteBatch mBatcher;
	protected Input mInput;

	protected boolean mIsFinishing;

	public GameState(Game _game) {
		mGame = _game;
		mGameWorld = _game.mWorld;
		mPreviousGameState = _game.getGameState();
		mUICamera = _game.mUICamera;
		mWorldRenderer = _game.mWorldRenderer;
		mTouchPoint = new Vector3();
		mBatcher = _game.mBatcher;

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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		onPresent(_interpolation);

		// Informação de debug
		drawDebugInfo();

		mBatcher.end();
	}

	private final void drawDebugInfo()
	{
		BitmapFont font = GfxAssets.mFont;
		if (DebugSettings.UI_DRAW_INPUT_ZONES)
		{
			Rectangle[] zones = mInput.getZones();
			for (int i = 0; i < zones.length; i++)
				mBatcher.draw(GfxAssets.mAtlas.findRegion("tiles_", 123), zones[i].x, zones[i].y, zones[i].width, zones[i].height);
		}

		if (DebugSettings.UI_DRAW_FPS)
		{
			Integer fps = Gdx.graphics.getFramesPerSecond();
			font.draw(mBatcher, "F: " + fps.toString(), 100, 470);
			font.draw(mBatcher, "T: " + Game.mTicksPerSecond.toString(), 165, 470);
			font.draw(mBatcher, "M: " + MessageContainer.mMessagesPerSecond.toString(), 570, 470);
			font.draw(mBatcher, "Id: " + RemoteConnections.mLocalID, 645, 470);
		}
	}

	public abstract void onPresent(float _interpolation);

	protected abstract void onUpdate();

	protected abstract void onUpdateFinishing();

	protected abstract void onFinish();
}