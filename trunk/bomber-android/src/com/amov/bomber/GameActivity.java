package com.amov.bomber;

import com.bomber.DebugSettings;
import com.bomber.common.Settings;
import com.bomber.common.assets.SoundAssets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public abstract class GameActivity extends Activity
{
	public static int NEXT_ACTIVITY = -1;
	
	private boolean startedActivity = false;
	
	public static boolean mDestroyed = true;

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		
	}

	@Override
	protected void onResume()
	{
		loadSharedPreferences();

		if(mDestroyed)
		{
			Intent myIntent = new Intent(this, AssetsLoader.class);		
			// proibe a anima��o na transi��o entre activities
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(myIntent);
			mDestroyed = false;
		}

		startedActivity = false;
		if(Settings.isSoundOn)
			SoundAssets.resume();
		super.onResume();
	}

	protected void loadSharedPreferences()
	{		
		if(DebugSettings.GAME_PREFS == null)
		{
			DebugSettings.loadPreferences(getSharedPreferences("super_prefs", 0));
			DebugSettings.PLAYER_NAME = DebugSettings.GAME_PREFS.getString("playerName", null);
			SoundAssets.mIsSoundActive = DebugSettings.GAME_PREFS.getBoolean("soundEnabled", true);
		}
	}
	
	protected void loadSoundAssets()
	{
		if(SoundAssets.mMusics == null || SoundAssets.mSounds == null)
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
		
		// proibe a anima��o na transi��o entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, NEXT_ACTIVITY);
	}
	

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
			
			// desactiva anima��o na transi��o entre activities
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
