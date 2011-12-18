package com.amov.bomber;

import android.os.Bundle;
import android.view.WindowManager.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.gametypes.GameType;
import com.bomber.remote.RemoteConnections;

public class AndroidGame extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub

		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		initialize(new Game(RemoteConnections.create(GameType.CTF, DebugSettings.START_ANDROID_AS_SERVER, DebugSettings.REMOTE_SERVER_ADDRESS, DebugSettings.REMOTE_SERVER_PORT)), false);
	}	
}