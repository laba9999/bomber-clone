package com.bomber.gameobjects;

import com.bomber.common.Directions;

public abstract class KillableObject extends WorldMovableObject {


	public boolean mIsDead = false;

	final public void kill(short _killerColor)
	{
		if(mIsDead)
			return;
		
		boolean ignoreKill = onKill(_killerColor);
		
		if( ignoreKill )
			return;
		
		mIsDead = true;
		mDirection = Directions.NONE;
		
		if (mMovableAnimations != null)
			setCurrentAnimation(mMovableAnimations.die, mMovableAnimations.numberOfFramesDying, true, false);
		
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

	/**
	 * 
	 * @return Deve devolver se é suposto ignorar a morte ou não.
	 */
	protected abstract boolean onKill(short _killerId);
}