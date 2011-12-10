package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class BonusSpeed extends Bonus {

	public BonusSpeed()
	{
		super();
		Animation anim = Assets.mBonus.get("bonus_speed");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true, true);
	}
	
    public void update()
    {
        super.update();
    }
    
	public void applyEffect(Player _player)
	{
		_player.mSpeedFactor *= 2;
	}

	public void removeEffect(Player _player)
	{
		_player.mSpeedFactor /= 2;
	}
}