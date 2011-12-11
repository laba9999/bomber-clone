package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class BonusSpeed extends TemporaryBonus {

	public BonusSpeed() {

		super(2050);

		Animation anim = Assets.mBonus.get("bonus_speed");
		setCurrentAnimation(anim, (short) NUMBER_OF_ANIMATION_FRAMES, true, true);
	}

	@Override
	public void onRemoveEffect()
	{
		mAffectedPlayer.mSpeed -= 1;
	}

	@Override
	public void onApplyEffect()
	{
		mAffectedPlayer.mSpeed += 1;
	}
}
