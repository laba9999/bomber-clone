package com.bomber.remote;

import java.util.HashMap;

public class RemoteConnections {
	private Connection mGameServer;
	
	/**
	 * a key é o mLocalID da Connection
	 */
	private HashMap<Short, Connection> mPlayers;
	public MessageContainer mRecvMessages;

	/**
	 * Chama a outra função broadcast com o parametro _include server a true.
	 */
	public void broadcast(Message _msg, boolean _includeServer)
	{
		throw new UnsupportedOperationException();
	}

	public void sendToServer(Message _msg)
	{
		throw new UnsupportedOperationException();
	}
}