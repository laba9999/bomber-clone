package com.amov.bomber;

import android.os.Bundle;
import android.view.WindowManager.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bomber.GameScreen;


public class AndroidGame extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		initialize(new GameScreen(), false);
	}
}