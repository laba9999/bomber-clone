package com.bomber.gameobjects.bonus;

import java.util.HashMap;

public class BonusTypes {
	private static HashMap<Short, String> ANIMATION_KEYS = null;
	
	public static short DOUBLE_POINTS = 0;
	public static short LIFE = 1;
	public static short BOMB_POWER = 2;
	public static short BOMB_COUNT = 3;
	public static short SHIELD = 4;
	public static short SPEED = 5;
	public static short PUSH = 6;
	
	
	public static String getAnimationKeyFromType(short _type)
	{
		if (ANIMATION_KEYS == null)
		{
			ANIMATION_KEYS  = new HashMap<Short, String>();

			ANIMATION_KEYS.put((short) 0, "bonus_star");
			ANIMATION_KEYS.put((short) 1, "bonus_life");
			ANIMATION_KEYS.put((short) 2, "bonus_potion");
			ANIMATION_KEYS.put((short) 3, "bonus_bomb");
			ANIMATION_KEYS.put((short) 4, "bonus_shield");
			ANIMATION_KEYS.put((short) 5, "bonus_speed");
			ANIMATION_KEYS.put((short) 6, "bonus_hand");
		}

		return ANIMATION_KEYS .get(_type);
	}
}