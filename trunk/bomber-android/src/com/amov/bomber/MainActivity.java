package com.amov.bomber;

import java.io.File;
import java.util.Scanner;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

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

		// bota som!
		SoundAssets.playMusic("intro", true, 0.6f);

		loadAchievements();
	}

	private void loadAchievements()
	{
		File f = null;
		Scanner scanner = null;
		String[] achievementsInfo = new String[Achievements.NUMBER_OF_ACHIEVEMENTS];

		try
		{
			f = new File(Environment.getExternalStorageDirectory() + "/" + Achievements.FILE_PATH);
			scanner = new Scanner(f);

			short i = 0;
			while (scanner.hasNextLine())
			{
				String nextLine = scanner.nextLine();
				if (nextLine.length() > 0 && nextLine.charAt(0) != '#')
					achievementsInfo[i++] = nextLine;
			}

			Achievements.mNumberMonsterKills = Integer.parseInt(achievementsInfo[0]);
			Achievements.mNumberPlayersKills = Integer.parseInt(achievementsInfo[1]);
			Achievements.mNumberCTFWins = Integer.parseInt(achievementsInfo[2]);
			Achievements.mNumberDMWins = Integer.parseInt(achievementsInfo[3]);
			Achievements.mTotalTimePlayed = Long.parseLong(achievementsInfo[4]);
			Achievements.mHasCompletedCampaign = Boolean.parseBoolean(achievementsInfo[5]);

		} catch (Throwable t)
		{
			t.printStackTrace();
			Achievements.reset();
		} finally
		{
			if (scanner != null)
			{
				scanner.close();
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
}
