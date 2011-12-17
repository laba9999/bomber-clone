package com.bomber;

import java.io.IOException;

import com.bomber.remote.Message;
import com.bomber.remote.MessageType;
import com.bomber.remote.MessagesHandler;
import com.bomber.remote.Protocols;
import com.bomber.remote.RemoteConnections;
import com.bomber.remote.EventType;

public class GameClient extends Thread {
	RemoteConnections mConnections;
	MessagesHandler msgHandler;
	
	public GameClient() {
		mConnections = new RemoteConnections(false);
		msgHandler = new MessagesHandler(mConnections, null);

		Message m = new Message();
		m.messageType = MessageType.BOMB;
		m.eventType = EventType.CREATE;
		m.senderID = 1;
		m.UUID = 2;
		m.valInt = 123456;
		m.valShort = 321;
		m.valVector2_0.set(0.01f, 0.02f);
		m.valVector2_1.set(0.03f, 0.04f);
		m.setStringValue("012345678901234567890123");

		try
		{
			mConnections.connectToGameServer(Protocols.TCP, "192.168.1.105", 50001);

			Game.LOGGER.log("Connectado ao servidor!");
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		while (true)
		{
			//mConnections.sendToServer(m);
			mConnections.update();
			msgHandler.parseNextMessage();
			Game.mCurrentTick++;
			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// try
		// {
		// mConnections.acceptConnections(Protocols.TCP, 50002, (short) 1);
		// } catch (IOException e)
		// {
		// e.printStackTrace();
		// }

	}
}
