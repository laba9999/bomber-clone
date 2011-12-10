package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class BonusShield extends TemporaryBonus {

	public BonusShield()
	{
		super(2050);
		
		Animation anim = Assets.mBonus.get("bonus_shield");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true, true);
	}

	public void onApplyEffect()
	{
		mAffectedPlayer.mIsShieldActive = true;
	}

	public void onRemoveEffect()
	{
		mAffectedPlayer.mIsShieldActive = false;
	}
}