package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.assets.GfxAssets;

public class BonusPush extends TemporaryBonus {

	public BonusPush()
	{
		super(Bonus.PUSH, 1000);
		
		Animation anim = GfxAssets.mBonusAnimations.get("bonus_hand");
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