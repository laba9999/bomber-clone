package com.bomber.remote;

import java.util.LinkedList;
import java.util.List;

import com.bomber.Game;

public abstract class LocalServer extends Thread {

	// Onde as novas conexões vão ser guardadas até a thread principal as vir
	// buscar. Este método é preferivel porque assim o método sincronizado está
	// nesta classe, que deixará de ser usado quando se para de receber novas
	// ligações. A alternativa seria criar métodos sincronizados para adicionar
	// e listar as ligações existentes no RemoteConnections.mConnections. O
	// método para listar seria usado ao longo de toda a execução que por ser do
	// tipo synchronized teria um maior impacto na performance.
	private LinkedList<Connection> mConnectionsCache;
	private boolean mKeepReceiving = true;
	private MessageContainer mMessageContainer;

	private short mMax = 0;
	
	public boolean mAllConnected = false;
	
	public LocalServer(MessageContainer _msgContainer, short _max) {
		mConnectionsCache = new LinkedList<Connection>();
		mMessageContainer = _msgContainer;
		
		mMax = _max;
	}

	/**
	 * Método a ser chamado quando não se pretender que sejam recebidas novas
	 * ligações.
	 */
	public void stopReceiving()
	{
		mKeepReceiving = false;
		onStopReceiving();
	}

	@Override
	public void run()
	{
		short nAdded = 0;
		while (mKeepReceiving && nAdded++ < mMax)
		{
			waitForConnection();
		}
		
		mAllConnected = true;
		mKeepReceiving = false;
		
		Game.LOGGER.log("Server desligado...");
	}

	/**
	 * Insere as novas ligações recebidas e guardadas em cache no Map passado
	 * como parametro
	 * 
	 * @param _container
	 *            O atributo {@link RemoteConnections.mPlayers}
	 */
	public synchronized void getCachedConnections(List<Connection> _container)
	{
		Connection tmpConnection;

		while (!mConnectionsCache.isEmpty())
		{
			tmpConnection = mConnectionsCache.remove();
			_container.add(tmpConnection.mLocalID, tmpConnection);
		}
	}

	/**
	 * Chamado pelas classes derivadas para adicionar uma nova ligação recebida
	 * à cache de ligações à espera de serem obtidas pela thread principal.
	 * 
	 * @param _newSocket
	 *            A nova conexão a ser adicionada à cache.
	 */
	protected synchronized void cacheConnection(MessageSocketIO _newSocket)
	{
		Connection tmpConn = new Connection(_newSocket, mMessageContainer);
		tmpConn.setDaemon(true);
		tmpConn.start();
		mConnectionsCache.add(tmpConn);
	}

	/**
	 * O método a implementar nas classes derivadas. Este método deve aceitar
	 * uma nova ligação, adicioná-la à cache e sair. Será invocado novamente
	 * para aceitar nova conexão.
	 */
	public abstract void waitForConnection();
	
	public abstract void onStopReceiving();
}
