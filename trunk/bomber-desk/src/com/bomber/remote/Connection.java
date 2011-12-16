package com.bomber.remote;

public class Connection extends Thread {

	/**
	 * ID que identifica este cliente perante todos os outros é atribuido pelo
	 * servidor.
	 */
	private short mLocalID;

	// Latência
	private short mRTT;
	public MessageSocketIO mSocket;

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		super.run();
	}
}