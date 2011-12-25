package com.bomber.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.common.assets.SoundAssets;
import com.bomber.gamestates.GameStateLoadingPVP;
import com.bomber.gamestates.GameStateServerConnectionError;

public class GameServer {
	private Connection mConnection = null;
	private List<Connection> mPlayers;

	private List<PlayerTeam> mTeamPlayerAssociations;

	public Stack<Short> mAvailableIds;
	public Stack<Short> mAvailableTeams;

	// Esta variável é muito importante!!!
	public boolean mReadyToStart = false;
	private boolean mInterconnectSequenceSent = false;
	private boolean mIsOnline = true;
	public boolean mStartedCountdown = false;

	public short mPlayersConfirmedCount = 0;

	private short mCountdownSeconds = DebugSettings.GAME_COUNTDOWN_SECONDS;
	private short mTicksSinceLastCountdownMessage = 0;
	private Message tmpMessage = new Message();

	public GameServer(Connection _server, List<Connection> _players) {
		mConnection = _server;
		mPlayers = _players;

		if (!RemoteConnections.mIsGameServer)
			return;

		mTeamPlayerAssociations = new ArrayList<PlayerTeam>();

		// Prepara os ID's disponiveis que correspondem às cores. Esta ordem de
		// introdução não é por acaso!
		mAvailableIds = new Stack<Short>();

		mAvailableIds.push((short) 3);
		mAvailableIds.push((short) 2);
		mAvailableIds.push((short) 1);

		// Prepara as equipas disponiveis
		mAvailableTeams = new Stack<Short>();

		for (short i = 0; i < Game.mNumberPlayers / 2; i++)
			mAvailableTeams.push((short) 1);

		if (Game.mNumberPlayers / 2 > 1)
			mAvailableTeams.push((short) 0);
	}

	public void releaseId(short _id)
	{
		tmpMessage.messageType = MessageType.GAME;
		tmpMessage.eventType = EventType.LEFT_TEAM;

		// Remove alguma possivel associação player/team que possa existir
		for (short i = 0; i < mTeamPlayerAssociations.size(); i++)
		{
			if (mTeamPlayerAssociations.get(i).mColor == _id)
			{
				// Avisa os players que esta associação deixou de existir
				tmpMessage.valInt = mTeamPlayerAssociations.get(i).mTeamId;
				tmpMessage.valShort = mTeamPlayerAssociations.get(i).mColor;
				for (short c = 0; c < mPlayers.size(); c++)
					if (mPlayers.get(c).mRemoteID != _id)
						mPlayers.get(c).sendMessage(tmpMessage);

				// Remove localmente
				Game.mRemoteConnections.mRecvMessages.add(tmpMessage);

				// Apaga a associação
				mAvailableTeams.push((short) tmpMessage.valInt);
				mTeamPlayerAssociations.remove(i);
				break;
			}
		}

		// Liberta o id
		mAvailableIds.push(_id);
	}

	public void resetCountdown(short _seconds)
	{
		mTicksSinceLastCountdownMessage = 0;
		mStartedCountdown = true;
		mCountdownSeconds = _seconds;
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
			if (!Game.mHasStarted && mReadyToStart && (mPlayersConfirmedCount >= Game.mNumberPlayers - 1) && !mInterconnectSequenceSent)
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
			// if (Game.mHasStarted && mPlayers.size() == 0 &&
			// !Game.mGameIsOver)
			// RemoteConnections.mGame.setGameState(new
			// GameStateServerConnectionError(RemoteConnections.mGame,
			// "Sem clientes..."));
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

			GameStateLoadingPVP.mCountdownSeconds = tmpMessage.valInt;
		}
	}

	public void giveIds(short _starIdx)
	{
		List<PlayerTeam> newAssociations = new ArrayList<PlayerTeam>();

		for (short i = _starIdx; i < mPlayers.size(); i++)
		{
			tmpMessage.messageType = MessageType.GAME;
			tmpMessage.eventType = EventType.INFO;

			tmpMessage.valShort = DebugSettings.GAME_TYPE;
			tmpMessage.valInt = DebugSettings.GAME_ROUNDS;
			tmpMessage.setStringValue(DebugSettings.LEVEL_TO_LOAD);

			mPlayers.get(_starIdx).sendMessage(tmpMessage);

			// Atribui os id's finais a cada um dos players
			tmpMessage.messageType = MessageType.CONNECTION;
			tmpMessage.eventType = EventType.SET_ID;

			tmpMessage.valShort = mAvailableIds.pop();
			mPlayers.get(_starIdx).sendMessage(tmpMessage);

			// Cria uma nova associação player/ team
			newAssociations.add(new PlayerTeam(tmpMessage.valShort, mAvailableTeams.pop()));

			// Avisa este player das associações player/equipa já existentes
			tmpMessage.messageType = MessageType.GAME;
			tmpMessage.eventType = EventType.JOINED_TEAM;

			for (short c = 0; c < mTeamPlayerAssociations.size(); c++)
			{
				tmpMessage.valInt = mTeamPlayerAssociations.get(c).mTeamId;
				tmpMessage.valShort = mTeamPlayerAssociations.get(c).mColor;
				mPlayers.get(i).sendMessage(tmpMessage);
			}

			// Envia o nome do jogador que está a fazer de server
			tmpMessage.messageType = MessageType.PLAYER;
			tmpMessage.eventType = EventType.NAME;
			tmpMessage.setStringValue(DebugSettings.PLAYER_NAME);
			tmpMessage.valShort = 0;
			mPlayers.get(i).sendMessage(tmpMessage);
		}

		// Avisa todos os players das novas associações player/equipa
		tmpMessage.messageType = MessageType.GAME;
		tmpMessage.eventType = EventType.JOINED_TEAM;

		for (short c = 0; c < newAssociations.size(); c++)
		{
			mTeamPlayerAssociations.add(newAssociations.get(c));

			tmpMessage.valInt = newAssociations.get(c).mTeamId;
			tmpMessage.valShort = newAssociations.get(c).mColor;

			for (short i = 0; i < mPlayers.size(); i++)
				mPlayers.get(i).sendMessage(tmpMessage);

			// Adiciona localmente
			Game.mRemoteConnections.mRecvMessages.add(tmpMessage);
		}

		// Envia a seed a usar nos randoms
		tmpMessage.messageType = MessageType.GAME;
		tmpMessage.eventType = EventType.RANDOM_SEED;
		tmpMessage.valInt = Game.mRandomSeed;
		for (; _starIdx < mPlayers.size(); _starIdx++)
			mPlayers.get(_starIdx).sendMessage(tmpMessage);
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

		GameStateLoadingPVP.mServerAuthorizedStart = true;
		Game.mHasStarted = true;

		//SoundAssets.playMusic(Game.mLevelToLoad, true, 1.0f);
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

	/**
	 * Guarda os dados relativos a que jogadores estao em cada equipa.
	 * 
	 * @author sPeC!
	 * 
	 */
	public class PlayerTeam {
		public short mTeamId;
		public short mColor;

		public PlayerTeam(short _color, short _teamId) {
			this.mColor = _color;
			this.mTeamId = _teamId;
		}

	}
}
