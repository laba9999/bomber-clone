package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.Game;
import com.bomber.common.Assets;
import com.bomber.world.Level;

public class GameStateLoading extends GameState {

	public static boolean mServerAuthorizedStart = false;
	public static boolean mFailedToConnectToServer = false;
	public static Integer mCountdownSeconds = -1;
	
	public GameStateLoading(Game _gameScreen) {
		super(_gameScreen);
		
		mServerAuthorizedStart = false;
		mFailedToConnectToServer = false;
		mCountdownSeconds = -1;
	}

	@Override
	public void onUpdate()
	{
		if (Game.mIsPVPGame && mFailedToConnectToServer)
			mGame.setGameState(new GameStateServerConnectionError(mGame, "Error connecting.."));
		
		if (Level.mIsLoaded)
		{
			if (Game.mIsPVPGame && !mServerAuthorizedStart)
				return;

			mGame.setGameState(new GameStatePlaying(mGame));
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

		
		if(mCountdownSeconds != -1)
			font.draw(mBatcher, "Inicio em " + mCountdownSeconds.toString(), 340, 250);
		else
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