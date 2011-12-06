package com.bomber.remote;

public class TCPMessageSocketIO extends MessageSocketIO {

	public synchronized void sendMessage(Message _message)
	{
		throw new UnsupportedOperationException();
	}

	public Message recvMessage()
	{
		throw new UnsupportedOperationException();
	}
}