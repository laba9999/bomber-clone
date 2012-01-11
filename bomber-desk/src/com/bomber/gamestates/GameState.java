package com.bomber.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.common.Strings;
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

	public final void reset()
	{
		mIsFinishing = false;
		onReset();
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

	public void present()
	{
		mBatcher.begin();
		Gdx.gl.glClearColor(0.21f, 0.21f, 0.21f, 0.8f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (!GfxAssets.mFinishedLoading)
		{
			BitmapFont font = GfxAssets.mBigFont;
			mBatcher.setProjectionMatrix(mUICamera.combined);
			font.draw(mBatcher, Strings.mStrings.get("loading"), 320, 250);
			mBatcher.end();
			return;
		}

		onPresent();

		// Informação de debug
		drawDebugInfo();

		mBatcher.end();
	}

	private final void drawDebugInfo()
	{
		BitmapFont font = GfxAssets.mGenericFont;
		if (Settings.UI_DRAW_INPUT_ZONES)
		{
			Rectangle[] zones = mInput.getZones();
			for (int i = 0; i < zones.length; i++)
				mBatcher.draw(GfxAssets.mAtlas.findRegion("tiles_", 123), zones[i].x, zones[i].y, zones[i].width, zones[i].height);
		}

		if (Settings.UI_DRAW_FPS)
		{
			Integer fps = Gdx.graphics.getFramesPerSecond();
			font.draw(mBatcher, "F: " + fps.toString(), 100, 470);
			font.draw(mBatcher, "T: " + Game.mTicksPerSecond.toString(), 165, 470);
			font.draw(mBatcher, "M: " + MessageContainer.mMessagesPerSecond.toString(), 560, 470);
			font.draw(mBatcher, "Id: " + RemoteConnections.mLocalID, 610, 470);

			if (Game.mIsPVPGame && RemoteConnections.mGameServer != null)
				font.draw(mBatcher, "RTT: " + RemoteConnections.mGameServer.getRTT(), 660, 470);
		}
	}

	public abstract void onPresent();

	protected abstract void onUpdate();

	protected abstract void onUpdateFinishing();

	protected abstract void onFinish();

	protected void onReset()
	{
	}
}