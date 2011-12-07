package com.bomber.gameobjects;

import com.bomber.common.Directions;

public abstract class MovableObject extends Drawable {
	public float mSpeed;
	public short mDirection;
	public boolean mIsMoving = false;
	public MovableObjectAnimation mAnimations;


	public void moveLeft()
	{
		onChangedDirection();
		mDirection = Directions.LEFT;
		setCurrentAnimation(mAnimations.walkLeft, mAnimations.numberOfFramesPerWalk);
	}

	public final void moveRight()
	{
		onChangedDirection();
		mDirection = Directions.RIGHT;
		setCurrentAnimation(mAnimations.walkRight, mAnimations.numberOfFramesPerWalk);
	}

	public void moveUp()
	{
		onChangedDirection();
		mDirection = Directions.UP;
		setCurrentAnimation(mAnimations.walkUp, mAnimations.numberOfFramesPerWalk);
	}

	public void moveDown()
	{
		onChangedDirection();
		mDirection = Directions.DOWN;
		setCurrentAnimation(mAnimations.walkDown, mAnimations.numberOfFramesPerWalk);
	}

	public void stop()
	{
		onStop();
		mDirection = Directions.NONE;
		setCurrentAnimation(mAnimations.die, mAnimations.numberOfFramesDying);
	}


	public void update()
	{
		super.update();
		
		// Actualiza a posição
	}

	protected abstract void onChangedDirection();

	protected abstract void onStop();
}