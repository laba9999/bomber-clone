package com.bomber.world;

public class LevelInfo {
	public String mCurrentLevelName;
	public String mNextLevelName;

	public short mMinutes;
	public short mSeconds;
	public short mNumberBonus;
	public short mBonusSeed;

	/**
	 * @param _levelInfo
	 *            Posição 0: Nome do nivelseguinte
	 *            Posição 1: Minutos 
	 *            Posição 2: Segundos
	 *            Posição 3: Nº de bónus a spawnar
	 *            Posição 4: Seed a usar no spawn dos bónus
	 */
	public void set(String[] _levelInfo)
	{
		mNextLevelName = _levelInfo[0];
		mMinutes = Short.valueOf(_levelInfo[1]);
		mSeconds = Short.valueOf(_levelInfo[2]);
		mNumberBonus = Short.valueOf(_levelInfo[3]);
		mBonusSeed = Short.valueOf(_levelInfo[4]);
	}
}
