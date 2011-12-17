package com.bomber.remote;

import com.bomber.world.GameWorld;

public class MessagesHandler {
	GameWorld mWorld;
	MessageContainer mMessageContainer;
	
	public MessagesHandler(MessageContainer _container, GameWorld _world)
	{
		mWorld = _world;
		mMessageContainer = _container;
	}
	
	public void parseNextMessage()
	{
		if( !mMessageContainer.hasNext())
			return;
		
		
		// TODO : Adicionar os restantes tipos
		Message tmpMessage = mMessageContainer.getNext();
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

		default:
			break;
		}
		
		
		mMessageContainer.setParsed(tmpMessage);
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
		throw new UnsupportedOperationException();
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
