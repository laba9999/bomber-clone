package com.bomber.remote;

public class BluetoothMessageSocketIO extends MessageSocketIO {

	public synchronized void sendMessage(Message _message)
	{
		throw new UnsupportedOperationException();
	}

	public Message recvMessage()
	{
		throw new UnsupportedOperationException();
	}
}