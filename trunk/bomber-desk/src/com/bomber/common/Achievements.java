package com.bomber.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;


public class Achievements {
	
	public static final String FILE_PATH = "com.amov.bomber/achievements.txt";
	public static final int NUMBER_OF_ACHIEVEMENTS = 6;
	//Todo : ajustar
	public static final int MONSTER_KILLS_GOAL = 1;
	public static final int PLAYER_KILLS_GOAL = 2;
	public static final int CTF_WINS_GOAL = 3;
	public static final int DM_WINS_GOAL = 4;
	public static final long TIME_PLAYED_GOAL = 5;

	public static int mNumberMonsterKills;
	public static int mNumberPlayersKills;
	public static int mNumberCTFWins;
	public static int mNumberDMWins;
	public static long mTotalTimePlayed;
	public static boolean mHasCompletedCampaign;

	

	
	public static boolean isMonsterKillsCompleted()
	{
		return mNumberMonsterKills >= MONSTER_KILLS_GOAL;
	}
	
	public static boolean isPlayerKillsCompleted()
	{
		return mNumberPlayersKills >= PLAYER_KILLS_GOAL;
	}
	
	public static boolean isCTFWinsCompleted()
	{
		return mNumberCTFWins >= CTF_WINS_GOAL;
	}
	
	public static boolean isDMWinsCompleted()
	{
		return mNumberDMWins >= DM_WINS_GOAL;
	}
	
	public static boolean isTimePlayedCompleted()
	{
		return mTotalTimePlayed >= TIME_PLAYED_GOAL;
	}
	
	public static boolean isCampaignCompleted()
	{
		return mHasCompletedCampaign;
	}

	
	public static void reset()
	{
		mNumberMonsterKills = 0;
		mNumberPlayersKills = 0;
		mNumberCTFWins = 0;
		mNumberDMWins = 0;
		mTotalTimePlayed = 0;
		mHasCompletedCampaign = false;
	}
	
	public static void saveFile()
	{
		String savepath = Gdx.files.getExternalStoragePath() + FILE_PATH;
		PrintStream out = new PrintStream(Gdx.files.absolute(savepath).write(false));
		out.println("#monster kills");		
		out.println(mNumberMonsterKills);
		out.println("#players kills");
		out.println(mNumberPlayersKills);
		out.println("#ctf wins");
		out.println(mNumberCTFWins);
		out.println("#dm wins");
		out.println(mNumberDMWins);
		out.println("#total time played");
		out.println(mTotalTimePlayed);
		out.println("#completed campaign");
		out.println(mHasCompletedCampaign);
		out.close();
	}
	
}