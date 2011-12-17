package com.bomber.remote.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.bomber.remote.MessageContainer;
import com.bomber.remote.MessageSocketIO;

public class TCPMessageSocketIO extends MessageSocketIO {
	private Socket mSocket;

	private BufferedOutputStream mOutput;
	private BufferedInputStream mInput;

	public TCPMessageSocketIO(String _addressToConnect, int _portToConnect, MessageContainer _msgContainer) throws IOException {
		super(_msgContainer);

		mSocket = new Socket(_addressToConnect, _portToConnect);
		initializeIOStreams();
	}

	public TCPMessageSocketIO(Socket _socket, MessageContainer _msgContainer) throws IOException {
		super(_msgContainer);

		mSocket = _socket;
		initializeIOStreams();
	}

	private void initializeIOStreams() throws IOException
	{
		mOutput = new BufferedOutputStream(mSocket.getOutputStream());
		mInput = new BufferedInputStream(mSocket.getInputStream());
	}

	@Override
	public boolean onSendMessage()
	{
		try
		{
			mOutput.write(mSendBytes);
			mOutput.flush();
		} catch (IOException e)
		{
			e.printStackTrace();

			return false;
		}

		return true;
	}

	@Override
	public boolean recvMessage()
	{
		try
		{
			mInput.read(mRecvBytes);
			onNewMessageReceived();
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
}