package com.bomber.gameobjects;

import javax.print.attribute.standard.Chromaticity;

import com.badlogic.gdx.math.Vector2;
import com.bomber.common.Collision;
import com.bomber.common.Directions;
import com.bomber.world.GameWorld;

public abstract class MovableObject extends Drawable {

	public float mSpeed = 0.5f;
	public short mDirection;
	public GameWorld mWorld;
	public boolean mIgnoreDestroyables;
	public MovableObjectAnimation mMovableAnimations;

	public boolean mIsMoving = false;
	private final Collision mCollision = new Collision();

	@Override
	public void reset()
	{
		mIsMoving = false;
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

		if(mCollision.mType == Collision.NONE)
			return;
		
		if( onMapCollision(mCollision.mType))
			mCollision.removeOverlap(mPosition);
	}

	@Override
	public void update()
	{
		// Actualiza a animação
		super.update();
		
		if(!mIsMoving)
			return;
		
		move();
		checkMapCollisions();
	}
	
	public void stop()
	{
		mIsMoving = false;
		onStop();
	}

	/**
	 * 
	 * @return True em caso de se pretender retirar o valor overlaped p.e
	 *         colisão do player com tiles. False se não se pretende retirar o
	 *         valor overlaped p.e colisão do tipo BOMB com um player com bonus
	 *         "Empurra bombas" activo.
	 */
	protected abstract boolean onMapCollision(short _collisionType);
	
	protected abstract void onChangedDirection();

	protected abstract void onStop();
}