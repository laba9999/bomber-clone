package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.remote.Protocols;
import com.bomber.remote.RemoteConnections;

public class GameStateLoadingPVP extends GameStateLoading {

	public static boolean mServerAuthorizedStart = false;
	public static boolean mFailedToConnectToServer = false;
	public static Integer mCountdownSeconds = -1;
	public int mAnimationTicks = 0;

	private final String mStringWaitingClients = Strings.mStrings.get("waiting_clients");
	private final String mStringConnectTo = Strings.mStrings.get("connect_to") + Settings.LOCAL_SERVER_ADDRESS;
	private final String mStringConnection = Strings.mStrings.get("connecting");
	private final String mStringStartsIn = Strings.mStrings.get("starts_in");

	public GameStateLoadingPVP(Game _gameScreen) {
		super(_gameScreen);

		mServerAuthorizedStart = false;
		mCountdownSeconds = -1;
	}

	@Override
	public void onUpdate()
	{
		if (mFailedToConnectToServer)
			mGame.setGameState(new GameStateServerConnectionError(mGame, Strings.mStrings.get("error_connecting")));

		if (!mServerAuthorizedStart || Game.mGameIsOver)
			return;

		super.onUpdate();
	}

	@Override
	public void onPresent()
	{
		mBatcher.setProjectionMatrix(mUICamera.combined);
		
		BitmapFont font = GfxAssets.mGenericFont;

		TextBounds tx;
		if (mCountdownSeconds != -1)
		{

			tx = font.getBounds(mStringStartsIn);
			mAnimationTicks++;

			mBatcher.draw(GfxAssets.mWaitingAnimation.getKeyFrame(mAnimationTicks, true), (400 - tx.width / 2) - 60, 225);
			font.draw(mBatcher, mStringStartsIn + mCountdownSeconds.toString(), 400 - tx.width / 2, 260);

		} else
		{
			if (RemoteConnections.mIsGameServer)
			{
				tx = font.getBounds(mStringWaitingClients);
				mBatcher.draw(GfxAssets.mWaitingAnimation.getKeyFrame(mAnimationTicks, true), 380, 150);
				mAnimationTicks++;
				font.draw(mBatcher, mStringWaitingClients, 400 - tx.width / 2, 280);
				
				if (Settings.REMOTE_PROTOCOL_IN_USE != Protocols.BLUETOOTH)
				{
					tx = font.getBounds(mStringConnectTo);
					font.draw(mBatcher, mStringConnectTo, 400 - tx.width / 2, 250);
				}
			} else
			{
				mAnimationTicks++;
				tx = font.getBounds(mStringConnection);
				mBatcher.draw(GfxAssets.mWaitingAnimation.getKeyFrame(mAnimationTicks, true), (400 - tx.width / 2) - 60, 225);

				font.draw(mBatcher, mStringConnection, 400 - tx.width / 2, 260);
			}
		}
	}
}
