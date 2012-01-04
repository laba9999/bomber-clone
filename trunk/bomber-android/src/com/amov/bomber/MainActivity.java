package com.amov.bomber;

import java.util.HashMap;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.bomber.Settings;
import com.bomber.common.Achievements;
import com.bomber.common.Strings;
import com.bomber.common.assets.SoundAssets;
import com.bomber.gametypes.GameTypeHandler;

public class MainActivity extends GameActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		loadSharedPreferences();

		SoundAssets.playMusic("intro", true, 0.6f);

		updateSoundButton(findViewById(R.id.imageButtonMainSound));

		Settings.GAME_TYPE = GameTypeHandler.CAMPAIGN;
		Settings.STARTED_FROM_DESKTOP = false;
		Achievements.loadFile();

		loadStrings();

		GameActivity.mDestroyed = false;

	}

	private void loadStrings()
	{
		if (Strings.mStrings != null) // já foram carregadas?
			return;

		final int[] GAME_STRINGS_RESOURCES = new int[] { R.string.game_connect_to, R.string.game_connecting, R.string.game_error_connecting, R.string.game_final_score, R.string.game_highscore,
				R.string.game_loading, R.string.game_lost_client, R.string.game_lost_server, R.string.game_no_suficient_clients, R.string.game_round, R.string.game_score, R.string.game_starts_in,
				R.string.game_starts_in_lowercase, R.string.game_time, R.string.game_waiting_clients, R.string.game_won, R.string.game_lost, R.string.bomber_champ };

		Strings.mStrings = new HashMap<String, String>(GAME_STRINGS_RESOURCES.length);
		String str;
		for (int i = 0; i < GAME_STRINGS_RESOURCES.length; i++)
		{
			str = getResources().getString(GAME_STRINGS_RESOURCES[i]);
			Strings.mStrings.put(Strings.GAME_STRINGS_KEYS[i], str);
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
