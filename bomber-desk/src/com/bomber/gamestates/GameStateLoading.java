package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.Game;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.common.assets.Level;

public class GameStateLoading extends GameState {
	
	public GameStateLoading(Game _gameScreen) {
		super(_gameScreen);

	}

	@Override
	public void onUpdate()
	{
		if (Level.mIsLoaded)
			mGame.setGameState(new GameStatePlaying(mGame));
	}

	public void onPresent(float _interpolation)
	{
		mBatcher.setProjectionMatrix(mUICamera.combined);
		BitmapFont font = GfxAssets.mGenericFont;
		mBatcher.draw(GfxAssets.Pixmaps.getGrey(), 0, 0);
		font.draw(mBatcher, Strings.mStrings.get("loading"), 320, 250);
	}

	@Override
	protected void onFinish()
	{
	}

	@Override
	protected void onUpdateFinishing()
	{
	}
}