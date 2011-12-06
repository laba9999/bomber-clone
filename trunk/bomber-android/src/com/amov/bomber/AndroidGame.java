package com.amov.bomber;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bomber.GameScreen;


public class AndroidGame extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initialize(new GameScreen(), false);
	}
}