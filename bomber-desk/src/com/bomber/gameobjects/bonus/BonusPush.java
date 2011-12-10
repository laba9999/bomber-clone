package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class BonusPush extends TemporaryBonus {

	public BonusPush()
	{
		super(2050);
		
		Animation anim = Assets.mBonus.get("bonus_hand");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true, true);
	}
	
    
	public void onApplyEffect()
	{
		mAffectedPlayer.mIsAbleToPushBombs = true;
	}

	@Override
	public void onRemoveEffect()
	{
		mAffectedPlayer.mIsAbleToPushBombs = false;
	}
}