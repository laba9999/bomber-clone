package com.bomber.world;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LevelInfo {
	public String mCurrentLevelName;
	public String mNextLevelName;

	public short mMinutes;
	public short mSeconds;
	public short mNumberBonus;
	public short mBonusSeed;
	public int mHighScore;
	/**
	 * @param _levelInfo
	 *            Posição 0: Nome do nivelseguinte
	 *            Posição 1: Minutos 
	 *            Posição 2: Segundos
	 *            Posição 3: Nº de bónus a spawnar
	 *            Posição 4: Seed a usar no spawn dos bónus
	 *            Posição 5: Pontuação Máxima actual
	 */
	public void set(String[] _levelInfo)
	{
		mNextLevelName = _levelInfo[0];
		mMinutes = Short.valueOf(_levelInfo[1]);
		mSeconds = Short.valueOf(_levelInfo[2]);
		mNumberBonus = Short.valueOf(_levelInfo[3]);
		mBonusSeed = Short.valueOf(_levelInfo[4]);
		mHighScore = Short.valueOf(_levelInfo[5]);
	}
	
	public void writeToFile()
	{
		//FileHandle fh = Gdx.files.internal("levels/" + mCurrentLevelName + "/info.txt");
		FileHandle fh = Gdx.files.external("./assets/levels/" + mCurrentLevelName + "/info.txt");
		
		BufferedWriter bw = new BufferedWriter(fh.writer(false));
		
		try {
			bw.write("#nome do próximo nível");
			bw.write(mNextLevelName);
			bw.write("#minutos");
			bw.write(mMinutes);
			bw.write("#segundos");
			bw.write(mSeconds);
			bw.write("#nº de bónus");
			bw.write(mNumberBonus);
			bw.write("#seed para os bónus");
			bw.write(mBonusSeed);
			bw.write("#highscore");			
			bw.write(mHighScore);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
