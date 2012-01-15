package com.bomber.remote.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.bomber.Settings;
import com.bomber.common.Utils;
import com.bomber.remote.LocalServer;
import com.bomber.remote.MessageContainer;

public class BluetoothLocalServer extends LocalServer {

	private BluetoothServerSocket mServerSocket = null;

	public BluetoothLocalServer(MessageContainer _msgContainer) throws IOException {
		super(_msgContainer, (short) 1);

		BluetoothAdapter mBluetoothAdapter = Settings.BLUETOOTH_ADAPTER;
		mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BOMBER", Settings.APP_UUID);
	}

	@Override
	public void waitForConnection()
	{
		try
		{
			Utils.LOG("À espera de um cliente bluetooth...");
			BluetoothSocket socket = mServerSocket.accept();
			cacheConnection(new BluetoothMessageSocketIO(socket));
			Utils.LOG("Cliente bluetooth ligado...");
		} catch (IOException e)
		{

		} finally
		{
			try
			{
				mServerSocket.close();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void onStopReceiving()
	{
		try
		{
			if (mServerSocket != null)
				mServerSocket.close();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

	}

	@Override
	public int getLocalPort()
	{
		return 0;
	}

}