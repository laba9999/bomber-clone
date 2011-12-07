package com.bomber.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bomber.common.PoolObject;


public abstract class GameObject extends PoolObject {
	public Vector2 mPosition;
	public Rectangle mBounds;
	public int mUUID;

	public abstract void update();

	/**
	 * Tem de devolver o mUUID.
	 */
	@Override
	public final int hashCode()
	{
		throw new UnsupportedOperationException();
	}

	public final boolean equals(Object _rhs)
	{
		return ((GameObject)_rhs).mUUID == mUUID;
	}

}