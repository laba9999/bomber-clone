package com.bomber;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.bomber.common.ObjectsPool;
import com.bomber.gameobjects.Player;

public class Team {
	public short mId;
	public short mNumberPlayers;
	public short mRoundsWon;
	private ArrayList<Short> mPlayerIds = new ArrayList<Short>();
	public ArrayList<Player> mPlayers = new ArrayList<Player>();

	public boolean mTransportingEnemyFlag = false;
	public boolean mCapturedEnemyFlag = false;

	public Team(short _nplayers, short _id) {
		mNumberPlayers = _nplayers;
		mId = _id;
	}

	public void clear()
	{
		mPlayerIds.clear();
		mPlayers.clear();
	}
	
	public void addElement(Player _player)
	{
		if (mPlayers.contains(_player))
		{
			Game.LOGGER.log("Este jogador já pertence a esta equipa! (" + mId + ")");
			return;
		}

		if (mPlayers.size() >= mNumberPlayers)
		{
			Game.LOGGER.log("A equipa já está cheia!");
			return;
		}

		Game.LOGGER.log("O jogador " + _player.mColor + " foi adicionado à equipa " + mId);

		mPlayerIds.add(_player.mColor);
		mPlayers.add(_player);
		_player.mTeam = this;
	}

	public void remove(Player _player)
	{
		if (!mPlayers.contains(_player))
			throw new InvalidParameterException("Este jogador não pertence a esta equipa!");

		Game.LOGGER.log("O jogador " + _player.mColor + " foi removido da equipa " + mId);

		mPlayerIds.remove((Object) _player.mColor);
		mPlayers.remove(_player);
		_player.mTeam = null;
	}

	public void reset(ObjectsPool<Player> _players)
	{
		mPlayers.clear();

		for (short s : mPlayerIds)
		{
			for (Player p : _players)
			{
				if (p.mColor == s)
				{
					mPlayers.add(p);
					p.mTeam = this;
				}
			}
		}

		mTransportingEnemyFlag = false;
		mCapturedEnemyFlag = false;
	}

	public boolean areAllDead()
	{
		for (int i = 0; i < mPlayers.size(); i++)
			if (!mPlayers.get(i).mIsDead)
				return false;

		return true;
	}

	public int getTotalPoints()
	{
		int total = 0;
		for (int i = 0; i < mPlayers.size(); i++)
			total += mPlayers.get(i).mPoints;

		return total;
	}
}