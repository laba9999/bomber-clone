package com.bomber.gamestates;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.gameobjects.UIMovableObject;
import com.bomber.input.InputPausedState;

public class GameStatePaused extends GameState {

	private static UIMovableObject mOptionsPanel = null;

	public GameStatePaused(GameScreen _gameScreen) {
		super(_gameScreen);


		if (mOptionsPanel == null)
		{
			int animationSpeed = 25;
			int animationDuration = Assets.mPauseScreen.getRegionWidth() / animationSpeed;
			animationDuration *=2;
			mOptionsPanel = new UIMovableObject(animationSpeed, 800 + Assets.mPauseScreen.getRegionWidth(), 0, -1.0f, 0f, animationDuration);
			mOptionsPanel.mTexture = Assets.mPauseScreen;
		}


		mInput = new InputPausedState(this);
	}

	

	@Override
	public void reset()
	{
		super.reset();
		mOptionsPanel.reset();
		mOptionsPanel.setDirection(-1.0f, 0f);
		mOptionsPanel.mIsMoving = true;
	}
	
	@Override
	public void onUpdate()
	{
		if (!mOptionsPanel.mIsMoving)
		{
			mInput.update();
			return;
		}

		mOptionsPanel.update();
	}

	@Override
	protected void onUpdateFinishing()
	{
		if (!mOptionsPanel.mIsMoving)
		{
			mGameScreen.setGameState(mNextGameState);
			return;
		}
		
		mOptionsPanel.update();
	}
	
	public void onPresent(float _interpolation)
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);

		// Desenha o vidro escuro
		mBatcher.enableBlending();
		mBatcher.draw(Assets.DarkGlass.get(), 0, 0);

		// Desenha o painel de opções
		mBatcher.draw(mOptionsPanel.mTexture, mOptionsPanel.mPosition.x, mOptionsPanel.mPosition.y);

	}
	
	
	@Override
	public void onFinish()
	{
		mOptionsPanel.mIsMoving = true;
		mOptionsPanel.mTicksCounter = 0;
		mOptionsPanel.setDirection(1, 0);
	}


}