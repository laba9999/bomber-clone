package com.bomber.gameobjects;

public abstract class KillableObject extends MovableObject {
	private boolean mIsDead = false;

	/**
	 * mIsDead = true; mCurrentAnimation = mAnimations.die;
	 */
	public void kill()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * É utilizado pela ObjectPool quando o objecto é marcado como disponivel.
	 * Este método deve ser sempre chamar o seu super antes/depois de efectuar
	 * mudanças numa classe derivada
	 * 
	 * mIsBeingDestroyed = false;
	 */
	public void reset()
	{
		throw new UnsupportedOperationException();
	}

	protected abstract void onKill();
}