package com.bomber;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.bomber.common.Assets;
import com.bomber.common.ObjectFactory;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStateLoading;
import com.bomber.gamestates.GameStatePaused;
import com.bomber.gamestates.GameStatePlaying;
import com.bomber.gametypes.GameTypeCampaign;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.MessagesHandler;
import com.bomber.remote.RemoteConnections;
import com.bomber.renderers.WorldRenderer;
import com.bomber.world.GameWorld;

public class Game implements ApplicationListener {
	private static final short MAX_PARSED_MESSAGES_PER_TICK = 2;
	private static final int MAX_FRAMESKIP = 5;
	public static final int TICKS_PER_SECOND = 100;
	public static final long SKIP_TICKS = 1000000000 / TICKS_PER_SECOND;

	public static long mCurrentTick = 0;
	public static Integer mTicksPerSecond = 100;

	public static Logger LOGGER = new Logger("GAM");
	private int mLoops;
	private long startTime;
	private float mInterpolation;
	private int ticksPerSecondCounter;

	public static short mGameType = -1;
	public static boolean mIsPVPGame = false;
	public static short mNumberPlayers = 0;
	public static long mNextGameTick;

	public GameWorld mWorld;
	public SpriteBatch mBatcher;
	public OrthographicCamera mUICamera;
	public WorldRenderer mWorldRenderer;

	private GameState mGameState;
	private long mLastGameStateChangeTime = System.currentTimeMillis();

	private MessagesHandler mMessagesHandler;
	public static boolean mHasStarted;
	public static RemoteConnections mRemoteConnections;
	public static Random mRandomGenerator;
	public static int mRandomSeed;
	private static AndroidBridge mMainActivity;

	public static String mLevelToLoad;

	public Game(AndroidBridge _bridge, short _gameType, String _levelToLoad) {
		setGameType(_gameType);

		mRandomSeed = (int) System.currentTimeMillis();
		mRandomGenerator = new Random(mRandomSeed);

		mMainActivity = _bridge;
		mHasStarted = false;
		mRemoteConnections = null;
		mLevelToLoad = _levelToLoad;

		mMessagesHandler = new MessagesHandler();
	}

	public void setConnections(RemoteConnections _conns)
	{
		mMessagesHandler.setConnections(_conns);
		RemoteConnections.mGame = this;
		mRemoteConnections = _conns;
	}

	public GameState getGameState()
	{
		return mGameState;
	}

	public static void goBackToActivities()
	{
		if (mMainActivity != null)
		{
			mMainActivity.goBackToMenu();
		} else
		{
			System.exit(-1);
		}
	}

	// A chamar antes de instanciar o objecto
	public static void setGameType(short _type)
	{
		mGameType = _type;

		switch (_type)
		{
		case GameTypeHandler.CAMPAIGN:
			mIsPVPGame = false;
			mNumberPlayers = 1;
			break;

		case GameTypeHandler.CTF:
		case GameTypeHandler.DEADMATCH:
			mIsPVPGame = true;
			mNumberPlayers = 2;
			break;

		case GameTypeHandler.TEAM_CTF:
		case GameTypeHandler.TEAM_DEADMATCH:
			mIsPVPGame = true;
			mNumberPlayers = 4;
			break;
		}
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

		mGameState = new GameStateLoading(this);
		mWorld = new GameWorld(new GameTypeCampaign(), mLevelToLoad);
		// mWorld = new
		// GameWorld(ObjectFactory.CreateGameTypeHandler.Create(mGameType),
		// mLevelToLoad);
		mWorldRenderer = new WorldRenderer(mBatcher, mWorld);

		mMessagesHandler.mWorld = mWorld;
		// mGameState = new GameStatePlaying(this);

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

			if (mIsPVPGame && mRemoteConnections != null)
			{
				mRemoteConnections.update();

				for (short i = 0; i < MAX_PARSED_MESSAGES_PER_TICK; i++)
					mMessagesHandler.parseNextMessage();
			}

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