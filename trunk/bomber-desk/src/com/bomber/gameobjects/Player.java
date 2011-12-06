package com.bomber.gameobjects;

import com.bomber.common.ObjectsPool;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.world.GameWorld;

public abstract class Player extends KillableObject {
	public short mPointsMultiplier = 1;
	public int mPoints = 0;
	public String mPointsAsString;
	public boolean mIsShieldActive = false;
	public short mLives = 1;
	public short mBombExplosionSize = 3;
	public short mMaxSimultaneousBombs = 1;
	public boolean mIsAbleToPushBombs = false;
	public short mSpeedFactor = 1;
	/**
	 * Inicializado com o máximo de bonus que podem estar activos ao mesmo
	 * tempo, 3.
	 */
	public ObjectsPool<Bonus> mActiveBonus;
	public String mName;
	/**
	 * Efeitos que devem ser desenhados por cima do jogador, bonus e splash da
	 * água...
	 */
	public ObjectsPool<Drawable> mEffects;
	public GameWorld mWorld;

	public abstract void update();

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

	/**
	 * Baseado no deviceID
	 */
	@Override
	public abstract boolean equals(Object _rhs);

	protected abstract void onChangedDirection();

	protected abstract void onStop();

	protected abstract void onKill();
}