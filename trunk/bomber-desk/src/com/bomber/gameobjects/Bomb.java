package com.bomber.gameobjects;

import com.bomber.common.Directions;
import com.bomber.common.ObjectsPool;
import com.bomber.common.assets.GfxAssets;
import com.bomber.world.GameMap;
import com.bomber.world.GameWorld;

/**
 * A bomba quando rebenta chama o metodo respectivo no GameWorld, que faz todas
 * as verificações e só depois cria a explosão.
 */
public class Bomb extends KillableObject {

	public short mBombPower;
	public short mDropedBy;
	public Tile mContainer = null;

	private int mTicksSinceDrop = 0;

	private static final int mTicksToExplode = 5; // 100/sec = 3secs;

	public Bomb(GameWorld _world) {
		mWorld = _world;
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

			for (Player p : mWorld.mPlayers)
			{
				if (p.mColor == mDropedBy)
				{
					p.mPlacedBombsCount--;
					break;
				}
			}
			mWorld.spawnExplosion(this);
		}

		if (mIsMoving)
			checkCollisions();

	}

	private boolean checkCollisions()
	{
		// Verifica as colisões
		GameMap map = mWorld.mMap;
		int currentTileIdx = map.calcTileIndex(mPosition);
		int forbiddenTileIdx = map.calcTileIndex(mPosition, mDirection, (short) 1);

		// Contra monstros
		if (checkCollisionsAgainstMovableObjects(mWorld.mMonsters, currentTileIdx, forbiddenTileIdx))
			return true;

		// Contra outros players
		if (checkCollisionsAgainstMovableObjects(mWorld.mPlayers, currentTileIdx, forbiddenTileIdx))
			return true;

		return false;
	}

	private <T extends KillableObject> boolean checkCollisionsAgainstMovableObjects(ObjectsPool<T> pool, int _currentTileIdx, int _forbiddenTileIdx)
	{
		for (KillableObject m : pool)
		{
			if (m.mIgnoreDestroyables || m.mIsDead)
				continue;

			int objTileIdx = mWorld.mMap.calcTileIndex(m.mPosition);
			if (objTileIdx == _forbiddenTileIdx || objTileIdx == _currentTileIdx)
			{
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
		setMovableAnimations(GfxAssets.mBomb);
		mPlayAnimation = true;
		mContainer = null;
		mIsDead = false;
		mTicksSinceDrop = 0;
		mDirection = Directions.NONE;
		mSpeed = 0;
	}

	@Override
	protected void onChangedDirection()
	{
		// De certeza que pode andar??
		if (checkCollisions())
			return;

		mContainer.mContainsBomb = false;
		mSpeed = 3.5f;
	}

	@Override
	protected void onStop()
	{
		mContainer = mWorld.mMap.getTile(mPosition);
		mContainer.mContainsBomb = true;

		// Centra a bomba no tile em que está actualmente
		mPosition.set(mContainer.mPosition.x + Tile.TILE_SIZE_HALF, mContainer.mPosition.y + Tile.TILE_SIZE_HALF);
	}

	@Override
	protected boolean onKill(short _killerId)
	{
		return false;
	}

	@Override
	protected void onMapCollision(short _collisionType)
	{
		stop();
	}
}