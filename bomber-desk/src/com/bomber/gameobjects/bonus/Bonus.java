package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.Player;

public abstract class Bonus extends Drawable {

	public abstract void applyEffect(Player _player);

	public abstract void removeEffect(Player _player);

	/**
	 * Baseado no tipo de bonus.
	 */
	@Override
	public final boolean equals(Object _rhs)
	{

		return false;
	}
}