package com.amov.bomber;

import com.bomber.common.assets.SoundAssets;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

public abstract class GameActivity extends Activity
{
	public static int NEXT_ACTIVITY = -1;
	
	private boolean startedActivity = false;
	
	@Override
	protected void onResume()
	{
		startedActivity = false;
		SoundAssets.resume();
		super.onResume();
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
			// desactiva animação na transição entre activities
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
