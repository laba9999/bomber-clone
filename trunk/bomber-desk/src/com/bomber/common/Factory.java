package com.bomber.common;


public abstract class Factory<T> {

	public final T create()
	{
		T tmpObject = onCreate();

		return tmpObject;
	}

	public abstract T onCreate();
}