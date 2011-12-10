package com.bomber.gameobjects;

import java.util.HashMap;

import com.badlogic.gdx.math.Rectangle;
import com.bomber.common.Collision;
import com.bomber.common.Directions;
import com.bomber.common.ObjectsPool;
import com.bomber.common.Utils;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.world.GameWorld;

public class Player extends KillableObject {
	private static HashMap<String, Short> COLORS = null;
	public static final short WHITE = 0;
	public static final short BLUE = 1;
	public static final short GREEN = 2;
	public static final short RED = 3;

	public int mPoints = 0;

	public String mName;
	public String mPointsAsString;

	public short mLives = 1;
	public short mPointsMultiplier = 1;
	public short mBombExplosionSize = 1;
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

		mEffects = new ObjectsPool<Drawable>((short) 0, null);
		mActiveBonus = new ObjectsPool<Bonus>((short) 0, null);
	}

	public void dropBomb()
	{
		 mWorld.spawnBomb(mBombExplosionSize, mPosition);

	}

	public static short getColorFromString(String _key)
	{
		if (COLORS == null)
		{
			COLORS = new HashMap<String, Short>();

			COLORS.put("b_white", (short) 0);
			COLORS.put("b_blue", (short) 1);
			COLORS.put("b_green", (short) 2);
			COLORS.put("b_red", (short) 3);

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

		// Verifica se colidiu com algum bónus
		checkBonusCollision();

		// Actualiza os bonus activos
		for (Bonus b : mActiveBonus)
			b.update();
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
				bonus.applyEffect(this);
				mWorld.mSpawnedBonus.releaseObject(bonus);
				mActiveBonus.addObject(bonus);
			}

		}
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
		mPoints = 0;
		mEffects.clear();
		mActiveBonus.clear();

		mIsShieldActive = false;
		mIsAbleToPushBombs = false;

		mLives = 1;
		mPointsMultiplier = 1;
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
		if (!mIsDead)
			stopCurrentAnimation();
	}

	@Override
	protected boolean onMapCollision(short _collisionType)
	{
		if (_collisionType == Collision.BOMB)
		{
			// TODO : descomentar:
			// if(mIsAbleToPushBombs)
			// {

					pushBombAhead();
			// }
		}
		// TODO Auto-generated method stub
		return true;
	}

	private void pushBombAhead()
	{
		Rectangle whereBombShouldBe = getBoundingBox();

		switch (mDirection)
		{
		case Directions.UP:
			whereBombShouldBe.setY(whereBombShouldBe.getY() + Tile.TILE_SIZE);
			break;
		case Directions.DOWN:
			whereBombShouldBe.setY(whereBombShouldBe.getY() - Tile.TILE_SIZE);
			break;
		case Directions.LEFT:
			whereBombShouldBe.setX(whereBombShouldBe.getX() - Tile.TILE_SIZE);
			break;
		case Directions.RIGHT:
			whereBombShouldBe.setX(whereBombShouldBe.getX() + Tile.TILE_SIZE);
			break;
		}

		for (Bomb bomb : mWorld.mBombs)
		{
			float x = bomb.mPosition.x;
			float y = bomb.mPosition.y;

			if (whereBombShouldBe.contains(x, y))
			{
				switch (mDirection)
				{
				case Directions.UP:
					bomb.moveUp();
					break;
				case Directions.DOWN:
					bomb.moveDown();
					break;
				case Directions.LEFT:
					bomb.moveLeft();
					break;
				case Directions.RIGHT:
					bomb.moveRight();
					break;
				}

			}
		}
	}

}