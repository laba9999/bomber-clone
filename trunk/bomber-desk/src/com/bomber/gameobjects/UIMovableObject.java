package com.bomber.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bomber.common.ObjectsPool;

public class UIMovableObject extends GameObject {

	public float mSpeed = 0f;

	protected Vector2 mStartPosition;
	private Vector2 mDirection;
	private int mDuration;

	public int mTicksCounter = 0;
	public boolean mIsMoving = false;

	public TextureRegion mTexture = null;

	public ObjectsPool<UIMovableObject> mChilds;

	public UIMovableObject(float _speed, float _startX, float _startY, float _dirX, float _dirY, int _duration) {

		mSpeed = _speed;
		mIsMoving = false;
		mDuration = _duration;
		mDirection = new Vector2(_dirX, _dirY);
		mStartPosition = new Vector2(_startX, _startY);

		mChilds = new ObjectsPool<UIMovableObject>((short) 0, null);
	}

	public <T extends UIMovableObject> void addChild(T _newChild)
	{
		mChilds.addObject(_newChild);
	}

	@Override
	public void update()
	{
		if (!mIsMoving)
			return;

		mPosition.x += mSpeed * mDirection.x;
		mPosition.y += mSpeed * mDirection.y;

		for (UIMovableObject child : mChilds)
		{
			child.mPosition.x += mSpeed * mDirection.x;
			child.mPosition.y += mSpeed * mDirection.y;
		}
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
