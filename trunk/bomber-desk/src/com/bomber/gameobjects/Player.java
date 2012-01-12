package com.bomber.gameobjects;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bomber.Game;
import com.bomber.Team;
import com.bomber.common.Achievements;
import com.bomber.common.Collision;
import com.bomber.common.Directions;
import com.bomber.common.ObjectFactory;
import com.bomber.common.ObjectsPool;
import com.bomber.common.PlayerEffect;
import com.bomber.common.assets.SoundAssets;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.gameobjects.bonus.BonusExplosionSize;
import com.bomber.gameobjects.bonus.BonusShield;
import com.bomber.gameobjects.bonus.BonusSpeed;
import com.bomber.gameobjects.bonus.TemporaryBonus;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.EventType;
import com.bomber.remote.Message;
import com.bomber.remote.MessageType;
import com.bomber.remote.RemoteConnections;
import com.bomber.world.GameWorld;

public class Player extends KillableObject {
	private static HashMap<String, Short> COLORS = null;
	public static final short WHITE = 0;
	public static final short RED = 1;
	public static final short BLUE = 2;
	public static final short GREEN = 3;

	public static final float MAX_SPEED = 2;

	public static final short MAX_EXPLOSION_SIZE = 6;
	public static final short MAX_SPEED_FACTOR = 4;

	public static final short SPAWN_IMMUNITY_TICKS = 300;
	public static final short PLAYER_BLINK_SPEED = 10;

	public Map<String, TextureRegion> mExtraTextures;

	public int mPoints = 0;
	public int mStartLevelPoints = 0;
	private int mLastTickPoints = -1;

	public String mName;
	private String mPointsAsString;

	public short mLives = 3;
	public short mPointsMultiplier = 1;
	public short mBombExplosionSize = 1;
	public short mMaxSimultaneousBombs = 1;
	public short mSpeedFactor = 1;
	public short mColor;
	
	public short mPlacedBombsCount = 0;

	public boolean mIsConnected = true;
	public boolean mIsShieldActive = false;
	public boolean mIsAbleToPushBombs = false;

	public Vector2 mSpawnPosition = new Vector2();

	public Flag mEnemyFlag = null;
	public Team mTeam = null;

	public boolean mIsLocalPlayer = false;
	private short mTicksSinceSpawn;
	private short mLastDirectionSent = -1;
	private boolean mMovedSinceLastStop = true;

	/**
	 * Inicializado com o máximo de bonus que podem estar activos ao mesmo
	 * tempo, 3.
	 */
	public ObjectsPool<TemporaryBonus> mActiveBonus;

	/**
	 * Efeitos que devem ser desenhados por cima do jogador, shield e splash da
	 * água...
	 */
	public ObjectsPool<PlayerEffect> mEffects;

	//private RemoteConnections mRemoteConnections = Game.mRemoteConnections;

	public Player(GameWorld _world) {
		mWorld = _world;

		mEffects = new ObjectsPool<PlayerEffect>((short) 0, null);
		mActiveBonus = new ObjectsPool<TemporaryBonus>((short) 0, null);
		mExtraTextures = new HashMap<String, TextureRegion>(3);
		
		mSpeed = 1.7f;
	}

	public void dropBomb()
	{
		if(mPlacedBombsCount >=mMaxSimultaneousBombs)
			return;
		
		mPlacedBombsCount++;
		
		mWorld.spawnBomb(mColor, mBombExplosionSize, mPosition);

		if (Game.mRemoteConnections == null || !mIsLocalPlayer)
			return;

		Message tmpMessage = Game.mRemoteConnections.mMessageToSend;
		tmpMessage.messageType = MessageType.BOMB;
		tmpMessage.eventType = EventType.CREATE;
		tmpMessage.valVector2_0.set(mPosition);
		tmpMessage.valShort = mColor;
		tmpMessage.valShort = mBombExplosionSize;

		Game.mRemoteConnections.broadcast(tmpMessage);
	}

	public boolean isImmune()
	{
		return mTicksSinceSpawn <= SPAWN_IMMUNITY_TICKS;
	}

