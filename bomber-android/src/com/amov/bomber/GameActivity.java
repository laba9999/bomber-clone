package com.amov.bomber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.bomber.Settings;
import com.bomber.common.assets.SoundAssets;

public abstract class GameActivity extends Activity
{
	private boolean startedActivity = false;

	public static boolean mDestroyed = false;
	public static boolean mGoneBackToAssetsLoader = false;

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		if (Settings.DEBUG_MODE)
			Log.d("GAM", "GameACtivity onCreate()");

		if (mDestroyed || SoundAssets.checkNullSounds())
		{
			if (Settings.DEBUG_MODE)
				Log.d("GAM", "GameActivity onCreate() inside if!");
			mGoneBackToAssetsLoader = true;
			Intent myIntent = new Intent(this, AssetsLoader.class);
			// proibe a animação na transição entre activities
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(myIntent);
			mDestroyed = false;
			finish();
			return;
		}

		loadSharedPreferences();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(Settings.DEBUG_MODE)
		Log.d("GAM", "GameACtivity onResume()");

		if ((mDestroyed || SoundAssets.checkNullSounds()) && !mGoneBackToAssetsLoader)
		{
			if(Settings.DEBUG_MODE)
			Log.d("GAM", "GameActivity onResume() inside if!");
			mGoneBackToAssetsLoader = true;

			Intent myIntent = new Intent(this, AssetsLoader.class);
			// proibe a animação na transição entre activities
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(myIntent);
			mDestroyed = false;
			finish();
			return;
		}

		startedActivity = false;
		SoundAssets.resume();
	}

	protected void loadSharedPreferences()
	{
		Settings.loadPreferences(getSharedPreferences("super_prefs", 0));
		Settings.PLAYER_NAME = Settings.GAME_PREFS.getString("playerName", null);
		SoundAssets.mIsSoundActive = Settings.GAME_PREFS.getBoolean("soundEnabled", true);
	}

	protected void loadSoundAssets()
	{
		if (SoundAssets.mMusics == null || SoundAssets.mSounds == null)
		{
			SoundAssets.load();
		}
	}

	@Override
	protected void onPause()
	{
		if (!startedActivity)
			SoundAssets.pause();

		super.onPause();
	}

	protected void launchActivity(Class<?> cls)
	{
		startedActivity = true;
		Intent myIntent = new Intent(this, cls);

		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(myIntent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_OK, resultIntent);
			finish();

			// desactiva animação na transição entre activities
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
