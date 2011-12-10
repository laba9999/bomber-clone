package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.Player;

public abstract class Bonus extends Drawable {
	public static final short NUMBER_OF_ANIMATION_FRAMES = 6;
	
	public abstract void applyEffect(Player _player);

	public abstract void removeEffect(Player _player);
}