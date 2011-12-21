package com.bomber;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.bomber.gamestates.GameStateLoadingPVP;
import com.bomber.remote.RemoteConnections;

public class BomberDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Game newGame = new Game(null, DebugSettings.GAME_TYPE, DebugSettings.LEVEL_TO_LOAD);
		new LwjglApplication(newGame, "Bomber", 800, 480, false);
		
		if(!Game.mIsPVPGame)
			return;
		
		RemoteConnections tmpConnections = RemoteConnections.create(DebugSettings.REMOTE_PROTOCOL_IN_USE, DebugSettings.START_DESKTOP_AS_SERVER, DebugSettings.REMOTE_SERVER_ADDRESS);
		if (tmpConnections == null)
			GameStateLoadingPVP.mFailedToConnectToServer = true;
		else
			newGame.setConnections(tmpConnections);
		
		// new LwjglApplication(new GameScreen(), "Bomber", 480, 320, false);

		// new GameServer().start();
		// new GameClient().start();
	}

}
