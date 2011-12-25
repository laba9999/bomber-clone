package com.amov.bomber;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bomber.common.assets.SoundAssets;

public class AssetsLoader extends AndroidApplication
{

	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data)
	{
		SoundAssets.mIsloaded = false;
		exit();
	}

	@Override
	protected void onCreate(Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

		initialize(new ApplicationListener()
		{
			private boolean startedMainActivity = false;

			public void resume(){}
			public void resize(int _width, int _height){}
			public void pause(){}
			public void dispose(){}
			
			public void create()
			{
				SoundAssets.load();
			}
			
			public void render()
			{
				if (SoundAssets.mIsloaded && !startedMainActivity)
				{
					startedMainActivity = true;

					Intent myIntent = new Intent(AssetsLoader.this, MainActivity.class);
					startActivityForResult(myIntent, 0);
				}

			}

		}, false);

	}

}
