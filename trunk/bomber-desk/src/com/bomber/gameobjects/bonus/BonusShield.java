package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.PlayerEffect;
import com.bomber.common.assets.GfxAssets;

public class BonusShield extends TemporaryBonus {

	public BonusShield() {
		super(Bonus.SHIELD, 1000); // 10secs

		Animation anim = GfxAssets.mBonusAnimations.get("bonus_shield");
		setCurrentAnimation(anim, (short) NUMBER_OF_ANIMATION_FRAMES, true, true);
	}

	public void onApplyEffect()
	{
		if (!mAffectedPlayer.mIsShieldActive)
		{
			// Adiciona o efeito maravilhástico
			mAffectedPlayer.mEffects.addObject(new PlayerEffect(PlayerEffect.SHIELD));
		}

		mAffectedPlayer.mIsShieldActive = true;
	}

	public void onRemoveEffect()
	{
		mAffectedPlayer.mIsShieldActive = false;

		// Remove o efeito maravilhástico :(
		for (PlayerEffect ef : mAffectedPlayer.mEffects)
		{
			if (ef.mType == PlayerEffect.SHIELD)
			{
				mAffectedPlayer.mEffects.releaseObject(ef);
				break;
			}
		}
	}
}