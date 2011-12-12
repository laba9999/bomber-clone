package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.Player;

public abstract class Bonus extends Drawable {
	public static final short NUMBER_OF_ANIMATION_FRAMES = 6;

	public static short DOUBLE_POINTS = 0;
	public static short LIFE = 1;
	public static short BOMB_POWER = 2;
	public static short BOMB_COUNT = 3;
	public static short SHIELD = 4;
	public static short SPEED = 5;
	public static short PUSH = 6;


	@Override
	public void update()
	{
		// Actualiza a animação
		super.update();
	}
	
	public abstract void applyEffect(Player _player);
}