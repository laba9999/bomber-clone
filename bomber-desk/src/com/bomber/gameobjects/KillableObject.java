package com.bomber.gameobjects;

import com.bomber.common.Directions;

public abstract class KillableObject extends MovableObject {


	protected boolean mIsDead = false;

	final public void kill()
	{
		if(mIsDead)
			return;
		
		mIsDead = true;
		mDirection = Directions.NONE;
		
		if (mMovableAnimations != null)
			setCurrentAnimation(mMovableAnimations.die, mMovableAnimations.numberOfFramesDying, true, false);
		
		onKill();
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * É utilizado pela ObjectPool quando o objecto é marcado como disponivel.
	 * Este método deve ser sempre chamar o seu super antes/depois de efectuar
	 * mudanças numa classe derivada
	 */
	public void reset()
	{
		mIsDead = false;
	}

	protected abstract void onKill();
}