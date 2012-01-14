package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.common.assets.Level;
import com.bomber.common.assets.SoundAssets;
import com.bomber.input.InputGameOverState;

public class GameStateGameOver extends GameState {

	public GameStateGameOver(Game _gameScreen) {
		super(_gameScreen);

		Game.mGameIsOver = true;
		
		if(!Settings.STARTED_FROM_DESKTOP)
			SoundAssets.stop();
		
		mInput = new InputGameOverState(this);
	}

	@Override
	public void onUpdate()
	{
		mInput.update();
	}

	public void onPresent()
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);

		// Desenha o vidro escuro
		mBatcher.enableBlending();
		mBatcher.draw(GfxAssets.Pixmaps.getDarkGlass(), 0, 0);
		
		BitmapFont font = GfxAssets.mGenericFont;		
		mBatcher.draw(GfxAssets.mScreens.get("gameover") ,125,60);
		
		font = GfxAssets.mBigFont;
		font.draw(mBatcher, Level.mInfo.mCurrentLevelName, 350, 405);
	}

	@Override
	protected void onFinish()
	{
		mGame.setGameState(mNextGameState);
	}

	@Override
	protected void onUpdateFinishing()
	{
	}
}