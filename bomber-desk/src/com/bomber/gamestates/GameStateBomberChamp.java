package com.bomber.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.bomber.Game;
import com.bomber.common.Achievements;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;

public class GameStateBomberChamp extends GameState {

	public GameStateBomberChamp(Game _game) {
		super(_game);
		
		Achievements.mHasCompletedCampaign = true;
		Achievements.saveFile();
	}

	@Override
	public void onPresent()
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
	}

	@Override
	protected void onFinish()
	{
	}

}
