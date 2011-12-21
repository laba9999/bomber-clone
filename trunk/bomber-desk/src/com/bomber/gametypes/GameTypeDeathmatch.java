package com.bomber.gametypes;

import com.bomber.Game;
import com.bomber.Team;
import com.bomber.common.Directions;
import com.bomber.gameobjects.Player;

public class GameTypeDeathmatch extends GameTypeHandler {

	Team[] mTeams;

	public GameTypeDeathmatch() {
		mTeams = Game.mTeams;
	}

	@Override
	public boolean isObjectiveAcomplished()
	{
		return false;
	}

	@Override
	public boolean isOver()
	{
		return mGameWorld.mClock.mReachedZero || mTeams[0].areAllDead() || mTeams[1].areAllDead();
	}

	@Override
	public boolean isLost()
	{
		return false;
	}

	@Override
	public boolean onPlayerKill(Player _player)
	{
		_player.mLives = 0;
		_player.mDirection = Directions.DOWN;
		for (int i = 0; i < 2; i++)
		{
			Team team = mTeams[i];
			if (team.mPlayers.contains(_player))
			{
				if (_player == mGameWorld.getLocalPlayer() && team.mPlayers.size() > 1)
				{
					// Vista remota
					mGameWorld.setLocalPlayer(team.mPlayers.get(0) == _player ? team.mPlayers.get(1) : team.mPlayers.get(0));
					mGameWorld.getLocalPlayer().mAcceptPlayerInput = false;
				}

				break;
			}
		}

		return true;
	}

	@Override
	public void onPlayerDisconnect(Player _player)
	{

	}

}