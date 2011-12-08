package com.bomber.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.bomber.common.Directions;
import com.bomber.world.GameWorld;

public abstract class MovableObject extends Drawable {
	public float mSpeed;
	public short mDirection;
	public GameWorld mWorld;
	public boolean mIsMoving = false;
	public boolean mJustCollided = false;
	public MovableObjectAnimation mAnimations;

	private final Vector2 mCollision = new Vector2();

	public void moveLeft()
	{
		mIsMoving = true;
		if (mDirection != Directions.LEFT)
		{
			mDirection = Directions.LEFT;
			setCurrentAnimation(mAnimations.walkLeft, mAnimations.numberOfFramesPerWalk, true);
		}

		onChangedDirection();
	}

	public final void moveRight()
	{
		mIsMoving = true;
		if (mDirection != Directions.RIGHT)
		{
			mDirection = Directions.RIGHT;
			setCurrentAnimation(mAnimations.walkRight, mAnimations.numberOfFramesPerWalk, true);
		}

		onChangedDirection();
	}

	public void moveUp()
	{
		mIsMoving = true;
		if (mDirection != Directions.UP)
		{
			mDirection = Directions.UP;
			setCurrentAnimation(mAnimations.walkUp, mAnimations.numberOfFramesPerWalk, true);
		}

		onChangedDirection();
	}

	public void moveDown()
	{
		mIsMoving = true;
		if (mDirection != Directions.DOWN)
		{
			mDirection = Directions.DOWN;
			setCurrentAnimation(mAnimations.walkDown, mAnimations.numberOfFramesPerWalk, true);
		}

		onChangedDirection();
	}

	protected void move(float _amount)
	{
		// Actualiza a posição
		if (mDirection == Directions.LEFT)
			mPosition.x -= _amount;
		else if (mDirection == Directions.RIGHT)
			mPosition.x += _amount;
		else if (mDirection == Directions.UP)
			mPosition.y += _amount;
		else if (mDirection == Directions.DOWN)
			mPosition.y -= _amount;

	}

	protected void checkTileCollisions(boolean _ignoreDestroyables)
	{
		mJustCollided = mWorld.mMap.checkIfTileCollidingWithObject(this, mCollision, false);

		if(!mJustCollided)
			return;
		
		mPosition.x += mCollision.x;
		mPosition.y += mCollision.y;
	}

	protected void checkBombCollisions()
	{
		mJustCollided = mWorld.checkIfBombCollidingWithObject(this, mCollision);
		
		if(!mJustCollided)
			return;
		
		mPosition.x += mCollision.x;
		mPosition.y += mCollision.y;	
	}

	public void stop()
	{
		mIsMoving = false;
		stopCurrentAnimation();
		onStop();
	}

	protected abstract void onChangedDirection();

	protected abstract void onStop();
}