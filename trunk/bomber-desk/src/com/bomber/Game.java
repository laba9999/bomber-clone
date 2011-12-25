package com.bomber;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.bomber.common.Achievements;
import com.bomber.common.ObjectFactory;
import com.bomber.common.Utils;
import com.bomber.common.assets.GfxAssets;
import com.bomber.common.assets.SoundAssets;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStateLoading;
import com.bomber.gamestates.GameStateLoadingPVP;
import com.bomber.gamestates.GameStatePaused;
import com.bomber.gamestates.GameStatePlaying;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.EventType;
import com.bomber.remote.Message;
import com.bomber.remote.MessageType;
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

	public short mRoundsToPlay;
	public short mRoundsPlayed;

	public static boolean mGameIsOver;
	public static boolean mHasStarted;

	public static int mRandomSeed;
	public static Random mRandomGenerator;

	private MessagesHandler mMessagesHandler;
	public static RemoteConnections mRemoteConnections;

	private static AndroidBridge mMainActivity;

	public static String mLevelToLoad;

	public static Team[] mTeams;

	public Game(AndroidBridge _bridge, short _gameType, String _levelToLoad) {
		setGameType(_gameType);

		Utils.resetUUID();

		GameStateLoadingPVP.mFailedToConnectToServer = false;
		mCurrentTick = 0;

		mRandomSeed = (int) System.currentTimeMillis();
		mRandomGenerator = new Random(mRandomSeed);

		mMainActivity = _bridge;
		mHasStarted = false;
		mRemoteConnections = null;
		mLevelToLoad = _levelToLoad;
		mGameIsOver = false;

		mRoundsPlayed = 1;
		mRoundsToPlay = DebugSettings.GAME_ROUNDS;

		if (mIsPVPGame)
		{
			mTeams = new Team[2];
			mTeams[0] = new Team((short) (mNumberPlayers / 2), (short) 1);
			mTeams[1] = new Team((short) (mNumberPlayers / 2), (short) 2);
		}

		mMessagesHandler = new MessagesHandler();
	}

	public void setConnections(RemoteConnections _conns)
	{
		mMessagesHandler.setConnections(_conns);
		RemoteConnections.mGame = this;
		mRemoteConnections = _conns;
	}

	public void changeInfo(short _type, short _nRounds, String _levelToLoad)
	{
		Game.setGameType(_type);
		mRoundsToPlay = _nRounds;
		mLevelToLoad = _levelToLoad;
		// Game.LOGGER.log("Received game info - Type: " + _type +
		// " - Number rounds: " + _nRounds + " - level: " + mLevelToLoad);

		mTeams[0].clear();
		mTeams[1].clear();

		mTeams[0].mNumberPlayers = (short) (mNumberPlayers / 2);
		mTeams[1].mNumberPlayers = (short) (mNumberPlayers / 2);

		mWorld = new GameWorld(this, ObjectFactory.CreateGameTypeHandler.Create(mGameType), mLevelToLoad);
		mWorldRenderer = new WorldRenderer(mBatcher, mWorld);

		mMessagesHandler.mWorld = mWorld;
	}

	public void updateRandomSeed(int _newSeed)
	{
		Game.mRandomSeed = _newSeed;
		Game.mRandomGenerator = new Random(Game.mRandomSeed);

		mWorld.reset(Game.mLevelToLoad);
		mWorld.setLocalPlayer(RemoteConnections.mLocalID);

		if (RemoteConnections.mIsGameServer)
		{
			// Envia uma mensagem de SYNC para a seed
			Message tmpMessage = Game.mRemoteConnections.mMessageToSend;

			tmpMessage.messageType = MessageType.GAME;
			tmpMessage.eventType = EventType.RANDOM_SEED;
			tmpMessage.valInt = Game.mRandomSeed;

			mRemoteConnections.broadcast(tmpMessage);
		}
	}

	public GameState getGameState()
	{
		return mGameState;
	}

	public static void goBackToActivities()
	{
		if (mMainActivity != null)
		{
			SoundAssets.stop();
			mMainActivity.goBackToMenu();
		} else
		{
			System.exit(-1);
		}
	}

	public static void goToHelpActivity()
	{
		if (mMainActivity != null)
		{
			mMainActivity.showHelpActivity();
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

		if (!SoundAssets.mIsloaded)
			SoundAssets.load();

		GfxAssets.loadAssets();

		if (!mIsPVPGame)
		{
			mGameState = new GameStateLoading(this);
			SoundAssets.play(Game.mLevelToLoad, true, 1.0f);
		} else
			mGameState = new GameStateLoadingPVP(this);

		if (Game.mIsPVPGame && RemoteConnections.mIsGameServer)
			changeInfo(DebugSettings.GAME_TYPE, DebugSettings.GAME_ROUNDS, DebugSettings.LEVEL_TO_LOAD);
		else if (mGameType == GameTypeHandler.CAMPAIGN)
		{
			mWorld = new GameWorld(this, ObjectFactory.CreateGameTypeHandler.Create(mGameType), mLevelToLoad);
			mWorldRenderer = new WorldRenderer(mBatcher, mWorld);

			mMessagesHandler.mWorld = mWorld;
		}

		mMessagesHandler.mGame = this;

		if (!RemoteConnections.mIsGameServer)
		{
			Message msg = mRemoteConnections.mMessageToSend;
			msg.messageType = MessageType.CONNECTION;
			msg.eventType = EventType.READY;
			mRemoteConnections.mGameServer.sendMessage(msg);
		}

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

			if (mIsPVPGame && mRemoteConnections != null && !mGameIsOver)
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
		Achievements.saveFile();

		SoundAssets.pause();

		if (mIsPVPGame)
			goBackToActivities();

		if (mGameState instanceof GameStatePlaying)
			setGameState(new GameStatePaused(this));

		GfxAssets.DarkGlass.dispose();
	}

	@Override
	public void resume()
	{
		mNextGameTick = System.nanoTime();
		SoundAssets.resume();
	}

	@Override
	public void dispose()
	{

	}
}