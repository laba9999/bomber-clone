package com.bomber.gameobjects.bonus;

import java.util.Random;


public class BonusTypes {

	
	public static short getRandom()
	{
		//TODO: indicar seed
		Random randomGenerator = new Random();
		int rnd = randomGenerator.nextInt(7);
		
		//TODO: definir probabilidades diferentes para cada bonus?
		
		return (short) rnd;
	}
}