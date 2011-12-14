package com.bomber.gameobjects.monsters;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.bomber.DebugSettings;
import com.bomber.OverlayingText;
import com.bomber.common.Directions;
import com.bomber.common.Utils;
import com.bomber.gameobjects.KillableObject;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.world.GameWorld;
import com.bomber.world.TilesetXMLHandler;

public class Monster extends KillableObject {
	private static short HOW_MANY_DIRECTIONS_TO_SAVE = 3;
	public MonsterInfo mInfo;
	private Random mRandomGenerator;
	
//	private short[] mPastDirections;
//	private short mPastDirectionsIndex;
//	private boolean mTurnedOnLastTile;
	
	public static ArrayList<Short> mAvailableDirections = new ArrayList<Short>();
	
	public Monster(GameWorld _world) {
		mWorld = _world;
		mUUID = Utils.getNextUUID();

		// TODO : ter muito cuidado pk em multiplayer isto tem de estar
		// sincronizado.
		mRandomGenerator = new Random(mUUID);
		
		
//		mTurnedOnLastTile = false;
//		mPastDirections = new short[HOW_MANY_DIRECTIONS_TO_SAVE];
//		mPastDirectionsIndex = 0;
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

		updateAvailableDirections();
		// Actualiza a posição
		super.update();

		// Verifica se o monstro está morto
		if (mIsDead)
		{
			if (mLooped)
			{
				mWorld.mMonsters.releaseObject(this);
			}
			return;
		}
		
		
		if (DebugSettings.MONSTERS_KILL_PLAYERS)
			checkForPlayerCollision();

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

		if(mAvailableDirections.size() == 0)
			return;
				
		short rand = (short )mRandomGenerator.nextInt(mAvailableDirections.size());
		short dir = mAvailableDirections.get(rand);
		changeDirection(dir);
	}
	
	
	private void updateAvailableDirections()
	{
		if(mIsDead)
			return;
		
		mAvailableDirections.clear();
			
		for(short i : Directions.getRemainingDirections(mDirection))
		{
			int x;
			
			if(mInfo.mAbleToFly)
				x = mWorld.mMap.getDistanceToNext(1, mPosition,i , Tile.WALKABLE,Tile.DESTROYABLE);
			else
				x = mWorld.mMap.getDistanceToNext(1, mPosition,i , Tile.WALKABLE);

			if(x!=-1)	
				mAvailableDirections.add(i);
			
		}
	}

	private void decideToTurn()
	{
		short chanceOfTurning = 3;
		float delta = 0.04f;
		
			
		if(mAvailableDirections.size() == 0)
			return;		
		
		
		Tile tileBelow = mWorld.mMap.getTile(mPosition);
		Rectangle mBB = getBoundingBox();	
		
		float x = tileBelow.mPosition.x - mBB.getX();
		float y = tileBelow.mPosition.y - mBB.getY();
		
		float dist = (float) Math.sqrt(x * x + y * y);

		if(dist>delta)
		{
			return;
		}

			if (mRandomGenerator.nextInt(10) < chanceOfTurning)
			{
				// obtem nova direcção
				short rand = (short )mRandomGenerator.nextInt(mAvailableDirections.size());

				changeDirection(mAvailableDirections.get(rand));
	
			}

			mPosition.set(tileBelow.mPosition.x + Tile.TILE_SIZE_HALF,tileBelow.mPosition.y+ Tile.TILE_SIZE_HALF);
	}


	
	@Override
	protected boolean onKill()
	{ 
		
		//TODO : colocar isto num metodo do GameWorld?
		String text = new Integer(mInfo.mPointsValue).toString();
		mWorld.addOverlayingPoints(text, mPosition);
		
		mWorld.getLocalPlayer().mPoints += mInfo.mPointsValue;
		
		return false;
	}

	@Override
	protected void onChangedDirection()
	{

	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub

	}

}