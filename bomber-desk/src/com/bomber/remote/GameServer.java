package com.bomber.remote;

import java.util.List;

import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.gamestates.GameStateLoading;
import com.bomber.gamestates.GameStateServerConnectionError;

public class GameServer {
	private Connection mConnection = null;
	private List<Connection> mPlayers;

	private static short mLastGivenId = 0;

	// Esta variável é muito importante!!!
	public boolean mReadyToStart = false;
	private boolean mInterconnectSequenceSent = false;
	private boolean mIsOnline = true;
	private boolean mStartedCountdown = false;

	private short mCountdownSeconds = DebugSettings.GAME_COUNTDOWN_SECONDS;
	private short mTicksSinceLastCountdownMessage = 0;
	private Message tmpMessage = new Message();

	public GameServer(Connection _server, List<Connection> _players) {
		mConnection = _server;
		mPlayers = _players;
		mLastGivenId = 0;
	}

	public boolean isConnected()
	{
		return mConnection != null && mConnection.mIsConnected;
	}

	public void update()
	{

		if (mConnection != null)
			mConnection.update();
		else
		{

			//
			// Somos o servidor
			if (!mIsOnline)
				return;

			if (mStartedCountdown)
				continueCountdown();

			// Envia a sequencia de interconexão dos clientes
			if (!Game.mHasStarted && mReadyToStart && !mInterconnectSequenceSent)
				mStartedCountdown = mInterconnectSequenceSent = startInterconnectSequence();

			// Não é permitido iniciar o jogo se um dos clientes desligar
			// durante o countdown
			if (!Game.mHasStarted && mStartedCountdown && mPlayers.size() != Game.mNumberPlayers - 1)
			{
				RemoteConnections.mGame.setGameState(new GameStateServerConnectionError(RemoteConnections.mGame, "Cliente perdido..."));
				for (short i = 0; i < mPlayers.size(); i++)
					mPlayers.get(i).disconnect("O countdown falhou!");

				mIsOnline = false;
				return;
			}

			// Verifica se ainda há jogadores ligados
			if (Game.mHasStarted && mPlayers.size() == 0)
				RemoteConnections.mGame.setGameState(new GameStateServerConnectionError(RemoteConnections.mGame, "Sem clientes..."));
		}

	}

	private void continueCountdown()
	{
		if (mTicksSinceLastCountdownMessage++ >= Game.TICKS_PER_SECOND)
		{
			mTicksSinceLastCountdownMessage = 0;

			if (mCountdownSeconds == 0)
			{
				mStartedCountdown = false;
				startGame();
				return;
			}

			tmpMessage.messageType = MessageType.GAME;
			tmpMessage.eventType = EventType.COUNTDOWN;
			tmpMessage.valInt = mCountdownSeconds--;

			for (int i = 0; i < mPlayers.size(); i++)
				mPlayers.get(i).sendMessage(tmpMessage);

			GameStateLoading.mCountdownSeconds = tmpMessage.valInt;
		}
	}

	public void giveIds(short _starIdx)
	{

		tmpMessage.messageType = MessageType.CONNECTION;
		tmpMessage.eventType = EventType.SET_ID;

		// Atribui os id's finais a cada um dos players
		for (; _starIdx < mPlayers.size(); _starIdx++)
		{
			tmpMessage.valShort = ++mLastGivenId;
			mPlayers.get(_starIdx).sendMessage(tmpMessage);
		}
	}

	public boolean startInterconnectSequence()
	{
		// Verifica se já todos enviaram o porto remoto
		for (short i = 0; i < mPlayers.size(); i++)
			if (mPlayers.get(i).mRemoteServerPort == -1)
				return false;

		tmpMessage.messageType = MessageType.CONNECTION;

		// Envia o endereço de todos a todos para que se liguem
		// entre si
		tmpMessage.eventType = EventType.CONNECT_TO;
		for (short i = 0; i < mPlayers.size(); i++)
		{
			// Junta o endereço ip da ligação à porta onde disseram que
			// estavam à escuta.
			String address = mPlayers.get(i).getSocketAddressString();
			String[] components = address.split(":");
			address = components[0] + ":" + mPlayers.get(i).mRemoteServerPort;
			for (short c = (short) (i + 1); c < mPlayers.size(); c++)
			{
				tmpMessage.setStringValue(address);
				mPlayers.get(c).sendMessage(tmpMessage);
			}
		}

		// Avisa todos os players que devem parar de ficar à espera de
		// novas ligações.
		tmpMessage.eventType = EventType.STOP_LOCAL_SERVER;
		for (short i = 0; i < mPlayers.size(); i++)
			mPlayers.get(i).sendMessage(tmpMessage);

		return true;
	}

	public void startGame()
	{

		// Inicia o jogo
		tmpMessage.messageType = MessageType.GAME;
		tmpMessage.eventType = EventType.START;
		for (short i = 0; i < mPlayers.size(); i++)
			mPlayers.get(i).sendMessage(tmpMessage);

		GameStateLoading.mServerAuthorizedStart = true;
		Game.mHasStarted = true;
	}

	public void setLocalId(short _id)
	{
		if (mConnection != null)
			mConnection.setLocalId(_id);
	}

	public void sendMessage(Message _msg)
	{
		if (mConnection != null)
			mConnection.sendMessage(_msg);
	}

	public void disconnect(String _reason)
	{
		if (mConnection != null)
			mConnection.disconnect(_reason);
	}

	public void join() throws InterruptedException
	{
		if (mConnection != null)
			mConnection.join();
	}
}
