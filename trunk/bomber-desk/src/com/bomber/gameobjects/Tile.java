package com.bomber.gameobjects;

public class Tile extends Drawable {
	public static final short TILE_SIZE = 47;
	public static final short WALKABLE = 0;
	public static final short DESTROYABLE = 1;
	public static final short COLLIDABLE = 2;
	public static final short PORTAL = 3;
	
	
	public short mType;
	public boolean mIsDestroyed = false;
	

	/**
	 * Método chamado pela classe GameWorld quando ocorre uma explosão
	 */
	public void explode()
	{
		mIsDestroyed = true;
		// TODO: Actualizar a animação
	}
}