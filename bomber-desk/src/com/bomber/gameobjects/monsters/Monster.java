package com.bomber.gameobjects.monsters;

import java.util.Random;

import com.bomber.common.Directions;
import com.bomber.common.Utils;
import com.bomber.gameobjects.KillableObject;
import com.bomber.world.GameWorld;

public class Monster extends KillableObject
{

	public MonsterInfo mInfo;

	public Monster(GameWorld _world)
	{
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

		
		// Efectua movimento
		move(mSpeed);
						
		checkTileCollisions(mInfo.mAbleToFly);
		//checkBombCollisions();

		
		// verifica se colidiu
		if (mJustCollided)
		{
			mDirection = getAnyOtherDirection(mDirection);
		}

	

	}

	private short getAnyOtherDirection(short _actualDirection)
	{

		Random randomGenerator = new Random();
		short randomValue = (short) randomGenerator.nextInt(3);

		short newDirection = 0;

		switch (mDirection)
		{
		case Directions.DOWN:
			switch (randomValue)
			{
			case 0:
				newDirection = Directions.UP;
				break;
			case 1:
				newDirection = Directions.LEFT;
				break;
			case 2:
				newDirection = Directions.RIGHT;
				break;
			}
			break;
		case Directions.UP:
			switch (randomValue)
			{
			case 0:
				newDirection = Directions.DOWN;
				break;
			case 1:
				newDirection = Directions.LEFT;
				break;
			case 2:
				newDirection = Directions.RIGHT;
				break;
			}
			break;
		case Directions.LEFT:
			switch (randomValue)
			{
			case 0:
				newDirection = Directions.UP;
				break;
			case 1:
				newDirection = Directions.DOWN;
				break;
			case 2:
				newDirection = Directions.RIGHT;
				break; 
			}
			break;
		case Directions.RIGHT:
			switch (randomValue)
			{
			case 0:
				newDirection = Directions.UP;
				break;
			case 1:
				newDirection = Directions.LEFT;
				break;
			case 2:
				newDirection = Directions.DOWN;
				break;
			}
			break;
		}

		return newDirection;
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