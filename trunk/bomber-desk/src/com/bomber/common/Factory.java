package com.bomber.common;

public abstract class Factory<T> {

	public final T create()
	{
		throw new UnsupportedOperationException();
	}

	public abstract T onCreate();
}