package com.bomber;

import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.bomber.remote.Protocols;
import com.bomber.remote.RemoteConnections;

public class BomberDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new LwjglApplication(new Game(createConnections()), "Bomber", 800, 480, false);
		// new LwjglApplication(new GameScreen(), "Bomber", 480, 320, false);

		 //new GameServer().start();
		 //new GameClient().start();
	}
	
	private static RemoteConnections createConnections()
	{
		RemoteConnections connections = new RemoteConnections(false);

		try
		{
			connections.connectToGameServer(Protocols.TCP, "188.81.64.109", 50005);
			//Game.LOGGER.log("Connectado ao servidor!");
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return connections;
	}
}
