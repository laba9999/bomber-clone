package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.common.assets.Assets;
import com.bomber.remote.RemoteConnections;

public class GameStateLoadingPVP extends GameStateLoading {

	public static boolean mServerAuthorizedStart = false;
	public static boolean mFailedToConnectToServer = false;
	public static Integer mCountdownSeconds = -1;

	public GameStateLoadingPVP(Game _gameScreen) {
		super(_gameScreen);

		mServerAuthorizedStart = false;
		// mFailedToConnectToServer = false;
		mCountdownSeconds = -1;
	}

	@Override
	public void onUpdate()
	{
		if (mFailedToConnectToServer)
			mGame.setGameState(new GameStateServerConnectionError(mGame, "Error connecting.."));

		if (!mServerAuthorizedStart || Game.mGameIsOver)
			return;

		super.onUpdate();
	}

	@Override
	public void onPresent(float _interpolation)
	{
		mBatcher.setProjectionMatrix(mUICamera.combined);
		BitmapFont font = Assets.mFont;

		if (mCountdownSeconds != -1)
			font.draw(mBatcher, "Inicio em " + mCountdownSeconds.toString(), 340, 250);
		else
		{
			if (RemoteConnections.mIsGameServer)
			{
				font.draw(mBatcher, "A esperar por clientes... ", 230, 250);
				font.draw(mBatcher, "Ligar a - " + DebugSettings.LOCAL_SERVER_ADDRESS, 220, 200);
			} else
				font.draw(mBatcher, "A ligar... ", 320, 250);
		}
	}

}
