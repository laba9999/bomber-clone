package com.bomber.gameobjects;

import com.bomber.common.Utils;

public class Tile extends Drawable {
	public static final short TILE_SIZE = 47;
	public static final short TILE_SIZE_HALF = TILE_SIZE /2;
	public static final short WALKABLE = 0;
	public static final short DESTROYABLE = 1;
	public static final short COLLIDABLE = 2;
	public static final short PORTAL = 3;


	public short mType;
	public int mPositionInArray;

	public Tile(short _type) {
		mType = _type;
		mUUID = Utils.getNextUUID();
	}

	/**
	 * Método chamado pela classe GameWorld quando ocorre uma explosão
	 */
	public void explode()
	{
		if (mType != DESTROYABLE)
			return;

		// TODO: Actualizar a animação
	}
}