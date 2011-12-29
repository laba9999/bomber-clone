package com.bomber.gametypes;

import com.bomber.common.Directions;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;

public class GameTypeCampaign extends GameTypeHandler {

	public GameTypeCampaign() {
		mNeedsPortal = true;
	}

	public boolean isObjectiveAcomplished()
	{
		if (mGameWorld == null)
			throw new IllegalStateException("Deve ser atribuido um Game World");

		boolean ret = false;

		if (mGameWorld.mMonsters.mLenght == 0)
			ret = true;

		return ret;
	}

	public boolean isOver()
	{
		if (mGameWorld == null)
			throw new IllegalStateException("Deve ser atribuido um Game World");

		boolean ret = false;

		if (isObjectiveAcomplished())
		{
			float x = mGameWorld.getLocalPlayer().mPosition.x - Tile.TILE_SIZE_HALF;
			float y = mGameWorld.getLocalPlayer().mPosition.y - Tile.TILE_SIZE_HALF;

			Tile portal = mGameWorld.mMap.mPortal;

			if (portal != null && portal.getBoundingBox().contains(x, y))
				ret = true;
		}

		return ret;
	}

	@Override
	public boolean isLost()
	{
		Player localPlayer = mGameWorld.getLocalPlayer();
		return (localPlayer.mLives == 0 && localPlayer.mLooped) || mGameWorld.mClock.mReachedZero;
	}

	@Override
	public boolean onPlayerKill(Player _player)
	{
		_player.mLives--;
		_player.mDirection = Directions.NONE;
		return true;
	}

	@Override
	public void onPlayerDisconnect(Player _player)
	{	
	}
}
