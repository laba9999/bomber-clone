package com.bomber.common;

public class Utils {
	private static int mUUID = 0;
	
	public static int getNextUUID()
	{
		return ++mUUID;
	}
}
