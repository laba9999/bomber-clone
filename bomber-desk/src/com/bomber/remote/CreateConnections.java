package com.bomber.remote;


import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.gamestates.GameStateLoadingPVP;

public class CreateConnections extends Thread {

	Game mNewGame;
	
	@Override
	public void run()
	{
		RemoteConnections tmpConnections = RemoteConnections.create(Settings.REMOTE_PROTOCOL_IN_USE, Settings.START_ANDROID_AS_SERVER, Settings.REMOTE_SERVER_ADDRESS);
		if (tmpConnections == null)
			GameStateLoadingPVP.mFailedToConnectToServer = true;
		else
			mNewGame.setConnections(tmpConnections);
													
		super.run();
	}

	public CreateConnections(Game _newGame) {
		mNewGame = _newGame;
	}

}
