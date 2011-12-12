package com.bomber.gameobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bomber.common.PoolObject;

public abstract class GameObject extends PoolObject {
	public Vector2 mPosition = new Vector2();
	protected Vector2 mDrawingPoint = new Vector2();
	private Rectangle mBoudingBox = new Rectangle(0, 0, Tile.TILE_SIZE, Tile.TILE_SIZE);
	public int mUUID;

	public abstract void update();

	/**
	 * Tem de devolver o mUUID.
	 */
	@Override
	public final int hashCode()
	{
		return mUUID;
	}

	public final boolean equals(Object _rhs)
	{
		return ((GameObject) _rhs).mUUID == mUUID;
	}

	public Rectangle getBoundingBox()
	{
		mBoudingBox.x = mPosition.x - Tile.TILE_SIZE_HALF;
		mBoudingBox.y = mPosition.y - Tile.TILE_SIZE_HALF;

		return mBoudingBox;
	}

	public Vector2 drawingPoint()
	{
		mDrawingPoint.set(mPosition.x - Tile.TILE_SIZE_HALF, mPosition.y- Tile.TILE_SIZE_HALF);
		return mDrawingPoint;
	}

}