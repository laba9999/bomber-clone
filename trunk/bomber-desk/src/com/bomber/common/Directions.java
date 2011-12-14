package com.bomber.common;

import java.util.Random;

public class Directions {
	public static final short NONE = -1;
	public static final short UP = 0;
	public static final short DOWN = 1;
	public static final short LEFT = 2;
	public static final short RIGHT = 3;
	
	public static final short[] DIRECTIONS = {UP,DOWN,LEFT};
	
	public static short getInverseDirection(short _direction)
	{
		if( _direction == UP) return DOWN;
		if( _direction == DOWN) return UP;
		if( _direction == LEFT) return RIGHT;
		if( _direction == RIGHT) return LEFT;
		
		return NONE;
	}
	
	public static short getPerpendicularDirection(Random _randomGenerator, short _actualDirection)
	{
		short randomValue = (short) _randomGenerator.nextInt(2);
		short newDirection = 0;

		if (_actualDirection == Directions.DOWN || _actualDirection == Directions.UP)
		{
			if (randomValue == 0)
				newDirection = Directions.LEFT;
			else
				newDirection = Directions.RIGHT;

		} else
		{
			if (randomValue == 0)
				newDirection = Directions.UP;
			else
				newDirection = Directions.DOWN;
		}

		return newDirection;
	}
	
	public static short[] getRemainingDirections(short _direction)
	{

		if(_direction == Directions.NONE)
			return new short[0];
		
		short c = 0;
//		short inverseDirection =getInverseDirection(_direction);
		
		for(short i = UP; i <= RIGHT; i++)
		{
			if(_direction != i )
				DIRECTIONS[c++] = i;
		}
	
		return DIRECTIONS;
	}
	
	/*
	 * Retorna aleatóriamente qualquer direcção diferente da actual;
	 */
	public static short getAnyOtherDirection(Random _randomGenerator, short _actualDirection)
	{
		short randomValue = (short) _randomGenerator.nextInt(3);

		short newDirection = 0;

		switch (_actualDirection)
		{
		case Directions.DOWN:
			switch (randomValue)
			{
			case 0:
				newDirection = Directions.UP;
				break;
			case 1:
				newDirection = Directions.LEFT;
				break;
			case 2:
				newDirection = Directions.RIGHT;
				break;
			}
			break;
		case Directions.UP:
			switch (randomValue)
			{
			case 0:
				newDirection = Directions.DOWN;
				break;
			case 1:
				newDirection = Directions.LEFT;
				break;
			case 2:
				newDirection = Directions.RIGHT;
				break;
			}
			break;
		case Directions.LEFT:
			switch (randomValue)
			{
			case 0:
				newDirection = Directions.UP;
				break;
			case 1:
				newDirection = Directions.DOWN;
				break;
			case 2:
				newDirection = Directions.RIGHT;
				break;
			}
			break;
		case Directions.RIGHT:
			switch (randomValue)
			{
			case 0:
				newDirection = Directions.UP;
				break;
			case 1:
				newDirection = Directions.LEFT;
				break;
			case 2:
				newDirection = Directions.DOWN;
				break;
			}
			break;
		}

		return newDirection;
	}
}