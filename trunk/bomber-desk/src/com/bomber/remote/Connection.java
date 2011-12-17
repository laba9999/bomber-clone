package com.bomber.remote;

public class Connection extends Thread {

	/**
	 * ID que identifica este cliente perante todos os outros é atribuido pelo
	 * servidor.
	 */
	public short mLocalID;

	// Latência
	private short mRTT;
	private MessageSocketIO mSocket;

	
	public Connection(MessageSocketIO _socket)
	{
		mSocket = _socket;
	}
	
	public void broadcastMessage(Message _msg)
	{
		
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		super.run();
	}
}