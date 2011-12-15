package com.bomber.gameobjects;


public class Tile extends Drawable {
	public static final short TILE_SIZE = 47;
	public static final short TILE_SIZE_HALF = TILE_SIZE / 2;
	public static final short WALKABLE = 0;
	public static final short DESTROYABLE = 1;
	public static final short COLLIDABLE = 2;
	public static final short PORTAL = 3;
	public static final int POINTS = 100;
	
	public short mContainedBonusType = -1;
	public boolean mIsPortal = false;
	public boolean mContainsBomb = false;
	
	public short mType;
	public int mPositionInArray;

	public Tile(short _type) {
		mType = _type;
	}

	@Override
	public void reset()
	{
		super.reset();

		mIsPortal = false;
		mContainsBomb = false;
		mContainedBonusType = -1;
	}

	public boolean containsBonus()
	{
		return mContainedBonusType !=-1;
	}
	
	/**
	 * Método chamado pela classe GameWorld quando ocorre uma explosão
	 */
	public void explode()
	{
		if (mType != DESTROYABLE)
			return;

		mPlayAnimation = true;
	}
}