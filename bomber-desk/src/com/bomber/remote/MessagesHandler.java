package com.bomber.remote;

import com.bomber.Game;
import com.bomber.world.GameWorld;

public class MessagesHandler {
	GameWorld mWorld;
	MessageContainer mMessageContainer;
	RemoteConnections mRemoteConnections;
	public MessagesHandler(RemoteConnections _connections, GameWorld _world)
	{
		mWorld = _world;
		mMessageContainer = _connections.mRecvMessages;
		mRemoteConnections = _connections;
	}
	
	public void parseNextMessage()
	{
		if( !mMessageContainer.hasNext())
			return;
		
		
		// TODO : Adicionar os restantes tipos
		Message tmpMessage = mMessageContainer.getNext();
		
		if(tmpMessage.eventType == EventType.DISCONNECT)
		{
			Game.LOGGER.log(tmpMessage.getStringValue());
			return;
		}
		
		switch (tmpMessage.messageType)
		{
		case MessageType.BOMB:
			parseBombMessage(tmpMessage);
			break;

		case MessageType.PLAYER:
			parsePlayerMessage(tmpMessage);
			break;
		case MessageType.MONSTER:
			parseMonsterMessage(tmpMessage);
			break;
		case MessageType.CONNECTION:
			parseConnectionMessage(tmpMessage);
			break;
		default:
			break;
		}
		
		
		mMessageContainer.setParsed(tmpMessage);
	}

	private void parseConnectionMessage(Message _msg)
	{

		
		switch(_msg.eventType)
		{
		case EventType.SET_ID:
			mRemoteConnections.setLocalID(_msg.valShort);
			break;
		}
	}
	
	private void parsePlayerMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	private void parseBombMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	private void parseMonsterMessage(Message _msg)
	{
		System.out.println("Nova mensagem:");
		System.out.println("Tipo: BOMB");
		System.out.println(_msg.toString());
	}

	private void parseBonusMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	private void parsePointsMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	private void parseClockMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}
}
