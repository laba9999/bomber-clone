package com.bomber.gameobjects;

public abstract class MovableObject extends Drawable {
	public float mSpeed;
	public short mDirection;
	public boolean mIsMoving = false;
	public MovableObjectAnimation mAnimations;

	/**
	 * mIsMoving = true mCurrentAnimation = mAnimations.walkLeft;
	 */
	public void moveLeft()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * mIsMoving = true mCurrentAnimation = mAnimations.walkRight;
	 */
	public final void moveRight()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * mIsMoving = true mCurrentAnimation = mAnimations.walkUp;
	 */
	public void moveUp()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * mIsMoving = true mCurrentAnimation = mAnimations.walkDown;
	 */
	public void moveDown()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * isMoving = false; Estamos parados por isso Drawable::mPlayAnimation =
	 * false
	 */
	public void stop()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Actualiza o animation ticks. mas apenas se
	 */
	public void update()
	{
		super.update();
	}

	protected abstract void onChangedDirection();

	protected abstract void onStop();

	protected abstract void onMove();
}