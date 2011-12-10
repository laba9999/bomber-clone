package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class BonusDoublePoints extends TemporaryBonus {

	public BonusDoublePoints()
	{
		super(2050);
		
		Animation anim = Assets.mBonus.get("bonus_star");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true,true);
	}

	public void onApplyEffect()
	{
		mAffectedPlayer.mPointsMultiplier *= 2;
	}

	public void onRemoveEffect()
	{
		mAffectedPlayer.mPointsMultiplier /= 2;
	}
}