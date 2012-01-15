package com.amov.bomber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.bomber.Settings;
import com.bomber.common.Achievements;
import com.bomber.common.assets.SoundAssets;
import com.bomber.gametypes.GameTypeHandler;

public class MainActivity extends GameActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(mGoneBackToAssetsLoader)
		{
			finish();
			return;
		}
		
		Log.d("GAM", "MainActivity onCreate()");

		setContentView(R.layout.main);
		SoundAssets.playMusic("intro", true, 1.0f);

		updateSoundButton(findViewById(R.id.imageButtonMainSound));

		Settings.GAME_TYPE = GameTypeHandler.CAMPAIGN;
		Settings.STARTED_FROM_DESKTOP = false;
		Achievements.loadFile();


		if (SoundAssets.mFailedLoading)
		{
			try
			{
				AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
				alertDialog.setTitle(getResources().getString(R.string.warn_failed_load_sounds_title));
				alertDialog.setMessage(getResources().getString(R.string.warn_failed_load_sounds_text));

				alertDialog.setButton("OK", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();

					}
				});

				alertDialog.show();
			} catch (Exception e)
			{
			}
		}
	}


	public void onArcadeButton(View v)
	{
		launchActivity(LevelChooserActivity.class);
	}

	public void onMultiplayerButton(View v)
	{
		launchActivity(MultiplayerConnectionActivity.class);
	}

	public void onTopButton(View v)
	{
		launchActivity(TopActivity.class);
	}

	public void onAchievementsButton(View v)
	{
		launchActivity(AchievementsActivity.class);
	}

	public void onHelpButton(View v)
	{
		launchActivity(HelpActivity.class);
	}

	public void onInfoButton(View v)
	{
		launchActivity(AboutActivity.class);
	}

	public void onSoundButton(View v)
	{
		SoundAssets.toggle();
		updateSoundButton(v);
	}

	private void updateSoundButton(View v)
	{
		if (!SoundAssets.mIsSoundActive)
		{
			ImageButton b = (ImageButton) v;
			b.setBackgroundResource(R.drawable.menu_button_sound_off);
		} else
		{
			ImageButton b = (ImageButton) v;
			b.setBackgroundResource(R.drawable.menu_button_sound_on);
		}
	}

}
