package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Player;

public class BonusSpeed extends Bonus {

	public void update()
	{
		super.update();
	}

	public void applyEffect(Player _player)
	{
		_player.mSpeedFactor *= 2;
	}

	public void removeEffect(Player _player)
	{
		_player.mSpeedFactor /= 2;
	}
}