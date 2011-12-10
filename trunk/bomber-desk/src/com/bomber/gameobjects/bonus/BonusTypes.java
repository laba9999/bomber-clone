package com.bomber.gameobjects.bonus;

import java.util.Random;


public class BonusTypes {
	
	public static short DOUBLE_POINTS = 0;
	public static short LIFE = 1;
	public static short BOMB_POWER = 2;
	public static short BOMB_COUNT = 3;
	public static short SHIELD = 4;
	public static short SPEED = 5;
	public static short PUSH = 6;
	
	public static short getRandom()
	{
		//TODO: indicar seed
		Random randomGenerator = new Random();
		int rnd = randomGenerator.nextInt(7);
		
		//TODO: definir probabilidades diferentes para cada bonus?
		
		return (short) rnd;
	}
}