	public String getPointsAsString()
	{
		if (mPoints == mLastTickPoints)
			return mPointsAsString;

		String points = new Integer(mPoints).toString();
		StringBuilder ret = new StringBuilder();

		for (int i = points.length(); i < 6; i++)
			ret.append(0);

		ret.append(points);

		mPointsAsString = ret.toString();
		return mPointsAsString;
	}

	public static short getColorFromString(String _key)
	{
		if (COLORS == null)
		{
			COLORS = new HashMap<String, Short>();

			COLORS.put("b_white", WHITE);
			COLORS.put("b_blue", BLUE);
			COLORS.put("b_green", GREEN);
			COLORS.put("b_red", RED);
		}

		return COLORS.get(_key);
	}

	@Override
	public void update()
	{
		super.update();

		if (mIsDead)
		{
			if (mLooped && mLives > 0 && mIsConnected)
			{
				mIsDead = false;
				mPosition.set(mSpawnPosition);
				changeDirection(Directions.DOWN);
				mTicksSinceSpawn = 0;
				mIsMoving = false;
				stopCurrentAnimation();
				mAcceptPlayerInput = true;
			} else if (mLooped && mLives == 0)
				mWorld.mPlayers.releaseObject(this);

			return;
		}

		mTicksSinceSpawn++;

		// Verifica se colidiu com algum bónus
		checkBonusCollision();

		// Verifica se apanhou algum bónus
		checkFlagCollision();

		// Actualiza os bonus activos
		for (Bonus b : mActiveBonus)
			b.update();

		// Actualiza os efeitos activos
		for (PlayerEffect ef : mEffects)
		{
			ef.update();
			ef.mPosition = mPosition;
		}
	}

	private void checkFlagCollision()
	{
		if (Game.mGameType != GameTypeHandler.CTF && Game.mGameType != GameTypeHandler.TEAM_CTF)
			return;

		Rectangle bb = getBoundingBox();

		for (int i = 0; i < 2; i++)
		{
			Flag flag = mWorld.mFlags[i];

			if (flag.mTransporter != null)
				continue;

			boolean isEnemyFlag = !Game.mTeams[flag.mBelongsToTeamId].mPlayers.contains(this);
			if (!isEnemyFlag)
				continue;

			if (bb.contains(flag.mPosition.x + Tile.TILE_SIZE_HALF, flag.mPosition.y + Tile.TILE_SIZE_HALF))
			{
				flag.mTransporter = this;
				flag.update();
				mEnemyFlag = flag;
				mTeam.mTransportingEnemyFlag = true;
				break;
			}
		}
	}

	private void checkBonusCollision()
	{
		Rectangle playerRectangle = getBoundingBox();
		for (Bonus bonus : mWorld.mSpawnedBonus)
		{
			Rectangle bonusRectangle = bonus.getBoundingBox();

			float bonusX = bonusRectangle.x + Tile.TILE_SIZE_HALF;
			float bonusY = bonusRectangle.y + Tile.TILE_SIZE_HALF;

			if (playerRectangle.contains(bonusX, bonusY))
			{
				mWorld.mSpawnedBonus.releaseObject(bonus);

				if (!reachedMaxBonusType(bonus))
					bonus.applyEffect(this);

				// Se não é temporario apenas aplica o efeito
				if (!TemporaryBonus.class.isAssignableFrom(bonus.getClass()))
					continue;

				// Para jogar pelo seguro clonamos o abjecto antes de o
				// adicionar a uma pool diferente
				TemporaryBonus tmpBonus = (TemporaryBonus) ObjectFactory.CreateBonus.create(((TemporaryBonus) bonus).mType);
				bonus.clone(tmpBonus);

				mActiveBonus.addObject(tmpBonus);
			}
		}
	}

	private <T extends Bonus> boolean reachedMaxBonusType(T _bonus)
	{
		if (_bonus instanceof BonusExplosionSize)
		{
			if (mBombExplosionSize >= MAX_EXPLOSION_SIZE)
				return true;
		} else if (_bonus instanceof BonusSpeed)
		{
			if (mSpeedFactor >= MAX_SPEED_FACTOR)
				return true;
		} 

		return false;
	}

