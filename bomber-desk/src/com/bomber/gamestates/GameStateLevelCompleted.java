package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.input.InputLevelCompletedState;

public class GameStateLevelCompleted extends GameState {

	public GameStateLevelCompleted(GameScreen _gameScreen) {
		super(_gameScreen);
		mInput = new InputLevelCompletedState(this);
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
		
		BitmapFont font = Assets.mFont;
	
		font.draw(mBatcher,"NICE", 350 , 250);
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