package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Player;

public class BonusShield extends Bonus {

	public void update()
	{
		super.update();
	}

	public void applyEffect(Player _player)
	{
		_player.mIsShieldActive = true;
	}

	public void removeEffect(Player _player)
	{
		_player.mIsShieldActive = false;
	}
}