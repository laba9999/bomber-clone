package com.amov.bomber;

import java.io.IOException;

import android.os.Bundle;
import android.view.WindowManager.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bomber.Game;
import com.bomber.GameClient;
import com.bomber.remote.EventType;
import com.bomber.remote.Message;
import com.bomber.remote.MessageType;
import com.bomber.remote.MessagesHandler;
import com.bomber.remote.Protocols;
import com.bomber.remote.RemoteConnections;

public class AndroidGame extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub

		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		initialize(new Game(createConnections()), false);
	}

	private static RemoteConnections createConnections()
	{
		RemoteConnections connections = new RemoteConnections(true);

		try
		{
			System.out.println("À espera de ligações...");
			connections.acceptConnections(Protocols.TCP, 50005, (short) 1);
			System.out.println("Server online!");
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
		return connections;
	}
	
}