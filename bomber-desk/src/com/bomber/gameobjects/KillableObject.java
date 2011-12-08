package com.bomber.gameobjects;

import com.bomber.common.Directions;

public abstract class KillableObject extends MovableObject {
	protected boolean mIsDead = false;

	public void kill()
	{
		mIsDead = true;
		mDirection = Directions.NONE;
		setCurrentAnimation(mAnimations.die, mAnimations.numberOfFramesDying, true);
		onKill();
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