package com.bomber;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.bomber.common.Assets;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStatePaused;
import com.bomber.gamestates.GameStatePlaying;
import com.bomber.gametypes.GameTypeCampaign;
import com.bomber.remote.MessagesHandler;
import com.bomber.remote.RemoteConnections;
import com.bomber.renderers.WorldRenderer;
import com.bomber.world.GameWorld;

public class Game implements ApplicationListener {
	private static final short MAX_PARSED_MESSAGES_PER_TICK = 2;
	private static final int MAX_FRAMESKIP = 5;
	public static final int TICKS_PER_SECOND = 100;
	private static final long SKIP_TICKS = 1000000000 / TICKS_PER_SECOND;

	public static long mCurrentTick = 0;
	public static Integer mTicksPerSecond = 100;

	public static Logger LOGGER = new Logger("TAG");
	private int mLoops;
	private long startTime;
	private long mNextGameTick;
	private float mInterpolation;
	private int ticksPerSecondCounter;

	public GameWorld mWorld;
	public SpriteBatch mBatcher;
	public OrthographicCamera mUICamera;
	public WorldRenderer mWorldRenderer;

	public GameState mGameState;
	private long mLastGameStateChangeTime = System.currentTimeMillis();

	private MessagesHandler mMessagesHandler;
	public static RemoteConnections mRemoteConnections;

	public Game(RemoteConnections _connections) {
		mRemoteConnections = _connections;
	}

	public GameState getGameState()
	{
		return mGameState;
	}

	public void goBackToActivities()
	{
		// TODO : implementar voltar às actividades do android
		throw new UnsupportedOperationException();
	}

	public void setGameState(GameState _newGameState)
	{
		if (System.currentTimeMillis() - mLastGameStateChangeTime < 250)
			return;

		mGameState = _newGameState;

		mGameState.reset();
		mLastGameStateChangeTime = System.currentTimeMillis();
	}

	@Override
	public void create()
	{
		mUICamera = new OrthographicCamera(800, 480);
		mUICamera.position.set(800 / 2, 480 / 2, 0);
		mUICamera.update();

		mBatcher = new SpriteBatch();

		Assets.loadAssets();

		//mGameState = new GameStateLoading(this);

		mWorld = new GameWorld(new GameTypeCampaign(), "level1");
		mWorldRenderer = new WorldRenderer(mBatcher, mWorld);
		mMessagesHandler = new MessagesHandler(mRemoteConnections, mWorld);

		mGameState = new GameStatePlaying(this);

		mNextGameTick = System.nanoTime();
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
			if ((System.nanoTime() - startTime) > 1000000000)
			{
				mTicksPerSecond = ticksPerSecondCounter;
				ticksPerSecondCounter = 0;
				startTime = System.nanoTime();
			}

			mGameState.update();
			mRemoteConnections.update();
			
			for (short i = 0; i < MAX_PARSED_MESSAGES_PER_TICK; i++)
				mMessagesHandler.parseNextMessage();

			mNextGameTick += SKIP_TICKS;
			mLoops++;

			mCurrentTick++;
		}

		mInterpolation = (System.nanoTime() + SKIP_TICKS - mNextGameTick) / SKIP_TICKS;
		mGameState.present(mInterpolation);

	}

	@Override
	public void pause()
	{
		if (mGameState instanceof GameStatePlaying)
			setGameState(new GameStatePaused(this));

		Assets.DarkGlass.dispose();
	}

	@Override
	public void resume()
	{
		mNextGameTick = System.nanoTime();
	}

	@Override
	public void dispose()
	{

	}
}