package com.bomber.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.bomber.common.Directions;
import com.bomber.world.GameWorld;

public abstract class MovableObject extends Drawable {

	public float mSpeed = 0.5f;
	public short mDirection;
	public GameWorld mWorld;
	
	protected boolean mIsMoving = false;
	protected boolean mJustCollided= false;
	
	public MovableObjectAnimation mMovableAnimations;

	
	private final Vector2 mCollision = new Vector2();

	@Override
	public void reset()
	{
		mIsMoving = false;
		mJustCollided = false;

		
		super.reset();
	}
	
	public void setMovableAnimations(MovableObjectAnimation _anim)
	{
		mMovableAnimations = _anim;
		setCurrentAnimation(mMovableAnimations.walkDown, mMovableAnimations.numberOfFramesPerWalk, false, true);
	}
	
	public void moveLeft()
	{
		
		if (mDirection != Directions.LEFT || !mIsMoving)
		{
			mDirection = Directions.LEFT;
			setCurrentAnimation(mMovableAnimations.walkLeft, mMovableAnimations.numberOfFramesPerWalk, true, true);
		}
		
		mIsMoving = true;

		onChangedDirection();
	}

	public final void moveRight()
	{

		if (mDirection != Directions.RIGHT|| !mIsMoving)
		{
			mDirection = Directions.RIGHT;
			setCurrentAnimation(mMovableAnimations.walkRight, mMovableAnimations.numberOfFramesPerWalk, true, true);
		}

		mIsMoving = true;
		onChangedDirection();
	}

	public void moveUp()
	{

		if (mDirection != Directions.UP|| !mIsMoving)
		{
			mDirection = Directions.UP;
			setCurrentAnimation(mMovableAnimations.walkUp, mMovableAnimations.numberOfFramesPerWalk, true, true);
		}
		mIsMoving = true;
		onChangedDirection();
	}

	public void moveDown()
	{

		if (mDirection != Directions.DOWN|| !mIsMoving)
		{
			mDirection = Directions.DOWN;
			setCurrentAnimation(mMovableAnimations.walkDown, mMovableAnimations.numberOfFramesPerWalk, true, true);
		}
		mIsMoving = true;
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

	protected void checkMapCollisions(boolean _ignoreDestroyables)
	{
		mJustCollided = mWorld.mMap.checkForCollisions(this, mCollision, _ignoreDestroyables);

		if(!mJustCollided)
			return;
		
		mPosition.x += mCollision.x;
		mPosition.y += mCollision.y;
	}


	public void stop()
	{
		mIsMoving = false;
		onStop();
	}

	protected abstract void onChangedDirection();

	protected abstract void onStop();
}