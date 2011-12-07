package com.bomber.gameobjects;

import com.bomber.world.GameWorld;

/**
 * A bomba quando rebenta chama o metodo respectivo no GameWorld, que faz todas
 * as verificações e só depois cria a explosão.
 */
public  class Bomb extends MovableObject {
	private int mTicksUntilExplosion;
	private short mBombPower = 3;
	public GameWorld mWorld;

	@Override
	public void update()
	{
		super.update();
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