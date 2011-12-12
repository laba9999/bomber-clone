package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;

public class BonusDoublePoints extends TemporaryBonus {

	public BonusDoublePoints()
	{
		super(Bonus.DOUBLE_POINTS, 600);
		
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