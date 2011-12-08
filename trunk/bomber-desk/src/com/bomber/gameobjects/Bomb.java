package com.bomber.gameobjects;

import com.bomber.world.GameWorld;

/**
 * A bomba quando rebenta chama o metodo respectivo no GameWorld, que faz todas
 * as verificações e só depois cria a explosão.
 */
public class Bomb extends MovableObject {

	private static final int mTicksToExplode = 75; // 25/sec = 3secs;
	public short mBombPower = 3;

	public Bomb(GameWorld _world) {
		mWorld = _world;
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