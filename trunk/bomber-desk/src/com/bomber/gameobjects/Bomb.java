package com.bomber.gameobjects;

import com.bomber.common.Assets;
import com.bomber.common.Utils;
import com.bomber.world.GameWorld;

/**
 * A bomba quando rebenta chama o metodo respectivo no GameWorld, que faz todas
 * as verificações e só depois cria a explosão.
 */
public class Bomb extends MovableObject {

	private static final int mTicksToExplode = 1000; // 25/sec = 3secs;
	public short mBombPower = 3;
	public Tile mContainer = null;
	public Bomb(GameWorld _world) {
		mWorld = _world;
		mUUID = Utils.getNextUUID();
		
	}

	@Override
	public void update()
	{
		super.update();

		if (mAnimationTicks >= mTicksToExplode)
			mWorld.createExplosion(this);
	}

	@Override
	public void reset()
	{
		super.reset();
		setCurrentAnimation(Assets.mBomb, (short) 3, true);
		mContainer=null;
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