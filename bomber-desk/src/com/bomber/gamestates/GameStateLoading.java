package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.world.Level;

public class GameStateLoading extends GameState {


	public GameStateLoading(GameScreen _gameScreen) {
		super(_gameScreen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUpdate()
	{
		if(Level.mIsLoaded) 
		{
			mGameScreen.mGameState = new GameStatePlaying(mGameScreen);
		}
		
	}

	public void parseInput()
	{
		throw new UnsupportedOperationException();
	}

	public void onPresent(float _interpolation)
	{
		mBatcher.setProjectionMatrix(mUICamera.combined);		
		BitmapFont font = Assets.mFont;
	
		//desenha "paused" ao canto
		font.draw(mBatcher,"LOADING", 600 , 470);

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