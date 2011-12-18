package com.bomber.gamestates;

import com.bomber.Game;
import com.bomber.common.Assets;
import com.bomber.common.Settings;
import com.bomber.gameobjects.UIMovableObject;
import com.bomber.input.InputPausedState;

public class GameStatePaused extends GameState {

	public static UIMovableObject mOptionsPanel = null;

	public GameStatePaused(Game _gameScreen) {
		super(_gameScreen);


		if (mOptionsPanel == null)
		{
			int animationSpeed = 25;
			int animationDuration = Assets.mScreens.get("pause").getRegionWidth() / animationSpeed;
			animationDuration *=2;
			float startX = 800 + Assets.mScreens.get("pause").getRegionWidth();
			mOptionsPanel = new UIMovableObject(animationSpeed, startX, 0, -1.0f, 0f, animationDuration);
			mOptionsPanel.mTexture = Assets.mScreens.get("pause") ;
			UIMovableObject soundButton = new UIMovableObject(0, startX + 7, 5, 0, 0,0);
			soundButton.mTexture = Assets.getSoundButtonTexture();
			mOptionsPanel.addChild(soundButton);
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
			mGame.setGameState(mNextGameState);
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

		for(UIMovableObject o : mOptionsPanel.mChilds)
		{
			mBatcher.draw(o.mTexture, o.mPosition.x, o.mPosition.y);
		}
	}
	
	
	@Override
	public void onFinish()
	{
		mOptionsPanel.mIsMoving = true;
		mOptionsPanel.mTicksCounter = 0;
		mOptionsPanel.setDirection(1, 0);
	}


}