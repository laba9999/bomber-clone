package com.bomber.gameobjects;

import com.bomber.common.ObjectsPool;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.world.GameWorld;

public class Player extends KillableObject {

	public int mPoints = 0;

	public String mName;
	public String mPointsAsString;

	public short mLives = 1;
	public short mSpeedFactor = 1;
	public short mPointsMultiplier = 1;
	public short mBombExplosionSize = 3;
	public short mMaxSimultaneousBombs = 1;

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