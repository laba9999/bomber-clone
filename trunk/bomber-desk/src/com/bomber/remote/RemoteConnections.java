package com.bomber.remote;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;

import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.gametypes.GameType;
import com.bomber.remote.tcp.TCPLocalServer;
import com.bomber.remote.tcp.TCPMessageSocketIO;

public class RemoteConnections {
	private Connection mGameServer = null;

	private ArrayList<Connection> mPlayers;
	private LocalServer mLocalServer = null;

	private boolean mAcceptingConnections = false;
	public MessageContainer mRecvMessages = null;

	public boolean mIsGameServer = false;

	public Message mMessageToSend = new Message();

	public RemoteConnections(boolean _isGameServer) {
		mRecvMessages = new MessageContainer();
		mPlayers = new ArrayList<Connection>();

		mIsGameServer = _isGameServer;
	}

	public void setLocalID(short _id)
	{
		if (mGameServer != null)
			mGameServer.setLocalId(_id);

		for (int i = 0; i < mPlayers.size(); i++)
			mPlayers.get(i).setLocalId(_id);
	}

	public boolean isConnectedToServer()
	{
		return mGameServer != null || mIsGameServer;
	}

	public void connectToGameServer(short _protocol, String _ip, int _port) throws IOException
	{

		switch (_protocol)
		{
		case Protocols.TCP:
			mGameServer = new Connection(new TCPMessageSocketIO(_ip, _port), mRecvMessages);

			mMessageToSend.messageType = MessageType.CONNECTION;
			mMessageToSend.eventType = EventType.LOCAL_SERVER_PORT;
			mMessageToSend.valInt = ((TCPLocalServer) mLocalServer).getLocalPort();
			mGameServer.sendMessage(mMessageToSend);
			break;
		}

		if (mGameServer != null)
		{
			mGameServer.setDaemon(true);
			mGameServer.start();
		}
	}

	public void connectToPlayer(short _protocol, String _ip, int _port) throws IOException
	{
		Connection tmpConn = null;
		switch (_protocol)
		{
		case Protocols.TCP:
			tmpConn = new Connection(new TCPMessageSocketIO(_ip, _port), mRecvMessages);
			break;
		}

		if (tmpConn != null)
		{
			tmpConn.setDaemon(true);
			tmpConn.start();
			mPlayers.add(tmpConn);
		}

	}

	public void update()
	{
		mRecvMessages.update();
		
		if (mAcceptingConnections)
		{
			mLocalServer.getCachedConnections(mPlayers);
			mAcceptingConnections = !mLocalServer.mAllConnected;

			if (!mAcceptingConnections)
			{

				// TODO : comentar os sysout's
				Game.LOGGER.log("Clientes ligados:");
				for (int i = 0; i < mPlayers.size(); i++)
					Game.LOGGER.log(mPlayers.get(i).toString());

				if (mIsGameServer)
				{
					Message tmpMessage = new Message();
					tmpMessage.messageType = MessageType.CONNECTION;
					tmpMessage.eventType = EventType.SET_ID;

					// Actualiza os id's
					for (short i = 0; i < mPlayers.size(); i++)
					{
						tmpMessage.valShort = (short) (i + 1);
						mPlayers.get(i).sendMessage(tmpMessage);
					}

					// Envia o endereço de todos a todos para que se liguem
					// entre si
					tmpMessage.eventType = EventType.CONNECT_TO;
					for (short i = 0; i < mPlayers.size(); i++)
					{
						String address = mPlayers.get(i).getSocketAddressString();
						String [] components  = address.split(":");
						address = components[0] + ":" + mPlayers.get(i).mRemoteServerPort;
						for (short c = (short) (i + 1); c < mPlayers.size(); c++)
						{
							tmpMessage.setStringValue(address);
							mPlayers.get(c).sendMessage(tmpMessage);
						}
					}
					
					// Inicia o jogo
					tmpMessage.eventType = EventType.START;
					for (short i = 0; i < mPlayers.size(); i++)
						mPlayers.get(i).sendMessage(tmpMessage);
					
					mRecvMessages.add(tmpMessage);
				}

			}
		}

		// Actualiza as ligações já existentes
		for (int i = 0; i < mPlayers.size(); i++)
			mPlayers.get(i).update();

		if (mGameServer != null && mGameServer.mIsConnected)
			mGameServer.update();
	}

	/**
	 * Cria um server local que vai aceitar conexões remotas.
	 * 
	 * @param _protocol
	 *            O protocolo a usar um dos {@link Protocols}.
	 * @param _port
	 *            O porto onde ficar à escuta. Null se não se aplicar.
	 * @param _maxConnections
	 *            O número máximo de conexões a aceitar.
	 * @throws IOException
	 */
	public void acceptConnections(short _protocol, int _port, short _maxConnections) throws IOException
	{
		switch (_protocol)
		{
		case Protocols.TCP:
			mLocalServer = new TCPLocalServer(mRecvMessages, _port, _maxConnections);
		}

		if (null != mLocalServer)
		{
			mAcceptingConnections = true;
			mLocalServer.setDaemon(true);
			mLocalServer.start();
		}
	}

	public void broadcast(Message _msg)
	{
		Connection tmpConn;
		for (int i = 0; i < mPlayers.size(); i++)
		{
			tmpConn = mPlayers.get(i);

			if (!tmpConn.mIsConnected)
			{
				// Remove a conexão da lista de conexões activas
				mPlayers.remove(i);
				i--;
				continue;
			}

			tmpConn.sendMessage(_msg);
		}

		if (mGameServer != null && mGameServer.mIsConnected)
			mGameServer.sendMessage(_msg);
	}

	public static RemoteConnections create(short _gameType, boolean _isServer, String _serverAddress, int _serverPort)
	{
		RemoteConnections connections = new RemoteConnections(_isServer);

		short nPlayers;
		switch (_gameType)
		{
		case GameType.CTF:
		case GameType.DEADMATCH:
			nPlayers = 2;
			break;
		case GameType.TEAM_CTF:
		case GameType.TEAM_DEADMATCH:
			nPlayers = 4;
			break;
		default:
			throw new InvalidParameterException("Tipo de jogo PvP desconhecido!");
		}

		nPlayers = DebugSettings.NUMBER_OF_PLAYERS;
		if (!_isServer)
		{
			Random r = new Random(System.nanoTime());
			try
			{
				System.out.println("À espera de ligações...");
				connections.acceptConnections(Protocols.TCP, r.nextInt(100) + 50006, (short) (nPlayers - 1));
			} catch (IOException e)
			{
				e.printStackTrace();
				System.exit(-1);
			}

			// Liga ao servidor
			try
			{
				connections.connectToGameServer(Protocols.TCP, _serverAddress, _serverPort);
				// Game.LOGGER.log("Connectado ao servidor!");
			} catch (IOException e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		} else
		{
			try
			{
				System.out.println("À espera de ligações...");
				connections.acceptConnections(Protocols.TCP, _serverPort, (short) (nPlayers - 1));
				if (_isServer)
					System.out.println("Server online!");
			} catch (IOException e)
			{
				e.printStackTrace();
				System.exit(-1);
			}
		}

		return connections;
	}

	public void closeAll(String _reason)
	{

		try
		{
			// Fecha tudo
			for (int i = 0; i < mPlayers.size(); i++)
			{
				mPlayers.get(i).disconnect(_reason);
				mPlayers.get(i).join();
			}

			if (mGameServer != null)
			{
				mGameServer.disconnect(_reason);
				mGameServer.join();
			}

			if (mLocalServer != null)
			{
				mLocalServer.stopReceiving();
				mLocalServer.join();
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}