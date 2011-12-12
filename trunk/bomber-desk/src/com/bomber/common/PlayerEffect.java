package com.bomber.common;

import com.badlogic.gdx.math.Vector2;
import com.bomber.gameobjects.Drawable;

public class PlayerEffect extends Drawable {

	public final static short SHIELD = 0;
	public final static short WATER_SPLASH = 0;

	public short mType;

	public PlayerEffect(short _type) {
		mType = _type;

		if (mType == SHIELD)
			setCurrentAnimation(Assets.mPlayerEffects.get("shield"), (short) 4, true, true);

	}

	@Override
	public Vector2 drawingPoint()
	{
		if (mType == SHIELD)
			mDrawingPoint.set((mPosition.x - mCurrentFrame.getRegionWidth() / 2), (mPosition.y - mCurrentFrame.getRegionWidth() / 2) + 5);

		return mDrawingPoint;
	}

}
