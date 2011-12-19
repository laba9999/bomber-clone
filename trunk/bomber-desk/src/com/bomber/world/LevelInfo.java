package com.bomber.world;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;

public class LevelInfo {
	public String mCurrentLevelName;
	public String mNextLevelName;

	public short mMinutes;
	public short mSeconds;
	public short mNumberBonus;
	private int mHighScore;

	/**
	 * @param _levelInfo
	 *            Posição 0: Nome do nivelseguinte Posição 1: Minutos Posição 2:
	 *            Segundos Posição 3: Nº de bónus a spawnar 
	 */
	public void set(String[] _levelInfo)
	{
		mNextLevelName = _levelInfo[0];
		mMinutes = Short.valueOf(_levelInfo[1]);
		mSeconds = Short.valueOf(_levelInfo[2]);
		mNumberBonus = Short.valueOf(_levelInfo[3]);

		loadHighSore();
	}

	public int getHighScore()
	{
		return mHighScore;
	}

	public void setHighScore(int _newHighScore)
	{
		mHighScore = _newHighScore;
		saveHighscore();
	}

	private void loadHighSore()
	{
		String path = "com.amov.bomber/levels/" + mCurrentLevelName + "/highscore.txt";

		InputStream inputStream = null;
		Scanner scanner = null;

		try
		{
			inputStream = Gdx.files.external(path).read();
			scanner = new Scanner(inputStream);
			mHighScore = Integer.valueOf(scanner.nextLine());
		} catch (Throwable t)
		{
			t.printStackTrace();
			mHighScore = 0;
		} finally
		{
			if (inputStream != null)
				try
				{
					inputStream.close();
				} catch (IOException e)
				{
				}
		}
	}

	private void saveHighscore()
	{
		String path = Gdx.files.getExternalStoragePath() + "com.amov.bomber/levels/" + mCurrentLevelName;

		// Cria a directoria se não existir
		File levelDirectory = new File(path);
		levelDirectory.mkdirs();

		path += "/highscore.txt";
		BufferedWriter out = null;
		try
		{
			out = new BufferedWriter(new OutputStreamWriter(Gdx.files.absolute(path).write(false)));
			PrintWriter bw = new PrintWriter(out);
			bw.println(mHighScore);

		} catch (Throwable t)
		{
			t.printStackTrace();
		} finally
		{
			try
			{
				if (out != null)
					out.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
