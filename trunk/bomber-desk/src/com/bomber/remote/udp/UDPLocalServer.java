package com.bomber.remote.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.bomber.remote.LocalServer;
import com.bomber.remote.Message;
import com.bomber.remote.MessageContainer;


public class UDPLocalServer extends LocalServer {
	DatagramSocket mSocket = null;
	DatagramPacket mRecvPacket = null;
	private byte[] mReceiveBuffer = new byte[Message.MESSAGE_SIZE + 2];
	
	public UDPLocalServer(MessageContainer _msgContainer, int _port, short _max) throws SocketException {
		super(_msgContainer, _max);
		
		mSocket = new DatagramSocket(_port);
		mRecvPacket = new DatagramPacket(mReceiveBuffer, mReceiveBuffer.length);
	}

	public int getLocalPort()
	{
		return mSocket.getLocalPort();
	}
	
	@Override
	public void waitForConnection()
	{
		try
		{
			mSocket.receive(mRecvPacket);
			cacheConnection(new UDPMessageSocketIO(mRecvPacket));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void onStopReceiving()
	{
		if (mSocket != null)
			mSocket.close();
	}

}
