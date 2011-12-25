package com.bomber.common.assets;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author sPeC!
 * 
 */
public class SoundAssets {
	private static HashMap<String, Music> mMusics;

	public static Sound mExplosion;
	public static Sound mRing;
	public static Sound mDie;
	
	public static boolean mIsloaded = false;

	private static Music mMusicPlaying = null;

	public static void load()
	{
		mMusicPlaying = null;
		
		mMusics = new HashMap<String, Music>(10);

		mMusics.put("intro", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_011.ogg")));
		mMusics.put("level1", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_002.ogg")));
		mMusics.put("level2", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_003.ogg")));
		mMusics.put("level3", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_004.ogg")));
		mMusics.put("level4", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_005.ogg")));
		mMusics.put("level5", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_006.ogg")));
		mMusics.put("level6", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_007.ogg")));
		mMusics.put("level7", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_008.ogg")));
		mMusics.put("level8", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_009.ogg")));
		mMusics.put("pvp", Gdx.audio.newMusic(Gdx.files.internal("sfx/m_010.ogg")));

		mExplosion = Gdx.audio.newSound(Gdx.files.internal("sfx/s_5.ogg"));
		mRing = Gdx.audio.newSound(Gdx.files.internal("sfx/ring.ogg"));
		mDie = Gdx.audio.newSound(Gdx.files.internal("sfx/s_1.ogg"));
		
		mIsloaded = true;
	}

	public static void play(String _music, boolean _looping, float _volume)
	{
		if (mMusicPlaying != null)
			mMusicPlaying.stop();
		
		mMusicPlaying = mMusics.get(_music);
		mMusicPlaying.setVolume(_volume);
		mMusicPlaying.setLooping(_looping);
		mMusicPlaying.play();
	}

	public static void resume()
	{
		if (mMusicPlaying == null)
			return;

		mMusicPlaying.play();
	}

	public static void pause()
	{
		if (mMusicPlaying == null)
			return;

		mMusicPlaying.pause();
	}

	public static void stop()
	{
		if (mMusicPlaying == null)
			return;

		mMusicPlaying.stop();
		mMusicPlaying = null;
	}
}
