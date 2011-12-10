package com.bomber.gameobjects;

import java.util.Iterator;

import com.bomber.common.Assets;
import com.bomber.common.ObjectsPool;
import com.bomber.common.Utils;
import com.bomber.gameobjects.monsters.Monster;
import com.bomber.world.GameMap;
import com.bomber.world.GameWorld;

/**
 * A bomba quando rebenta chama o metodo respectivo no GameWorld, que faz todas
 * as verificações e só depois cria a explosão.
 */
public class Bomb extends KillableObject {

	public short mBombPower;
	public Tile mContainer = null;

	private int mTicksSinceDrop = 0;
	private static final int mTicksToExplode = 1050; // 350/sec = 3secs;

	public Bomb(GameWorld _world) {
		mWorld = _world;
		mUUID = Utils.getNextUUID();
		mIgnoreDestroyables = false;
	}

	@Override
	public void update()
	{
		super.update();

		if (mTicksSinceDrop++ >= mTicksToExplode || mIsDead)
		{
			if (mIsMoving)
			{
				mContainer = mWorld.mMap.getTile(mPosition);
				mContainer.mContainsBomb = true;
			}

			mWorld.spawnExplosion(this);
		}

		if (mIsMoving)
		{
			// Verifica as colisões
			GameMap map = mWorld.mMap;
			int currentTileIdx = map.calcTileIndex(mPosition);
			int forbiddenTileIdx = map.calcTileIndex(currentTileIdx, mDirection, (short) 1);

			// Contra monstros
			if (checkCollisionsAgainstMovableObjects(mWorld.mMonsters, forbiddenTileIdx))
				return;

			// Contra outros players
			if (checkCollisionsAgainstMovableObjects(mWorld.mPlayers, forbiddenTileIdx))
				return;
		}
	}

	private <T extends MovableObject> boolean checkCollisionsAgainstMovableObjects(ObjectsPool<T> pool, int _forbiddenTileIdx)
	{
		for (MovableObject m : pool)
		{
			if (m.mIgnoreDestroyables)
				continue;

			int objTileIdx = mWorld.mMap.calcTileIndex(m.mPosition);
			if (objTileIdx == _forbiddenTileIdx)
			{
				if (m.mDirection == mDirection)
				{
					// Centra a bomba no tile em que o ocjecto com o qual
					// colidimos está actualmente
					Tile tmpTile = mWorld.mMap.getTile(mPosition);
					mPosition.set(tmpTile.mPosition.x + Tile.TILE_SIZE_HALF, tmpTile.mPosition.y + Tile.TILE_SIZE_HALF);
				} else
				{
					// Centra a bomba no tile em que está actualmente
					mPosition.set(mContainer.mPosition.x + Tile.TILE_SIZE_HALF, mContainer.mPosition.y + Tile.TILE_SIZE_HALF);
				}

				stop();
				return true;
			}
		}

		return false;
	}

	@Override
	public void reset()
	{
		super.reset();
		setMovableAnimations(Assets.mBomb);
		mPlayAnimation = true;
		mContainer = null;
		mIsDead = false;
		mTicksSinceDrop = 0;
		mSpeed = 0;
	}

	@Override
	protected void onChangedDirection()
	{
		mContainer.mContainsBomb = false;
		mSpeed = 2.5f;
	}

	@Override
	protected void onStop()
	{
		mContainer = mWorld.mMap.getTile(mPosition);
		mContainer.mContainsBomb = true;

	}

	@Override
	protected void onKill()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onMapCollision(short _collisionType)
	{
		stop();
		return true;
	}
}