package com.bomber.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;

public class Achievements {

	public static final String FILE_PATH = "com.amov.bomber";
	public static final int NUMBER_OF_ACHIEVEMENTS = 6;
	// Todo : ajustar
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
		String savepath =  FILE_PATH;

		// Cria a directoria se não existir
		File levelDirectory = new File(savepath);
		levelDirectory.mkdirs();

		savepath += "/achievements.txt";
		PrintStream out = null;
		try
		{
			out = new PrintStream(Gdx.files.external(savepath).write(false));
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
		} catch (Throwable t)
		{
			t.printStackTrace();
		}

		if (out != null)
			out.close();
	}

	public static void loadFile()
	{
		String[] achievementsInfo = new String[NUMBER_OF_ACHIEVEMENTS];

		Scanner scanner = null;
		InputStream inputStream = null;

		String loadPath = FILE_PATH + "/achievements.txt";
		try
		{
			inputStream = Gdx.files.external(loadPath).read();
			scanner = new Scanner(inputStream);
			short i = 0;
			while (scanner.hasNextLine())
			{
				String nextLine = scanner.nextLine();
				if (nextLine.length() > 0 && nextLine.charAt(0) != '#')
					achievementsInfo[i++] = nextLine;
			}

			mNumberMonsterKills = Integer.parseInt(achievementsInfo[0]);
			mNumberPlayersKills = Integer.parseInt(achievementsInfo[1]);
			mNumberCTFWins = Integer.parseInt(achievementsInfo[2]);
			mNumberDMWins = Integer.parseInt(achievementsInfo[3]);
			mTotalTimePlayed = Long.parseLong(achievementsInfo[4]);
			mHasCompletedCampaign = Boolean.parseBoolean(achievementsInfo[5]);

		} catch (Throwable t)
		{
			t.printStackTrace();
			reset();
		} finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				} catch (IOException e){}
			}
		}
	}
}