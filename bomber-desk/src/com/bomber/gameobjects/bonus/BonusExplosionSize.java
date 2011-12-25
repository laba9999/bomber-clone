package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.assets.GfxAssets;
import com.bomber.gameobjects.Player;

public class BonusExplosionSize extends Bonus {

	
	public BonusExplosionSize()
	{
		Animation anim = GfxAssets.mBonusAnimations.get("bonus_potion");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true, true);
	}
    
	public void applyEffect(Player _player)
	{
		_player.mBombExplosionSize += 1;
	}
}