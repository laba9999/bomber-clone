package com.amov.bomber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bomber.AndroidBridge;
import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.common.assets.SoundAssets;
import com.bomber.gamestates.GameStatePaused;
import com.bomber.remote.RemoteConnections;

public class AndroidGame extends AndroidApplication implements AndroidBridge
{

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (Game.mIsPVPGame)
			{
				if (Settings.PLAYING_ONLINE && !Game.mGameIsOver)
				{
					SharedPreferences.Editor editor = Settings.GAME_PREFS.edit();
					editor.putBoolean("abandonedGame", true);
					editor.commit();
				}

				SoundAssets.stop();
				SoundAssets.playMusic("intro", true, 1.0f);
			} else
			{
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (Game.mRemoteConnections != null)
			Game.mRemoteConnections.closeAll("Aplication exited!");

		Game.mRemoteConnections = null;
		GameStatePaused.mOptionsPanel = null;

		System.gc();

		// Para grandes males grandes remédios...
		// System.exit(1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);

		Settings.STARTED_FROM_DESKTOP = false;
		Settings.LIMPAR_SARAMPO = GameActivity.mDestroyed || SoundAssets.checkNullSounds();

		RemoteConnections.mIsGameServer = Settings.START_ANDROID_AS_SERVER;
		Game newGame = new Game(this, Settings.GAME_TYPE, Settings.LEVEL_TO_LOAD);
		initialize(newGame, false);

		if (!Game.mIsPVPGame)
			return;

		// new CreateConnections(newGame).start();
	}

	public void goBackToMenu()
	{
		SoundAssets.stop();
		SoundAssets.playMusic("intro", true, 1.0f);

		// finishActivity(0);
		// this.exit();

		goBackWithoutExiting();
	}

	public void goBackWithoutExiting()
	{
		SoundAssets.stop();
		Intent myIntent = new Intent(this, AssetsLoader.class);

		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myIntent);
	}

	public void showHelpActivity()
	{
		Intent myIntent = new Intent(this, HelpActivity.class);

		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(myIntent);
	}
}