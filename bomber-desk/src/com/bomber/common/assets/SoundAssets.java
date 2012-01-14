package com.bomber.common.assets;

import java.util.HashMap;

import android.content.SharedPreferences;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.bomber.Settings;

/**
 * 
 * @author sPeC!
 * 
 */
public class SoundAssets {
	public static HashMap<String, Music> mMusics;
	public static HashMap<String, Sound> mSounds;

	public static boolean mIsSoundActive = false;
	public static boolean mIsloaded = false;
	public static boolean mFailedLoading = false;

	private static Music mMusicPlaying = null;

	public static String mLastMusicPlaying;
	private static boolean mMusicLooping;
	private static float mMusicVolume;

	private static boolean mPaused = false;

	private static short MAX_LOAD_RETRIES = 3;

	public static void load()
	{
		Log.d("GAM"," SoundAssets load()");
		short retries = 0;
		boolean clean = false;
		do
		{
			mFailedLoading = false;
			mMusicPlaying = null;

			if (mMusics != null)
			{
				mMusics.clear();
				mMusics = null;
				clean = true;
			}

			if (mMusics != null)
			{
				mSounds.clear();
				mSounds = null;
				clean = true;
			}

			if (clean)
			{
				System.gc();
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException ie)
				{
					ie.printStackTrace();
				}
			}
			Log.d("GAM"," SoundAssets load after clean()");

			mMusics = new HashMap<String, Music>(10);
			try
			{
				mMusics.put("intro", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_011.ogg")));
				mMusics.put("level1", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_009.ogg")));
				mMusics.put("level2", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_003.ogg")));
				mMusics.put("level3", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_004.ogg")));
				mMusics.put("level4", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_005.ogg")));
				mMusics.put("level5", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_006.ogg")));
				mMusics.put("level6", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_007.ogg")));
				mMusics.put("level7", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_008.ogg")));
				mMusics.put("level8", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_002.ogg")));
				mMusics.put("timeEnding", Gdx.audio.newMusic(Gdx.files.internal("sfx/time_ending.ogg")));

				mMusics.put("levelISEC", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_010.ogg")));

				mSounds = new HashMap<String, Sound>();
				mSounds.put("explosion", Gdx.audio.newSound(Gdx.files.internal("sfx/s_5.ogg")));
				mSounds.put("bling", Gdx.audio.newSound(Gdx.files.internal("sfx/ring.ogg")));
				mSounds.put("die", Gdx.audio.newSound(Gdx.files.internal("sfx/s_1.ogg")));
			} catch (Exception e)
			{
				mMusicPlaying = null;
				mFailedLoading = true;

				if (++retries == MAX_LOAD_RETRIES)
				{
					System.gc();
					try
					{
						Thread.sleep(100);
					} catch (InterruptedException ie)
					{
						ie.printStackTrace();
					}

					if (!Settings.STARTED_FROM_DESKTOP)
					{
						SharedPreferences.Editor edit = Settings.GAME_PREFS.edit();
						edit.putBoolean("soundEnabled", false);
						edit.commit();
					}
					mIsSoundActive = false;
				}
			}
		} while (mFailedLoading && retries < MAX_LOAD_RETRIES);

		Log.d("GAM"," SoundAssets load end ()");

		mIsloaded = true;
		
	}

	public static boolean checkNullSounds()
	{

		if (mMusics == null)
		{
			mIsloaded = false;
			return true;

		} else if (mMusics.get("intro") == null || mMusics.get("level1") == null || mMusics.get("level2") == null || mMusics.get("level3") == null || mMusics.get("level4") == null
				|| mMusics.get("level5") == null || mMusics.get("level6") == null || mMusics.get("level7") == null || mMusics.get("level8") == null || mMusics.get("timeEnding") == null
				|| mMusics.get("levelISEC") == null)
		{

			mIsloaded = false;
			return true;
		}

		if (mSounds == null)
		{
			mIsloaded = false;
			return true;
		} else if (mSounds.get("explosion") == null || mSounds.get("bling") == null || mSounds.get("die") == null)
		{

			mIsloaded = false;
			return true;

		}

		return false;
	}

	public static void playMusic(String _music, boolean _looping, float _volume)
	{
		if (mFailedLoading)
			return;

		if (mPaused)
		{
			resume();
			return;
		}

		mPaused = false;
		mLastMusicPlaying = _music;
		mMusicLooping = _looping;
		mMusicVolume = _volume;

		if (!mIsSoundActive)
			return;

		try
		{
			if (mMusicPlaying != null)
				mMusicPlaying.stop();

			mMusicPlaying = mMusics.get(_music);
			mMusicPlaying.setVolume(_volume);
			mMusicPlaying.setLooping(_looping);
			mMusicPlaying.play();
		} catch (Exception e)
		{
			mMusicPlaying = null;

			SharedPreferences.Editor edit = Settings.GAME_PREFS.edit();
			edit.putBoolean("soundEnabled", false);
			edit.commit();

			mIsSoundActive = false;
		}
	}

	public static void playSound(String _sound)
	{
		if (!mIsSoundActive || mFailedLoading)
			return;

		mSounds.get(_sound).play();
	}

	public static void resume()
	{
		if (mMusicPlaying == null || !mIsSoundActive || mFailedLoading)
			return;

		mMusicPlaying.play();
		mPaused = false;
	}

	public static void pause()
	{
		if (mMusicPlaying == null || mFailedLoading)
			return;

		mPaused = true;
		mMusicPlaying.pause();
	}

	public static void stop()
	{
		mPaused = false;
		mLastMusicPlaying = null;
		if (mMusicPlaying == null || mFailedLoading)
			return;

		mMusicPlaying.stop();
		mMusicPlaying = null;
	}

	public static void toggle()
	{
		if (mFailedLoading)
			return;

		mIsSoundActive = !mIsSoundActive;

		SharedPreferences.Editor edit = Settings.GAME_PREFS.edit();
		edit.putBoolean("soundEnabled", mIsSoundActive);
		edit.commit();

		if (!mIsSoundActive && mMusicPlaying != null)
			mMusicPlaying.stop();
		else
			playMusic(mLastMusicPlaying, mMusicLooping, mMusicVolume);
	}
}
