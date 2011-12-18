package com.bomber;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.bomber.gametypes.GameType;
import com.bomber.remote.RemoteConnections;

public class BomberDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new LwjglApplication(new Game(RemoteConnections.create(GameType.CTF, DebugSettings.START_DESKTOP_AS_SERVER, DebugSettings.REMOTE_SERVER_ADDRESS, DebugSettings.REMOTE_SERVER_PORT)), "Bomber", 800, 480, false);
		// new LwjglApplication(new GameScreen(), "Bomber", 480, 320, false);

		// new GameServer().start();
		// new GameClient().start();
	}

}
