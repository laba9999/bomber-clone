package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Player;

public class BonusExplosionSize extends Bonus {

	public void update()
	{
		super.update();
	}

	public void applyEffect(Player _player)
	{
		_player.mBombExplosionSize += 1;
	}

	public void removeEffect(Player _player)
	{
		_player.mBombExplosionSize -= 1;
	}
}