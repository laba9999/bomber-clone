package com.bomber.gameobjects;

import com.bomber.common.Collision;
import com.bomber.common.Directions;
import com.bomber.world.GameWorld;

public abstract class WorldMovableObject extends Drawable {

	public float mSpeed = 0.8f;//1.5f;
	public short mDirection;
	public GameWorld mWorld;
	public boolean mIgnoreDestroyables;
	public MovableObjectAnimation mMovableAnimations;

	public boolean mIsMoving = false;
	
	public boolean mAcceptPlayerInput = true;
	
	private final Collision mCollision = new Collision();

	
	@Override
	public void reset()
	{
		mAcceptPlayerInput = true;
		mIsMoving = false;
		super.reset();
	}

	public void setMovableAnimations(MovableObjectAnimation _anim)
	{
		mMovableAnimations = _anim;
		setCurrentAnimation(mMovableAnimations.walk[Directions.DOWN], mMovableAnimations.numberOfFramesPerWalk, false, true);
	}

	public void changeDirection(short _newDirection)
	{
		
	
		if (mDirection != _newDirection || !mIsMoving)
		{
			mDirection = _newDirection;
			setCurrentAnimation(mMovableAnimations.walk[_newDirection], mMovableAnimations.numberOfFramesPerWalk, true, true);
		}

		mIsMoving = true;
		onChangedDirection();
	}

	private void move()
	{
		// Actualiza a posição
		if (mDirection == Directions.LEFT)
			mPosition.x -= mSpeed;
		else if (mDirection == Directions.RIGHT)
			mPosition.x += mSpeed;
		else if (mDirection == Directions.UP)
			mPosition.y += mSpeed;
		else if (mDirection == Directions.DOWN)
			mPosition.y -= mSpeed;
	}

	private void checkMapCollisions()
	{
		mWorld.mMap.checkForCollisions(this, mCollision, mIgnoreDestroyables);

		if (mCollision.mType == Collision.NONE)
			return;

		mCollision.removeOverlap(mPosition);
			
		onMapCollision(mCollision.mType);
	}

	@Override
	public void update()
	{
		// Actualiza a animação
		super.update();

		if (!mIsMoving)
			return;

		move();
		checkMapCollisions();
	}

	public void stop()
	{
		onStop();
		mIsMoving = false;
	}

	/**
	 * 
	 * @return True em caso de se pretender retirar o valor overlaped p.e
	 *         colisão do player com tiles. False se não se pretende retirar o
	 *         valor overlaped p.e colisão do tipo BOMB com um player com bonus
	 *         "Empurra bombas" activo.
	 */
	protected abstract void onMapCollision(short _collisionType);

	protected abstract void onChangedDirection();

	protected abstract void onStop();
}