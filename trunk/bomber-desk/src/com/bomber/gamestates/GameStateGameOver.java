package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.input.InputGameOverState;
import com.bomber.input.InputPlayingState;
import com.bomber.world.Level;

public class GameStateGameOver extends GameState {

	public GameStateGameOver(GameScreen _gameScreen) {
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
		mBatcher.draw(Assets.mGameOverScreen,125,60);
		font.setScale(1.8f);
		font.draw(mBatcher, Level.mInfo.mCurrentLevelName, 320, 405);
		font.setScale(1);
		
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