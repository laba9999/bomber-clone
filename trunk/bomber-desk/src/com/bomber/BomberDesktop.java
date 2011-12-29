package com.bomber;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.bomber.common.Strings;
import com.bomber.gamestates.GameStateLoadingPVP;
import com.bomber.remote.RemoteConnections;

public class BomberDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Game newGame = new Game(null, Settings.GAME_TYPE, Settings.LEVEL_TO_LOAD);
		new LwjglApplication(newGame, "Bomber", 800, 480, false);

		Strings.loadForGdx();
		
		if (!Game.mIsPVPGame)
			return;

		RemoteConnections.mIsGameServer = Settings.START_DESKTOP_AS_SERVER;
		
		RemoteConnections tmpConnections = RemoteConnections.create(Settings.REMOTE_PROTOCOL_IN_USE, Settings.START_DESKTOP_AS_SERVER, Settings.REMOTE_SERVER_ADDRESS);
		if (tmpConnections == null)
			GameStateLoadingPVP.mFailedToConnectToServer = true;
		else
			newGame.setConnections(tmpConnections);

		// new LwjglApplication(new GameScreen(), "Bomber", 480, 320, false);

		// new GameServer().start();
		// new GameClient().start();
	}

}
