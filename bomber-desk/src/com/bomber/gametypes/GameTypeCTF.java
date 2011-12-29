package com.bomber.gametypes;

import com.badlogic.gdx.math.Rectangle;
import com.bomber.Game;
import com.bomber.Team;
import com.bomber.gameobjects.Flag;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;

public class GameTypeCTF extends GameTypeHandler {
	Team mTeam1;
	Team mTeam2;

	public GameTypeCTF() {
		mTeam1 = Game.mTeams[0];
		mTeam2 = Game.mTeams[1];
	}

	@Override
	public boolean isObjectiveAcomplished()
	{
		return false;
	}

	@Override
	public boolean isOver()
	{
		for (int c = 0; c < 2; c++)
		{
			for (int i = 0; i < mTeam1.mNumberPlayers; i++)
			{
				if (!Game.mTeams[c].mTransportingEnemyFlag)
					continue;

				Player p = Game.mTeams[c].mPlayers.get(i);
				if (p.mEnemyFlag == null)
					continue;

				Flag flag = p.mTeam.mId == 1 ? mGameWorld.mFlags[0] : mGameWorld.mFlags[1];
				Rectangle bb = p.getBoundingBox();
				if (!bb.contains(flag.mSpawnPosition.x + Tile.TILE_SIZE_HALF, flag.mSpawnPosition.y))
					continue;

				Game.mTeams[c].mCapturedEnemyFlag = true;
			}
		}

		if (mGameWorld.mClock.mReachedZero || Game.mTeams[0].mCapturedEnemyFlag || Game.mTeams[1].mCapturedEnemyFlag)
			return true;
		
		return false;
	}

	@Override
	public boolean isLost()
	{
		return false;
	}

	@Override
	public boolean onPlayerKill(Player _player)
	{
		if (_player.mEnemyFlag != null)
		{
			_player.mEnemyFlag.reset();
			_player.mEnemyFlag = null;
			_player.mTeam.mTransportingEnemyFlag = false;
		}

		return false;
	}

	@Override
	public void onPlayerDisconnect(Player _player)
	{

	}

}
