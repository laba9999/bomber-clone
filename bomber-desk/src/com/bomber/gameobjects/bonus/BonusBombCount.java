package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Player;

public class BonusBombCount extends Bonus {

	public void update()
	{
		super.update();
	}

	public void applyEffect(Player _player)
	{
		_player.mMaxSimultaneousBombs += 1;
	}

	public void removeEffect(Player _player)
	{
		_player.mMaxSimultaneousBombs -= 1;
	}
}