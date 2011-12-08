package com.bomber.gameobjects;

import java.util.HashMap;

import com.bomber.common.ObjectsPool;
import com.bomber.common.Utils;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.world.GameWorld;

public class Player extends KillableObject {
	public static HashMap<String, Short> COLORS = null;
	public static final short WHITE = 0;
	public static final short BLUE = 1;
	public static final short GREEN = 2;
	public static final short RED = 3;

	public int mPoints = 0;

	public String mName;
	public String mPointsAsString;

	public short mLives = 1;
	public short mSpeedFactor = 1;
	public short mPointsMultiplier = 1;
	public short mBombExplosionSize = 3;
	public short mMaxSimultaneousBombs = 1;
	public short mColor;

	public boolean mIsShieldActive = false;
	public boolean mIsAbleToPushBombs = false;

	/**
	 * Inicializado com o máximo de bonus que podem estar activos ao mesmo
	 * tempo, 3.
	 */
	public ObjectsPool<Bonus> mActiveBonus;
	/**
	 * Efeitos que devem ser desenhados por cima do jogador, bonus e splash da
	 * água...
	 */
	public ObjectsPool<Drawable> mEffects;

	public Player(GameWorld _world) {
		mWorld = _world;
		mUUID = Utils.getNextUUID();
	}

	public static short getColorFromString(String _key)
	{
		if (COLORS == null)
		{
			COLORS = new HashMap<String, Short>();

			COLORS.put("white/b_white", (short) 0);
			COLORS.put("blue/b_blue", (short) 1);
			COLORS.put("green/b_green", (short) 2);
			COLORS.put("red/b_red", (short) 3);

		}

		return COLORS.get(_key);
	}

	@Override
	public void update()
	{
		super.update();

		if (mIsDead)
		{
			if (mLooped)
				mWorld.mPlayers.releaseObject(this);
			return;
		}

		if (!mIsMoving)
			return;

		// Executa o movimento
		move(mSpeed * mSpeedFactor);
		checkTileCollisions(false);
		checkBombCollisions();
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
		throw new UnsupportedOperationException();
	}

	public String getPoints()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onKill()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onChangedDirection()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub

	}
}