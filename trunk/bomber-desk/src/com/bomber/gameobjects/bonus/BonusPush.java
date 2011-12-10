package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class BonusPush extends Bonus {

	public BonusPush()
	{
		super();
		Animation anim = Assets.mBonus.get("bonus_hand");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true, true);
	}
	
    public void update()
    {
        super.update();
    }
    
    
	public void applyEffect(Player _player)
	{
		_player.mIsAbleToPushBombs = true;
	}

	public void removeEffect(Player _player)
	{
		_player.mIsAbleToPushBombs = false;
	}
}