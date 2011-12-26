package com.bomber.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.remote.RemoteConnections;

public class GameStateLoadingPVP extends GameStateLoading {

	public static boolean mServerAuthorizedStart = false;
	public static boolean mFailedToConnectToServer = false;
	public static Integer mCountdownSeconds = -1;
	public int mAnimationTicks = 0;
	public GameStateLoadingPVP(Game _gameScreen) {
		super(_gameScreen);

		mServerAuthorizedStart = false;
		//mFailedToConnectToServer = false;
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
	public void onPresent(float _interpolation)
	{
		mBatcher.setProjectionMatrix(mUICamera.combined);
		mBatcher.draw(GfxAssets.mScreens.get("background_gradient_grey"), 0, 0);
		BitmapFont font = GfxAssets.mFont;
		if (mCountdownSeconds != -1)
		{
			
			mBatcher.draw(GfxAssets.mWaitingAnimation.getKeyFrame(mAnimationTicks, true),270,215);
			mAnimationTicks++;
			font.draw(mBatcher, Strings.mStrings.get("starts_in") + mCountdownSeconds.toString(), 340, 250);

		}
		else
		{
			if (RemoteConnections.mIsGameServer)
			{
				mBatcher.draw(GfxAssets.mWaitingAnimation.getKeyFrame(mAnimationTicks, true),160,215);
				mAnimationTicks++;
				font.draw(mBatcher, Strings.mStrings.get("waiting_clients"), 230, 250);
				font.draw(mBatcher, Strings.mStrings.get("connect_to") + DebugSettings.LOCAL_SERVER_ADDRESS, 220, 200);
			} else
			{
				mBatcher.draw(GfxAssets.mWaitingAnimation.getKeyFrame(mAnimationTicks, true),250,215);
				mAnimationTicks++;
				font.draw(mBatcher,Strings.mStrings.get("connecting"), 320, 250);
			}
		}
	}

}
