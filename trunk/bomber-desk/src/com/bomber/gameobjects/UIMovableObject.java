package com.bomber.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class UIMovableObject extends GameObject {

	public float mSpeed = 0f;

	private Vector2 mStartPosition;
	private Vector2 mDirection;
	private int mDuration;
	
	public int mTicksCounter = 0;
	public boolean mIsMoving = false;

	public TextureRegion mTexture = null;

	public UIMovableObject(float _speed, float _startX, float _startY, float _dirX, float _dirY, int _duration) {

		mSpeed = _speed;
		mIsMoving = false;
		mDuration = _duration;
		mDirection = new Vector2(_dirX, _dirY);
		mStartPosition = new Vector2(_startX, _startY);
	}

	@Override
	public void update()
	{
		if (!mIsMoving)
			return;

		mPosition.x += mSpeed * mDirection.x;
		mPosition.y += mSpeed * mDirection.y;

		if (++mTicksCounter >= mDuration)
			mIsMoving = false;
	}

	public void setDirection(float _dirX, float _dirY)
	{
		mDirection.set(_dirX, _dirY);
	}

	@Override
	public void reset()
	{
		mIsMoving = false;
		mPosition.set(mStartPosition);
		mTicksCounter = 0;
	}
}
