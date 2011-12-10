package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Player;

public class BonusNewLife extends Bonus {

	public void update()
	{
		super.update();
	}

	public void applyEffect(Player _player)
	{
		_player.mLives += 1;
	}

	public void removeEffect(Player _player)
	{
		_player.mLives -= 1;
	}
}