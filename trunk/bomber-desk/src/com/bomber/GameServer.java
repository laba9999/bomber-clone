package com.bomber;

import java.io.IOException;

import com.bomber.remote.Message;
import com.bomber.remote.MessageType;
import com.bomber.remote.MessagesHandler;
import com.bomber.remote.Protocols;
import com.bomber.remote.RemoteConnections;
import com.bomber.remote.EventType;

public class GameServer extends Thread {

	RemoteConnections mConnections;
	MessagesHandler msgHandler;

	public GameServer() {
		mConnections = new RemoteConnections(true);
		msgHandler = new MessagesHandler(mConnections, null);
		try
		{
			mConnections.acceptConnections(Protocols.TCP, 50001, (short) 1);
			System.out.println("Server online!");
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void run()
	{
		while (true)
		{
			mConnections.update();

			msgHandler.parseNextMessage();

			Game.mCurrentTick++;
			try
			{
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