	/**
	 * É utilizado pela ObjectPool quando o objecto é marcado como disponivel.
	 * Este método deve ser sempre chamar o seu super antes/depois de efectuar
	 * mudanças numa classe derivada
	 * 
	 * mIsBeingDestroyed = false;
	 */
	public void reset()
	{
		super.reset();

		mPoints = 0;
		mEffects.clear();
		mActiveBonus.clear();

		mIsShieldActive = false;
		mIsAbleToPushBombs = false;

		mLives = 3;
		mPointsMultiplier = 1;

		mBombExplosionSize = 1;
		mMaxSimultaneousBombs = 1;
		mSpeedFactor = 1;
		mPlacedBombsCount = 0;
		
		mTicksSinceSpawn = 0;
		mIsLocalPlayer = false;

		mIsConnected = true;
		mExtraTextures.clear();

		mAcceptPlayerInput = true;
		mEnemyFlag = null;
		mTeam = null;
		
		mSpeed = 1.7f;
	}

	public String getPoints()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean onKill(short _killerId)
	{
		if (mTicksSinceSpawn <= SPAWN_IMMUNITY_TICKS)
			return true;

		if (mIsShieldActive)
		{
			for (TemporaryBonus b : mActiveBonus)
			{
				if (b instanceof BonusShield)
				{
					b.removeEffect();
					break;
				}
			}
			return true;

		} else if (!mIsLocalPlayer && _killerId == mWorld.getLocalPlayer().mColor)
		{
			mWorld.getLocalPlayer().mPoints += 500;
			mWorld.spawnOverlayingPoints("+500", mPosition.x, mPosition.y + Tile.TILE_SIZE_HALF);
			Achievements.mNumberPlayersKills++;
		}

		
		if (mIsLocalPlayer)
			SoundAssets.playSound("die");

		mAcceptPlayerInput = false;
		mWorld.mGameTypeHandler.onPlayerKill(this);

		return false;
	}

	@Override
	protected void onChangedDirection()
	{

		mMovedSinceLastStop = true;

		if (Game.mRemoteConnections == null || !mIsLocalPlayer || mDirection == mLastDirectionSent  || !mAcceptPlayerInput)
			return;
		
		mLastDirectionSent = mDirection;

		Message tmpMessage = Game.mRemoteConnections.mMessageToSend;
		tmpMessage.messageType = MessageType.PLAYER;
		tmpMessage.eventType = EventType.MOVE;
		tmpMessage.valVector2_0.set(mPosition);
		tmpMessage.valShort = mDirection;
		tmpMessage.UUID = mUUID;

		Game.mRemoteConnections.broadcast(tmpMessage);
	}

	@Override
	protected void onStop()
	{
		if (!mIsDead)
			stopCurrentAnimation();

		if (Game.mRemoteConnections == null || !mIsLocalPlayer || !mMovedSinceLastStop || !mAcceptPlayerInput)
			return;

		mMovedSinceLastStop = false;
		mLastDirectionSent = -1;

		Message tmpMessage = Game.mRemoteConnections.mMessageToSend;
		tmpMessage.messageType = MessageType.PLAYER;
		tmpMessage.eventType = EventType.STOP;
		tmpMessage.valVector2_0.set(mPosition);
		tmpMessage.valShort = mDirection;
		tmpMessage.UUID = mUUID;

		Game.mRemoteConnections.broadcast(tmpMessage);
	}

	@Override
	protected void onMapCollision(short _collisionType)
	{
		if (_collisionType == Collision.BOMB)
		{
			if (mIsAbleToPushBombs)
				pushBombAhead();
		}
	}

	private void pushBombAhead()
	{

		int whereBombShouldBe = mWorld.mMap.calcTileIndex(mPosition, mDirection, (short) 1);
		for (Bomb bomb : mWorld.mBombs)
			if (whereBombShouldBe == bomb.mContainer.mPositionInArray)
				bomb.changeDirection(mDirection);
	}
}