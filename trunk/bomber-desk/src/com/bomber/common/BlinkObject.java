package com.bomber.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BlinkObject {
	short mTicksBeforeStopBlinking;
	short mBlinkInterval;
	short mTicksSinceLastBlink = 0;
	boolean mDrawing = true;
	public boolean mExceededDuration = false;
	TextureRegion mTexture;
	float mPosX;
	float mPosY;

	public BlinkObject(short _blinkInterval, short _blinkDuration, TextureRegion _texture, float _posX, float _posY) {
		mTicksBeforeStopBlinking = _blinkDuration;
		mBlinkInterval = _blinkInterval;
		mTexture = _texture;
		mPosX = _posX;
		mPosY = _posY;
	}

	public void update()
	{
		if (!mExceededDuration && mTicksBeforeStopBlinking-- < 0)
			mExceededDuration = true;

		if (mTicksSinceLastBlink++ > mBlinkInterval)
		{
			mDrawing = !mDrawing;
			mTicksSinceLastBlink = 0;
		}

	}

	public void draw(SpriteBatch _batcher)
	{
		if (mExceededDuration || mDrawing)
		{
			_batcher.draw(mTexture, mPosX, mPosY);
			return;
		}
	}
}
