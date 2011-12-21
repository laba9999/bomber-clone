package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.assets.Assets;
import com.bomber.gameobjects.Player;

public class BonusNewLife extends Bonus {

	
	public BonusNewLife()
	{
		Animation anim = Assets.mBonusAnimations.get("bonus_life");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true, true);
	}
    
	public void applyEffect(Player _player)
	{
		_player.mLives += 1;
	}
}