package com.bomber.gameobjects.bonus;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class BonusDoublePoints extends Bonus {

	
	public BonusDoublePoints()
	{
		super();
		Animation anim = Assets.mBonus.get("bonus_star");
		setCurrentAnimation(anim,(short) NUMBER_OF_ANIMATION_FRAMES, true,true);
	}
	
    public void update()
    {
        super.update();
    }

	public void applyEffect(Player _player)
	{
		_player.mPointsMultiplier *= 2;
	}

	public void removeEffect(Player _player)
	{
		_player.mPointsMultiplier /= 2;
	}
}