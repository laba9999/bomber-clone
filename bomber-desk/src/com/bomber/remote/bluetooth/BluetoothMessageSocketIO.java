package com.bomber.remote.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.bomber.Settings;
import com.bomber.remote.MessageSocketIO;

public class BluetoothMessageSocketIO extends MessageSocketIO {

	private final BluetoothSocket mSocket;
	private final InputStream mInStream;
	private final OutputStream mOutStream;

	public BluetoothMessageSocketIO(BluetoothSocket _socket) throws IOException {
		mSocket = _socket;

		mInStream = _socket.getInputStream();
		mOutStream = _socket.getOutputStream();
	}

	public BluetoothMessageSocketIO(String _macAddress) throws IOException {
		BluetoothDevice btDevice = Settings.BLUETOOTH_ADAPTER.getRemoteDevice(_macAddress);
		mSocket = btDevice.createRfcommSocketToServiceRecord(Settings.APP_UUID);
		mSocket.connect();
		mInStream = mSocket.getInputStream();
		mOutStream = mSocket.getOutputStream();
	}

	@Override
	protected void onClose()
	{
		try
		{
			mSocket.close();
		} catch (IOException e)
		{
		}
	}

	@Override
	protected boolean onSendMessage()
	{
		try
		{
			mOutStream.write(mSendBytes);
			return true;
		} catch (IOException e)
		{
			return false;
		}
	}

	@Override
	protected boolean onRecvMessage()
	{
		try
		{
			mInStream.read(mRecvBytes);
			return true;
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

}
