package com.amov.bomber;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bomber.AndroidBridge;
import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.gamestates.GameStateLoadingPVP;
import com.bomber.remote.RemoteConnections;

public class AndroidGame extends AndroidApplication implements AndroidBridge
{
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (Game.mRemoteConnections != null)
			Game.mRemoteConnections.closeAll("Aplication exited!");

		// Para grandes males grandes remédios...
		// System.exit(1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);

		Game newGame = new Game(this, DebugSettings.GAME_TYPE, DebugSettings.LEVEL_TO_LOAD);
		initialize(newGame, false);

		if (!Game.mIsPVPGame)
			return;

		RemoteConnections tmpConnections = RemoteConnections.create(DebugSettings.REMOTE_PROTOCOL_IN_USE, DebugSettings.START_ANDROID_AS_SERVER, DebugSettings.REMOTE_SERVER_ADDRESS);
		if (tmpConnections == null)
			GameStateLoadingPVP.mFailedToConnectToServer = true;
		else
			newGame.setConnections(tmpConnections);
	}

	public void goBackToMenu()
	{
		finishActivity(0);
		this.exit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			// Tira o efeito ao botão back
			// return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}