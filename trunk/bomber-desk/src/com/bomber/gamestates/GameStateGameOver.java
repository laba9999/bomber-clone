package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.Game;
import com.bomber.common.Assets;
import com.bomber.input.InputGameOverState;
import com.bomber.world.Level;

public class GameStateGameOver extends GameState {

	public GameStateGameOver(Game _gameScreen) {
		super(_gameScreen);

		mInput = new InputGameOverState(this);
	}

	@Override
	public void onUpdate()
	{
		mInput.update();
	}

	public void onPresent(float _interpolation)
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);

		// Desenha o vidro escuro
		mBatcher.enableBlending();
		mBatcher.draw(Assets.DarkGlass.get(), 0, 0);
		
		BitmapFont font = Assets.mFont;		
		mBatcher.draw(Assets.mScreens.get("gameover") ,125,60);
		font.setScale(1.8f);
		font.draw(mBatcher, Level.mInfo.mCurrentLevelName, 320, 405);
		font.setScale(1);
		
	}

	@Override
	protected void onFinish()
	{
		mGameScreen.setGameState(mNextGameState);
	}

	@Override
	protected void onUpdateFinishing()
	{
		// TODO Auto-generated method stub

	}
}