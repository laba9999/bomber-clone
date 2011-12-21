package com.bomber.remote.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.bomber.remote.MessageSocketIO;

public class TCPMessageSocketIO extends MessageSocketIO {
	private Socket mSocket;

	private BufferedOutputStream mOutput;
	private BufferedInputStream mInput;

	public TCPMessageSocketIO(String _addressToConnect, int _portToConnect) throws IOException {

		mSocket = new Socket(_addressToConnect, _portToConnect);

		mSocket.setTcpNoDelay(true);
		initializeIOStreams();
	}

	public TCPMessageSocketIO(Socket _socket) throws IOException {
		mSocket = _socket;
		mSocket.setTcpNoDelay(true);
		initializeIOStreams();
	}

	@Override
	public String toString()
	{
		// return mSocket.getInetAddress().toString() + ":" + mSocket.getPort();
		return mSocket.getInetAddress().getHostAddress().toString() + ":" + mSocket.getPort();
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
	public boolean onRecvMessage()
	{
		try
		{
			if (mInput.read(mRecvBytes) <= 0)
				throw new IOException("Falha ao ler do socket!");
		} catch (Throwable e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public void onClose()
	{
		try
		{
			mOutput.close();
			mInput.close();

			mSocket.close();
		} catch (Throwable e)
		{
			e.printStackTrace();
		}

	}
}