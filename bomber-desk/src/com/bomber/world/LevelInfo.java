package com.bomber.world;

public class LevelInfo {
	public String mCurrentLevelName;
	public String mNextLevelName;

	public short mMinutes;
	public short mSeconds;

	/**
	 * @param _levelInfo
	 *            Posição 0: Nome do nivelseguinte
	 *            Posição 1: Minutos 
	 *            Posição 2: Segundos
	 */
	public void set(String[] _levelInfo)
	{
		mNextLevelName = _levelInfo[0];
		mMinutes = Short.valueOf(_levelInfo[1]);
		mSeconds = Short.valueOf(_levelInfo[2]);
	}
}
