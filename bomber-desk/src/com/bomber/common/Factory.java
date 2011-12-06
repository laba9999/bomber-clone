package com.bomber.common;

import com.bomber.gameobjects.GameObject;

public abstract class Factory<T> {

	public final T create()
	{
		T tmpObject = onCreate();
		if( tmpObject instanceof GameObject)
			((GameObject) tmpObject).mUUID = Utils.getNextUUID();
		
		return tmpObject;
	}

	public abstract T onCreate();
}