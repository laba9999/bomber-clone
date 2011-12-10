package com.bomber.gameobjects.monsters;

import java.util.Random;

import com.bomber.common.Directions;
import com.bomber.common.Utils;
import com.bomber.gameobjects.KillableObject;
import com.bomber.gameobjects.Tile;
import com.bomber.world.GameWorld;

public class Monster extends KillableObject {
	public MonsterInfo mInfo;

	public Monster(GameWorld _world) {
		mWorld = _world;
		mUUID = Utils.getNextUUID();
	}
	
	@Override
	public void reset()
	{
		super.reset();
		mIsMoving = true;
	}

	@Override
	public void update()
	{
		// Actualiza a posição
		super.update();

		// Verifica se o monstro está morto
		if (mIsDead)
		{
			if (mLooped)
				mWorld.mMonsters.releaseObject(this);

			return;
		}

		decideToTurn();
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
	
	@Override
	protected boolean onMapCollision(short _collisionType)
	{
		mDirection = getAnyOtherDirection(mDirection);
		return true;
	}

	private void decideToTurn()
	{

		// TODO :indicar seed?
		Random randomGenerator = new Random();
		Tile tileBelow = mWorld.mMap.getTile(mPosition);
		// verifica se está exactamente no centrado na tile:
		if (mPosition.x - Tile.TILE_SIZE_HALF == tileBelow.mPosition.x && mPosition.y - Tile.TILE_SIZE_HALF == tileBelow.mPosition.y)
		{

			int rnd = randomGenerator.nextInt(10);

			// TODO: ajustar valor
			if (rnd < 3)
			{
				// obtem nova direcção
				short newDirection = getLeftOrRightDirection(mDirection);
				Tile newTileInDirection = mWorld.mMap.getTile(mPosition, newDirection, (short) 1);

				// verifica se a tile na direcção de newDirection é "andável"
				if (newTileInDirection.mType == Tile.WALKABLE || (newTileInDirection.mType == Tile.DESTROYABLE && mInfo.mAbleToFly))
				{
					// se for "andável", atribui a nova direcção
					mDirection = newDirection;
				}
			}

		}
	}

	/*
	 * Retorna aleatóriamente qualquer direcção diferente da actual;
	 */
	private short getAnyOtherDirection(short _actualDirection)
	{

		// TODO : indicar seed?
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

	/*
	 * Retorna aleatóriamente a direcção à esquerda ou à direita da tile actual;
	 */
	private short getLeftOrRightDirection(short _actualDirection)
	{

		// TODO : indicar seed?
		Random randomGenerator = new Random();
		short randomValue = (short) randomGenerator.nextInt(2);
		short newDirection = 0;

		if (mDirection == Directions.DOWN || mDirection == Directions.UP)
		{
			if (randomValue == 0)
				newDirection = Directions.LEFT;
			else
				newDirection = Directions.RIGHT;

		} else
		{
			if (randomValue == 0)
				newDirection = Directions.UP;
			else
				newDirection = Directions.DOWN;
		}

		return newDirection;
	}



}