package com.bomber;

import com.badlogic.gdx.math.Vector2;
import com.bomber.common.PoolObject;

public class OverlayingText extends PoolObject {
	
	public static final short POINTS_TICKS_DURATION = 200;
	public short mTicksElapsed;
	public String mText;
	public Vector2 mPosition;
	
	@Override
	public void reset() {
		mTicksElapsed = 0;
		mText = null;
		mPosition = null;
	}
	
}
