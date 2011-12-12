package com.bomber.gameobjects.monsters;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.bomber.common.Directions;
import com.bomber.common.Utils;
import com.bomber.gameobjects.KillableObject;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.world.GameWorld;

public class Monster extends KillableObject {

	public MonsterInfo mInfo;
	private Random mRandomGenerator;

	public Monster(GameWorld _world) {
		mWorld = _world;
		mUUID = Utils.getNextUUID();

		// TODO : ter muito cuidado pk em multiplayer isto tem de estar
		// sincronizado.
		mRandomGenerator = new Random(mUUID);
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

		// TODO : descomentar
		//checkForPlayerCollision();
		decideToTurn();
	}

	private void checkForPlayerCollision()
	{
		for (Player p : mWorld.mPlayers)
			if (getBoundingBox().contains(p.mPosition.x, p.mPosition.y))
				p.kill();
	}

	@Override
	protected void onMapCollision(short _collisionType)
	{
		mDirection = Directions.getAnyOtherDirection(mRandomGenerator, mDirection);
	}

	private void decideToTurn()
	{
		Tile tileBelow = mWorld.mMap.getTile(mPosition);
		// verifica se está exactamente no centrado na tile:
		if (mPosition.x - Tile.TILE_SIZE_HALF == tileBelow.mPosition.x && mPosition.y - Tile.TILE_SIZE_HALF == tileBelow.mPosition.y)
		{
			if (mRandomGenerator.nextInt(10) < 3)
			{
				// obtem nova direcção
				short newDirection = Directions.getPerpendicularDirection(mRandomGenerator, mDirection);
				Tile newTileInDirection = mWorld.mMap.getTile(mPosition, newDirection, (short) 1);

				// verifica se a tile na direcção de newDirection é "andável"
				if (newTileInDirection.mType == Tile.WALKABLE || (newTileInDirection.mType == Tile.DESTROYABLE && mInfo.mAbleToFly))
					mDirection = newDirection;
			}
		}
	}

	@Override
	protected boolean onKill()
	{
		return false;
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