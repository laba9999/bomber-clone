package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class BonusBombCount extends Bonus {

	public BonusBombCount()
	{
		Animation anim = Assets.mBonusAnimations.get("bonus_bomb");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true,true);
	}


	@Override
	public void applyEffect(Player _player)
	{
		_player.mMaxSimultaneousBombs += 1;
	}
}