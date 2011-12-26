package com.amov.bomber;


import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.bomber.DebugSettings;
import com.bomber.common.Achievements;
import com.bomber.common.Strings;
import com.bomber.common.assets.SoundAssets;


public class MainActivity extends GameActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Lê os settings
		DebugSettings.loadPreferences(getSharedPreferences("super_prefs", 0));
		DebugSettings.PLAYER_NAME = DebugSettings.GAME_PREFS.getString("playerName", null);

		// bota som!
		SoundAssets.mIsSoundActive = DebugSettings.GAME_PREFS.getBoolean("soundEnabled", true);
		SoundAssets.playMusic("intro", true, 0.6f);

		Achievements.loadFile();
		loadStrings();
	}

	
	private void loadStrings()
	{
		if(Strings.mStrings != null) //já foram carregadas?
			return;
		
		final int[] GAME_STRINGS_RESOURCES = new int[]{
			R.string.game_connect_to,
			R.string.game_connecting,
			R.string.game_error_connecting,
			R.string.game_final_score,
			R.string.game_highscore,
			R.string.game_loading,
			R.string.game_lost_client,
			R.string.game_lost_server,
			R.string.game_no_suficient_clients,
			R.string.game_round,
			R.string.game_score,
			R.string.game_starts_in,
			R.string.game_starts_in_lowercase,
			R.string.game_time,
			R.string.game_waiting_clients
		};

		Strings.mStrings = new HashMap<String, String>(Strings.NUMBER_GAME_STRINGS);
		String str;
		for(int i = 0; i < Strings.NUMBER_GAME_STRINGS; i++)
		{
			str = getResources().getString(GAME_STRINGS_RESOURCES[i]);
			Strings.mStrings.put(Strings.GAME_STRINGS_KEYS[i],str);
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
}
