package com.bomber;

import java.io.IOException;

import com.bomber.remote.Message;
import com.bomber.remote.MessageType;
import com.bomber.remote.Protocols;
import com.bomber.remote.RemoteConnections;
import com.bomber.remote.RemoteEventType;

public class GameServer extends Thread {

	RemoteConnections mConnections;

	public GameServer() {
		mConnections = new RemoteConnections(true);
		
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
			while(mConnections.mRecvMessages.hasNext())
			{
				Message tmpMessage = mConnections.mRecvMessages.getNext();
				
				if(tmpMessage.remoteEventType == RemoteEventType.DISCONNECT)
				{
					System.out.println(tmpMessage.getStringValue());
					continue;
				}
				switch(tmpMessage.messageType){
				case MessageType.BOMB:
					parseBombMessage(tmpMessage);
					break;
				}
				
				mConnections.mRecvMessages.setParsed(tmpMessage);
			}
			Game.mCurrentTick++;
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void parseBombMessage(Message _msg){
		System.out.println("Nova mensagem:");
		System.out.println("Tipo: BOMB");
		System.out.println(_msg.toString());
	}
}
