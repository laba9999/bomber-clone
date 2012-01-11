package com.bomber;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.bomber.common.Strings;
import com.bomber.gamestates.GameStateLoadingPVP;
import com.bomber.remote.CreateConnections;
import com.bomber.remote.RemoteConnections;

public class BomberDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RemoteConnections.mIsGameServer = Settings.START_DESKTOP_AS_SERVER;
		Game newGame = new Game(null, Settings.GAME_TYPE, Settings.LEVEL_TO_LOAD);
		new LwjglApplication(newGame, "Bomber", 800, 480, false);

		Strings.loadForGdx();
		
		if (!Game.mIsPVPGame)
			return;

//		RemoteConnections.mIsGameServer = Settings.START_DESKTOP_AS_SERVER;
//		new CreateConnections(newGame).start();
		/*RemoteConnections tmpConnections = RemoteConnections.create(Settings.REMOTE_PROTOCOL_IN_USE, Settings.START_DESKTOP_AS_SERVER, Settings.REMOTE_SERVER_ADDRESS);
		
		if (tmpConnections == null)
			GameStateLoadingPVP.mFailedToConnectToServer = true;
		else
			newGame.setConnections(tmpConnections);
	*/
	}

}
