package com.bomber.remote;

import java.io.IOException;

import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.monsters.Monster;
import com.bomber.gamestates.GameStateLoadingPVP;
import com.bomber.world.GameWorld;

public class MessagesHandler {
	public GameWorld mWorld;
	public Game mGame;

	MessageContainer mMessageContainer;
	RemoteConnections mRemoteConnections;

	public void setConnections(RemoteConnections _connections)
	{
		mMessageContainer = _connections.mRecvMessages;
		mRemoteConnections = _connections;
	}

	public void parseNextMessage()
	{
		if (!mMessageContainer.hasNext())
			return;

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
		case MessageType.CONNECTION:
			parseConnectionMessage(tmpMessage);
			break;
		case MessageType.GAME:
			parseGameMessage(tmpMessage);
			break;

		case MessageType.CLOCK:
			parseClockMessage(tmpMessage);
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
			RemoteConnections.mLocalID = _msg.valShort;
			mWorld.setLocalPlayer(_msg.valShort);
			break;

		case EventType.CONNECT_TO:
			try
			{
				mRemoteConnections.connectToPlayer(RemoteConnections.mProtocolInUse, _msg.getStringValue());
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			break;

		case EventType.DISCONNECTED:
			Game.LOGGER.log(_msg.getStringValue());

			if (Game.mHasStarted && !Game.mGameIsOver)
				mWorld.onPlayerDisconnect(_msg.valShort);
			else if (!Game.mHasStarted)
			{
				// Abre mais uma vaga
				mRemoteConnections.removePlayer(_msg.valShort);
			}
			break;

		case EventType.READY:
			RemoteConnections.mGameServer.mPlayersConfirmedCount++;
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

		case EventType.NAME:
			Game.LOGGER.log("Recebido nome!");
			for (Player player : mWorld.mPlayers)
			{
				if (player.mColor == _msg.valShort)
				{
					player.mName = _msg.getStringValue();
					Game.LOGGER.log("Atribuido nome!");
					break;
				}
			}
			// mGame.mWorld.getLocalPlayer().mName = DebugSettings.PLAYER_NAME;
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
			mWorld.spawnBomb((short) _msg.valInt, _msg.valShort, _msg.valVector2_0);
			break;

		default:
			if (Settings.DEBUG_MODE)
				throw new UnsupportedOperationException("Não está definido tratamento para a mensagem recebida: " + _msg.eventType);
			else
				Game.mRemoteConnections.closeAll("Não está definido tratamento para a mensagem recebida: " + _msg.eventType);
		}
	}

	private void parseMonsterMessage(Message _msg)
	{
		switch (_msg.eventType)
		{
		case EventType.KILL:
			for (Monster m : mWorld.mMonsters)
			{
				if (m.mUUID == _msg.UUID)
				{
					m.mPosition.set(_msg.valVector2_0);
					m.kill(_msg.valShort);
					break;
				}
			}
			break;

		default:
			if (Settings.DEBUG_MODE)
				throw new UnsupportedOperationException("Não está definido tratamento para a mensagem recebida: " + _msg.eventType);
			else
				Game.mRemoteConnections.closeAll("Não está definido tratamento para a mensagem recebida: " + _msg.eventType);
		}
	}

	private void parseGameMessage(Message _msg)
	{
		switch (_msg.eventType)
		{
		case EventType.INFO:
			mGame.changeInfo(_msg.valShort, (short) _msg.valInt, _msg.getStringValue());
			break;

		case EventType.COUNTDOWN:
			GameStateLoadingPVP.mCountdownSeconds = _msg.valInt;
			break;

		case EventType.START:
			GameStateLoadingPVP.mServerAuthorizedStart = true;
			Game.mHasStarted = true;

			mRemoteConnections.broadcastPlayerName();
			break;

		case EventType.RANDOM_SEED:
			mGame.updateRandomSeed(_msg.valInt);
			break;

		case EventType.JOINED_TEAM:
			for (Player p : mWorld.mPlayers)
			{
				if (p.mColor == _msg.valShort)
				{
					Game.mTeams[_msg.valInt].addElement(p);
					break;
				}
			}
			break;

		case EventType.LEFT_TEAM:
			for (Player p : mWorld.mPlayers)
			{
				if (p.mColor == _msg.valShort)
				{
					Game.mTeams[_msg.valInt].remove(p);
					break;
				}
			}
			break;

		default:
			if (Settings.DEBUG_MODE)
				throw new UnsupportedOperationException("Não está definido tratamento para a mensagem recebida: " + _msg.eventType);
			else
				Game.mRemoteConnections.closeAll("Não está definido tratamento para a mensagem recebida: " + _msg.eventType);
		}
	}

	private void parseClockMessage(Message _msg)
	{
		switch (_msg.eventType)
		{
		case EventType.SYNC:
			// Sincroniza o relógio
			mWorld.mClock.sync(_msg.valLong1);
			break;
		default:
			if (Settings.DEBUG_MODE)
				throw new UnsupportedOperationException("Não está definido tratamento para a mensagem recebida: " + _msg.eventType);
			else
				Game.mRemoteConnections.closeAll("Não está definido tratamento para a mensagem recebida: " + _msg.eventType);
		}
	}
}
