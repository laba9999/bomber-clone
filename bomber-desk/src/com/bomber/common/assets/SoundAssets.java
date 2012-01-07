package com.bomber.common.assets;

import java.util.HashMap;

import android.content.SharedPreferences;

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

	public static void load()
	{
		mFailedLoading = false;
		mMusicPlaying = null;

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

			SharedPreferences.Editor edit = Settings.GAME_PREFS.edit();
			edit.putBoolean("soundEnabled", mIsSoundActive);
			edit.commit();
		}

		mIsloaded = true;
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
			edit.putBoolean("soundEnabled", mIsSoundActive);
			edit.commit();
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
