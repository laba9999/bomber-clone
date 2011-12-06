package com.bomber.gameobjects;

public class Tile extends Drawable {
	public static final short TILE_SIZE = 47;
	public static final short WALKABLE = 0;
	public static final short DESTROYABLE = 1;
	public static final short COLLIDABLE = 2;
	public static final short PORTAL = 3;
	public short mType;

	/**
	 * Método chamado pela classe GameWorld quando ocorre uma explosão
	 */
	public void explode()
	{
		throw new UnsupportedOperationException();
	}

	public void update()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object _rhs)
	{
		// TODO Auto-generated method stub
		return false;
	}
}