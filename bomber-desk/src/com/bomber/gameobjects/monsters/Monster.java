package com.bomber.gameobjects.monsters;

import com.bomber.gameobjects.KillableObject;
import com.bomber.gameobjects.Player;
import com.bomber.world.GameWorld;

public abstract class Monster extends KillableObject {
	private int mPointsValue;
	private short mMonsterType;

	Monster(GameWorld _world) {
		mWorld = _world;
	}

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