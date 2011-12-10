package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Player;

public class BonusPush extends Bonus {

	public void update()
	{
		super.update();
	}

	public void applyEffect(Player _player)
	{
		_player.mIsAbleToPushBombs = true;
	}

	public void removeEffect(Player _player)
	{
		_player.mIsAbleToPushBombs = false;
	}
}