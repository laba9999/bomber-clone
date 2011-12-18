package com.bomber.remote;

import java.io.IOException;

import com.bomber.Game;
import com.bomber.gameobjects.Player;
import com.bomber.gamestates.GameStateLoading;
import com.bomber.world.GameWorld;

public class MessagesHandler {
	GameWorld mWorld;
	public MessageContainer mMessageContainer;
	RemoteConnections mRemoteConnections;

	public MessagesHandler(RemoteConnections _connections, GameWorld _world) {
		mWorld = _world;
		mMessageContainer = _connections.mRecvMessages;
		mRemoteConnections = _connections;
	}

	public void parseNextMessage()
	{
		if (!mMessageContainer.hasNext())
			return;

		// TODO : Adicionar os restantes tipos
		Message tmpMessage = mMessageContainer.getNext();

		if (tmpMessage.eventType == EventType.DISCONNECT)
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

		switch (_msg.eventType)
		{
		case EventType.SET_ID:
			mRemoteConnections.setLocalID(_msg.valShort);
			mWorld.setLocalPlayer(_msg.valShort);
			break;

		case EventType.CONNECT_TO:
			
			Game.LOGGER.log("A tentar ligar ao cliente: " + _msg.getStringValue());
			String[] data = _msg.getStringValue().split(":");
			try
			{
				mRemoteConnections.connectToPlayer(Protocols.TCP, data[0], Integer.valueOf(data[1]));
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			break;

		case EventType.START:
			GameStateLoading.mServerAuthorizedStart = true;
			break;
		}
	}

	private void parsePlayerMessage(Message _msg)
	{
		switch (_msg.eventType)
		{
		case EventType.MOVE:
			for (Player player : mWorld.mPlayers)
			{
				if (player.mUUID == _msg.UUID)
				{
					player.changeDirection(_msg.valShort);
					player.mPosition.set(_msg.valVector2_0);
					break;
				}
			}
			break;
		case EventType.STOP:
			for (Player player : mWorld.mPlayers)
			{
				if (player.mUUID == _msg.UUID)
				{
					player.changeDirection(_msg.valShort);
					player.mPosition.set(_msg.valVector2_0);
					player.stop();
					break;
				}

			}
			break;
		case EventType.SYNC:
			for (Player player : mWorld.mPlayers)
			{
				if (player.mUUID == _msg.UUID)
				{
					player.mDirection = _msg.valShort;
					player.mPosition.set(_msg.valVector2_0);

					if (_msg.valInt == 0)
						player.mIsMoving = false;
					else
						player.mIsMoving = true;
					break;
				}
			}
			break;
		default:
			throw new UnsupportedOperationException("Não está definido tratamento para a mensagem recebida.");
		}

	}

	private void parseBombMessage(Message _msg)
	{
		switch (_msg.eventType)
		{
		case EventType.CREATE:
			mWorld.spawnBomb(_msg.valShort, _msg.valVector2_0);

			break;
		default:
			throw new UnsupportedOperationException("Não está definido tratamento para a mensagem recebida.");
		}
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
