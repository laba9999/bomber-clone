package com.amov.bomber;

import java.io.File;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.bomber.common.Achievements;

public class MainActivity extends Activity
{
	public static final int DIALOG_MULTIPLAYER = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		loadAchievements();
	}

	private void loadAchievements()
	{
		File f = null;
		Scanner scanner = null;
		String[] achievementsInfo = new String[Achievements.NUMBER_OF_ACHIEVEMENTS];

		try
		{	
			f = new File(Environment.getExternalStorageDirectory() + "/" +  Achievements.FILE_PATH);
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
			
			
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			Achievements.reset();
		}
		finally
		{
			if(scanner != null)
			{
				scanner.close();
			} 

		}
	}

	public void onArcadeButton(View v)
	{
		Intent myIntent = new Intent(this, LevelChooserActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);
	}

	public void onMultiplayerButton(View v)
	{
		
		Intent myIntent = new Intent(this, MultiplayerConnectionActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);	

	}
		
	
	public void onTopButton(View v)
	{
		Intent myIntent = new Intent(this, TopActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);
	}

	public void onAchievementsButton(View v)
	{
		Intent myIntent = new Intent(this, AchievementsActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);
	}

	public void onHelpButton(View v)
	{
		Intent myIntent = new Intent(this, HelpActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);
	}
}
