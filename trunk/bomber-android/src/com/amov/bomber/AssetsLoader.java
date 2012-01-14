package com.amov.bomber;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomber.Settings;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.common.assets.SoundAssets;

public class AssetsLoader extends AndroidApplication
{
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data)
	{
		Log.d("GAM", "AssetsLoader onActivityResult()");

		SoundAssets.mIsloaded = false;
		System.exit(0);
	}

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

		Log.d("GAM", "AssetsLoader onCreate()");

		GameActivity.mDestroyed = false;

		initialize(new ApplicationListener()
		{
			boolean startedMainActivity = false;
			SpriteBatch mBatcher;
			
			public void create()
			{
				Log.d("GAM", "AssetsLoader initialize - create()");

				int[] maxTextureSize = new int[1];
				Gdx.gl11.glGetIntegerv(GL11.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
				Log.i("glinfo", "Max texture size = " + maxTextureSize[0]);

				Log.d("GAM", "AssetsLoader initialize - create() LOADS START");
				loadStrings();
				loadSharedPreferences();
				Log.d("GAM", "AssetsLoader initialize - create() LOADS END");
				

				GfxAssets.loadBigFont();
				OrthographicCamera mUICamera = new OrthographicCamera(800, 480);
				mUICamera.position.set(800 / 2, 480 / 2, 0);
				mUICamera.update();
				mBatcher = new SpriteBatch();
				mBatcher.setProjectionMatrix(mUICamera.combined);
				
				SoundAssets.mIsloaded = false;
				
				Log.d("GAM", "AssetsLoader initialize - antes thread()");
				new AssetsLoaderThread().start();
				Log.d("GAM", "AssetsLoader initialize - depois thread()");
			}

			public void render()
			{
				Log.d("GAM", "AssetsLoader initialize - render()");
				
				if(startedMainActivity)
					return;
				
				if (SoundAssets.mIsloaded && !startedMainActivity)
				{
					startedMainActivity = true;
					GameActivity.mGoneBackToAssetsLoader = false;
					
					Intent myIntent = new Intent(AssetsLoader.this, MainActivity.class);
					startActivityForResult(myIntent, 0);
					
					mBatcher.dispose();
				}
				
				mBatcher.begin();
				Gdx.gl11.glClearColor(0.21f, 0.21f, 0.21f, 0.8f);
				Gdx.gl11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				
				GfxAssets.mBigFont.draw(mBatcher, Strings.mStrings.get("loading"), 320, 250);
				mBatcher.end();

			}

			public void resize(int _width, int _height)
			{
			}

			public void pause()
			{

			}

			public void resume()
			{				

			}

			public void dispose()
			{
			}

		}, false);
	}

	private void loadStrings()
	{
		if (Strings.mStrings != null) // já foram carregadas?
			return;

		final int[] GAME_STRINGS_RESOURCES = new int[] { R.string.game_connect_to, R.string.game_connecting, R.string.game_error_connecting, R.string.game_final_score, R.string.game_highscore,
				R.string.game_loading, R.string.game_lost_client, R.string.game_lost_server, R.string.game_no_suficient_clients, R.string.game_round, R.string.game_score, R.string.game_starts_in,
				R.string.game_starts_in_lowercase, R.string.game_time, R.string.game_waiting_clients, R.string.game_won, R.string.game_lost, R.string.bomber_champ };

		Strings.mStrings = new HashMap<String, String>(GAME_STRINGS_RESOURCES.length);
		String str;
		for (int i = 0; i < GAME_STRINGS_RESOURCES.length; i++)
		{
			str = getResources().getString(GAME_STRINGS_RESOURCES[i]);
			Strings.mStrings.put(Strings.GAME_STRINGS_KEYS[i], str);
		}
	}

	protected void loadSharedPreferences()
	{
			Settings.loadPreferences(getSharedPreferences("super_prefs", 0));
			Settings.PLAYER_NAME = Settings.GAME_PREFS.getString("playerName", null);
			SoundAssets.mIsSoundActive = Settings.GAME_PREFS.getBoolean("soundEnabled", true);
	}

}
