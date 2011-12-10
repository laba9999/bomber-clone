package com.bomber.common;

import com.badlogic.gdx.math.Rectangle;

public class Utils {
	private static int mUUID = 0;
	
	public static int getNextUUID()
	{
		return ++mUUID;
	}
	public static boolean rectsOverlap(Rectangle r1, Rectangle r2)
	{
		if (r2.x >= r1.x + r1.width)
			return false;
		if (r2.x + r2.width <= r1.x)
			return false;
		if (r2.y >= r1.y + r1.height)
			return false;
		if (r2.y + r2.height <= r1.y)
			return false;

		return true;
	}
}
