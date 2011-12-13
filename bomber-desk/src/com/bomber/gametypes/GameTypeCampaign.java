package com.bomber.gametypes;

import com.bomber.gameobjects.Tile;

public class GameTypeCampaign extends GameType {
	
	public GameTypeCampaign() {
		super();
		mNeedsPortal = true;
	}
	
	public boolean isObjectiveAcomplished()
	{
		if(mGameWorld == null)
			throw new IllegalStateException("Deve ser atribuido um Game World");
		
		boolean ret = false;
		
		if(mGameWorld.mMonsters.usedObjects() == 0)
		{
			ret = true;
		}
		
		return ret;
	}
	
	public boolean isOver()
	{
		if(mGameWorld == null)
			throw new IllegalStateException("Deve ser atribuido um Game World");
		
		boolean ret = false;
 		
		if(isObjectiveAcomplished())
		{
			float x = mGameWorld.getLocalPlayer().mPosition.x - Tile.TILE_SIZE_HALF;
			float y = mGameWorld.getLocalPlayer().mPosition.y - Tile.TILE_SIZE_HALF;
			
			Tile portal = mGameWorld.mMap.mPortal;

			if(portal != null && portal.getBoundingBox().contains(x,y))
			{
				ret = true;
			}

		}
		
		return ret;
		
	}
	

}
