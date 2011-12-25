package com.amov.bomber;

import android.os.Bundle;
import android.view.View;

import com.bomber.DebugSettings;
import com.bomber.common.Achievements;
import com.bomber.common.assets.SoundAssets;

public class MainActivity extends GameActivity
{
	public static final int DIALOG_MULTIPLAYER = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Lê os settings
		DebugSettings.loadPreferences(getPreferences(0));
		DebugSettings.PLAYER_NAME = DebugSettings.GAME_PREFS.getString("playerName", null);

		// bota som!
		SoundAssets.mIsSoundActive = DebugSettings.GAME_PREFS.getBoolean("soundEnabled", true);
		SoundAssets.playMusic("intro", true, 0.6f);

		Achievements.loadFile();
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
