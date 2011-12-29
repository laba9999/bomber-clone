package com.bomber.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.bomber.Game;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;

public class GameStateBomberChamp extends GameState {

	public GameStateBomberChamp(Game _game) {
		super(_game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPresent(float _interpolation)
	{
		BitmapFont font = GfxAssets.mGenericFont;
		String finalString;
		TextBounds tx;

		mBatcher.draw(GfxAssets.Pixmaps.getGreen(), 0, 0);

		finalString = Strings.mStrings.get("bomber_champ");
		tx = font.getBounds(finalString);
		font.draw(mBatcher, finalString, 400 - tx.width / 2, 60);

		// Troféu grande centrado no ecrã
		mBatcher.draw(GfxAssets.mTrophy[1], 363, 160);

		short startX = 250;
		short startY = 100;
		mBatcher.draw(mGameWorld.getLocalPlayer().mExtraTextures.get("happy"), startX, startY);

	}

	@Override
	protected void onUpdate()
	{
		if (Gdx.input.justTouched())
			Game.goBackToActivities();

	}

	@Override
	protected void onUpdateFinishing()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onFinish()
	{
		// TODO Auto-generated method stub

	}

}
