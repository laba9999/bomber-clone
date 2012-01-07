package com.amov.bomber;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomber.Settings;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.common.assets.SoundAssets;
import com.bomber.renderers.WorldRenderer;

public class AssetsLoader extends AndroidApplication
{

	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data)
	{
		SoundAssets.mIsloaded = false;
		System.exit(0);
	}

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		Settings.loadPreferences(getSharedPreferences("super_prefs", 0));

		
		initialize(new ApplicationListener()
		{
			private boolean startedMainActivity = false;

			private SpriteBatch mBatcher;
			private OrthographicCamera mUICamera;
			
			public void resume(){}
			public void resize(int _width, int _height){}
			public void pause(){}
			public void dispose(){}
			
			public void create()
			{
				SoundAssets.load();
				loadSharedPreferences();
				loadStrings();
				GfxAssets.loadAssets();
				
				mUICamera = new OrthographicCamera(800, 480);
				mUICamera.position.set(800 / 2, 480 / 2, 0);
				mUICamera.update();

				mBatcher = new SpriteBatch();
				
			}
			
			public void render()
			{
				mBatcher.begin();
				mBatcher.setProjectionMatrix(mUICamera.combined);
				BitmapFont font = GfxAssets.mBigFont;
				mBatcher.draw(GfxAssets.Pixmaps.getGrey(), 0, 0);
				font.draw(mBatcher, Strings.mStrings.get("loading"), 320, 250);
				mBatcher.end();
				
				if (SoundAssets.mIsloaded && !startedMainActivity)
				{
					
					startedMainActivity = true;

					Intent myIntent = new Intent(AssetsLoader.this, MainActivity.class);
					startActivityForResult(myIntent, 0);
				}
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
		if (Settings.GAME_PREFS == null)
		{
			Settings.loadPreferences(getSharedPreferences("super_prefs", 0));
			Settings.PLAYER_NAME = Settings.GAME_PREFS.getString("playerName", null);
			SoundAssets.mIsSoundActive = Settings.GAME_PREFS.getBoolean("soundEnabled", true);
		}
	}

	

}
