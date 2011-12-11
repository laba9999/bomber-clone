package com.bomber;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomber.common.Assets;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStatePlaying;
import com.bomber.renderers.WorldRenderer;
import com.bomber.world.GameWorld;

public class GameScreen implements ApplicationListener {
	final int TICKS_PER_SECOND = 25;
	final float SKIP_TICKS = 1000 / TICKS_PER_SECOND;
	final int MAX_FRAMESKIP = 5;

	public GameWorld mWorld;
	public GameState mGameState;

	public SpriteBatch mBatcher;
	public OrthographicCamera mUICamera;
	public WorldRenderer mWorldRenderer;

	int mLoops;
	float mInterpolation;
	long mNextGameTick;

	long startTime;
	static public Integer ticksPerSecond;
	int ticksPerSecondCounter;
	
	@Override
	public void create()
	{
		mUICamera = new OrthographicCamera(800, 480);
		mUICamera.position.set(800 / 2, 480 / 2, 0);
		mUICamera.update();
		
		Assets.loadAssets();
		mWorld = new GameWorld(GameType.CAMPAIGN, "level1");

		mBatcher = new SpriteBatch();
		mWorldRenderer = new WorldRenderer(mBatcher, mWorld);
		
		mGameState = new GameStatePlaying(this);
	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void render()
	{
		mLoops = 0;
		while (System.nanoTime() > mNextGameTick && mLoops < MAX_FRAMESKIP)
		{
			ticksPerSecondCounter++;
			if((System.nanoTime() - startTime) > 1000000000)
			{
				ticksPerSecond = ticksPerSecondCounter;
				ticksPerSecondCounter = 0;
				startTime = System.nanoTime();
			}
			
			mGameState.update();
			mNextGameTick += SKIP_TICKS;
			mLoops++;
		}

		mInterpolation = (System.nanoTime() + SKIP_TICKS - mNextGameTick) / SKIP_TICKS;
		mGameState.present(mInterpolation);
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}
}