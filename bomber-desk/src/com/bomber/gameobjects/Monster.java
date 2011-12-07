package com.bomber.gameobjects;

import com.bomber.world.GameWorld;

public abstract class Monster extends KillableObject {
	private int mPointsValue;
	private short mMonsterType;
	public GameWorld mWorld;

	@Override
	public void update()
	{
		super.update();
		
		// Verifica se o monstro está morto
		if (mIsDead && mLooped)
			mWorld.mMonsters.releaseObject(this);
	}

	public abstract void interactWithPlayer(Player _player);
}