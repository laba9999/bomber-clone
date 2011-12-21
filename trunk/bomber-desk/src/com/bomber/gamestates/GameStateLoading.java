package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.Game;
import com.bomber.common.assets.Assets;
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
		BitmapFont font = Assets.mFont;

		font.draw(mBatcher, "A carregar... ", 320, 250);
	}

	@Override
	protected void onFinish()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUpdateFinishing()
	{
		// TODO Auto-generated method stub

	}
}