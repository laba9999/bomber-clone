package com.bomber.common;

public class Directions {
	public static final short NONE = -1;
	public static final short UP = 0;
	public static final short DOWN = 1;
	public static final short LEFT = 2;
	public static final short RIGHT = 3;
	
	public static short getInverseDirection(short _direction)
	{
		if( _direction == UP) return DOWN;
		if( _direction == DOWN) return UP;
		if( _direction == LEFT) return RIGHT;
		if( _direction == RIGHT) return LEFT;
		
		return NONE;
	}
}