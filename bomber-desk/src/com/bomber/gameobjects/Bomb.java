package com.bomber.gameobjects;

import com.bomber.common.Assets;
import com.bomber.common.Utils;
import com.bomber.world.GameWorld;

/**
 * A bomba quando rebenta chama o metodo respectivo no GameWorld, que faz todas
 * as verificações e só depois cria a explosão.
 */
public class Bomb extends KillableObject {

	private static final int mTicksToExplode = 1050; // 350/sec = 3secs;
	public short mBombPower;
	public Tile mContainer = null;

	public Bomb(GameWorld _world) {
		mWorld = _world;
		mUUID = Utils.getNextUUID();
	}

	@Override
	public void update()
	{
		super.update();

		if (mAnimationTicks >= mTicksToExplode || mIsDead)
			mWorld.spawnExplosion(this);
	}

	@Override
	public void reset()
	{
		super.reset();
		setMovableAnimations(Assets.mBomb);
		mPlayAnimation = true;
		mContainer = null;
		mIsDead = false;
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

	@Override
	protected void onKill()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean onMapCollision(short _collisionType)
	{
		// TODO Auto-generated method stub
		return false;
	}

}