package com.bomber.common;

import com.badlogic.gdx.math.Vector2;

public class Collision {

	public static final short NONE = 0;
	public static final short TILE = 1;
	public static final short BOMB = 2;
	
	public static final float ALLOWED_OVERLAP = 10.0f;

	public Vector2 mAmounts = new Vector2();
	public short mType;

	public void removeOverlap(Vector2 _position)
	{
		_position.x += mAmounts.x;
		_position.y += mAmounts.y;
	}

	public void reset()
	{
		mType = NONE;
		mAmounts.set(0, 0);
	}
}
