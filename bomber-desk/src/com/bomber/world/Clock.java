package com.bomber.world;

public class Clock {

	public boolean mReachedZero = false;

	private boolean mStartedClock = false;
	private boolean mIsPaused = false;
	private long mLastTimeMilis = 0;
	private long mTotalTimeEllapsed = 0;
	private int mUpdateInterval = 1000;

	private boolean mCompletedUpdateInterval = false;

	private long mCountdownValue;

	private String mLastTimeString;
	private StringBuilder mTimeStringBuilder = new StringBuilder("00:00");

	/**
	 * Prepara o clock para começar uma nova contagem decrescente.
	 * 
	 * @param _minutes
	 *            Número de minutos iniciais
	 * @param _seconds
	 *            Número de segundos iniciais
	 */
	public void reset(int _minutes, int _seconds)
	{
		mCountdownValue = _minutes * 60 * 1000 + _seconds * 1000;
		updateTimeString(_minutes, _seconds);
		mReachedZero = false;
		mStartedClock = false;
		mIsPaused = false;

		mUpdateInterval = 1000;
		mTotalTimeEllapsed = 0;
	}

	public boolean hasCompletedUpdateInterval()
	{
		boolean res = mCompletedUpdateInterval;

		mCompletedUpdateInterval = false;

		return res;
	}

	public void setUpdateInterval(int _value)
	{
		mUpdateInterval = _value;
	}

	public int getRemainingSeconds()
	{
		return (int) (mCountdownValue - mTotalTimeEllapsed);
	}

	private void updateTimeString(int _minutes, int _seconds)
	{
		if (_minutes > 9)
			mTimeStringBuilder.replace(0, 1, String.valueOf(_minutes));
		else
		{
			mTimeStringBuilder.setCharAt(0, '0');
			mTimeStringBuilder.replace(1, 2, String.valueOf(_minutes));
		}

		if (_seconds > 9)
			mTimeStringBuilder.replace(3, 5, String.valueOf(_seconds));
		else
		{
			mTimeStringBuilder.setCharAt(3, '0');
			mTimeStringBuilder.replace(4, 5, String.valueOf(_seconds));
		}

		mLastTimeString = mTimeStringBuilder.toString();
	}

	/**
	 * Inicia/reinicia o clock
	 */
	public void start()
	{
		if (!mIsPaused)
			mTotalTimeEllapsed = 0;

		mStartedClock = true;
		mIsPaused = false;
		mLastTimeMilis = System.currentTimeMillis();
	}

	/**
	 * Pausa o clock
	 */
	public void pause()
	{
		mIsPaused = true;
	}

	/**
	 * Obtém o tempo restante sob a forma de string.
	 */
	public String toString()
	{
		if (!mStartedClock)
			return "Erro: O clock não foi iniciado!";

		long interval = System.currentTimeMillis() - mLastTimeMilis;

		// Apenas recria a string caso já tenha passado mais de um segundo
		if (interval < mUpdateInterval || mIsPaused)
			return mLastTimeString;

		mTotalTimeEllapsed += 1000;
		mLastTimeMilis = System.currentTimeMillis();
		mCompletedUpdateInterval = true;

		interval = mCountdownValue - mTotalTimeEllapsed;
		interval /= 1000;

		int minutes = java.lang.Math.max(0, (int) (interval / 60));
		int seconds = java.lang.Math.max(0, (int) (interval % 60));

		updateTimeString(minutes, seconds);

		mReachedZero = (minutes == 0) && (seconds == 0);

		return mLastTimeString;
	}
}