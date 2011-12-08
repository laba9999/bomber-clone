package com.bomber.gameobjects.monsters;

import com.bomber.common.Utils;
import com.bomber.gameobjects.KillableObject;
import com.bomber.world.GameWorld;

public class Monster extends KillableObject {

	public MonsterInfo mInfo;

	public Monster(GameWorld _world) {
		mWorld = _world;
		mUUID = Utils.getNextUUID();
	}

	@Override
	public void update()
	{
		super.update();

		// Verifica se o monstro está morto
		if (mIsDead)
		{
			if (mLooped)
				mWorld.mMonsters.releaseObject(this);

			return;
		}

		// TODO: Movimento
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
		// TODO Auto-generated method stub

	}
}