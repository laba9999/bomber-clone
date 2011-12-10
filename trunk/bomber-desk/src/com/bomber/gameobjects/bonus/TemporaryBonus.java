package com.bomber.gameobjects.bonus;

import com.bomber.gameobjects.Player;

public abstract class TemporaryBonus extends Bonus {

	protected Player mAffectedPlayer;
	private int mEffectDuration;
	private int mEffectCurrentDuration;
	
	TemporaryBonus( int _durationTicks)
	{
		mEffectDuration = _durationTicks;
		mAffectedPlayer = null;
	}
	
	@Override
	public final void applyEffect(Player _player)
	{
		mAffectedPlayer = _player;
		mEffectCurrentDuration = 0;
		//mAffectedPlayer.mActiveBonus.addObject(this)
		
		onApplyEffect();
	}

	@Override
	public void update()
	{
		// Actualiza a animação
		super.update();
		
		if( mAffectedPlayer == null)
			return;
		
		if( ++mEffectCurrentDuration >= mEffectDuration)
		{
			onRemoveEffect();
			mAffectedPlayer.mActiveBonus.releaseObject(this);
		}
	}
	
	public abstract void onRemoveEffect();
	public abstract void onApplyEffect();
}
