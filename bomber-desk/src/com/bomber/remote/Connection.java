package com.bomber.remote;

import com.bomber.Game;

public class Connection extends Thread {

	private static final short RTT_CHECK_INTERVAL = Game.TICKS_PER_SECOND * 2;
	private static final int TIMEOUT_VALUE = (Game.TICKS_PER_SECOND / 2);
	private static final short MAX_TIMEOUTS = 3;

	/**
	 * ID que identifica este cliente perante todos os outros é atribuido pelo
	 * servidor.
	 */
	public short mRemoteID = -1;

	// Latência em ticks
	public short mRTT = 0;
	private long mLastRTTCheckTick = 0;
	private boolean mSentPing = false;

	private MessageSocketIO mSocket;
	private MessageContainer mMessagesContainer;

	// Para os pings/pongs e desconexões.
	private Message mMessageForInternalUse;

	private short mTimeoutsCounter = 0;
	public boolean mIsConnected = true;

	public int mRemoteServerPort = -1;

	public Connection(MessageSocketIO _socket, MessageContainer _msgContainer) {
		mSocket = _socket;
		mMessagesContainer = _msgContainer;

		mMessageForInternalUse = new Message();
		mMessageForInternalUse.messageType = MessageType.CONNECTION;
	}

	@Override
	public String toString()
	{
		return "Remote id: " + mRemoteID + " - " + mSocket.toString();
	}

	public String getSocketAddressString()
	{
		return mSocket.toString();
	}

	public void sendMessage(Message _msg)
	{
		// Se estamos disconectados nem vale a pena tentar
		if (!mIsConnected)
			return;

		// Se a mensagem for enviada com sucesso sai
		if (mSocket.sendMessage(_msg))
			return;

		// Houve um erro ao enviar a mensagem
		disconnect("Erro enviar mensagem!");
	}

	public synchronized void disconnect(String _reason)
	{
		if (!mIsConnected)
			return;

		// Avisa o World através de uma mensagem
		// Para verificar se foi o server basta comparar o valShort com o
		// RemoteId do atributo mGameServer
		mMessageForInternalUse.eventType = EventType.DISCONNECTED;
		mMessageForInternalUse.valShort = mRemoteID;
		mMessageForInternalUse.senderID = RemoteConnections.mLocalID;
		mMessageForInternalUse.setStringValue(_reason);
		mMessagesContainer.add(mMessageForInternalUse);

		mIsConnected = false;
		mSocket.close();

		Game.LOGGER.log("Conexion " + mRemoteID + " has disconnected...");

	}

	public void update()
	{
		if (!mIsConnected)
			return;

		if (mSentPing)
		{
			if ((Game.mCurrentTick - mLastRTTCheckTick) > TIMEOUT_VALUE)
			{
				if (mTimeoutsCounter++ < MAX_TIMEOUTS)
				{
					Game.LOGGER.log("Tick: " + Game.mCurrentTick + " - Timeout " + mTimeoutsCounter + " de " + MAX_TIMEOUTS);
					mLastRTTCheckTick = Game.mCurrentTick;
					return;
				}

				Game.LOGGER.log("Tick: " + Game.mCurrentTick + " - Conexion " + mRemoteID + " timed out...");
				disconnect("Timeout!");
			}
		} else
		{
			// Verifica a latência
			if (Game.mCurrentTick > (mLastRTTCheckTick + RTT_CHECK_INTERVAL))
			{
				mLastRTTCheckTick = Game.mCurrentTick;
				mMessageForInternalUse.eventType = EventType.PING;
				mSentPing = true;
				sendMessage(mMessageForInternalUse);
				
				Game.LOGGER.log("Tick: " + Game.mCurrentTick + " - PING enviado!");
			}
		}
	}

	@Override
	public void run()
	{
		Message rcvedMsg;
		while (true)
		{
			rcvedMsg = mSocket.recvMessage();
			if (rcvedMsg == null || mSocket.mIsClosed)
				break;

			mTimeoutsCounter = 0;
			mRemoteID = rcvedMsg.senderID;
			addMessageToContainer(rcvedMsg);
		}

		disconnect("Erro receber mensagem!");
	}

	private void addMessageToContainer(Message _msg)
	{

		switch (_msg.eventType)
		{
		case EventType.PING:
			// Responde imediatamente e não adiciona a mensagem ao contentor
			mMessageForInternalUse.eventType = EventType.PONG;
			mMessageForInternalUse.senderID = RemoteConnections.mLocalID;
			sendMessage(mMessageForInternalUse);
			break;

		case EventType.PONG:
			// Actualiza o RTT e não adiciona a mensagem ao contentor
			mSentPing = false;
			mRTT = (short) (Game.mCurrentTick - mLastRTTCheckTick);
			Game.LOGGER.log("Tick: " + mLastRTTCheckTick + " - >RTT ligação(" + RemoteConnections.mLocalID + "<->" + mRemoteID + "): " + mRTT);
			break;

		case EventType.LOCAL_SERVER_PORT:
			mRemoteServerPort = _msg.valInt;
			break;

		default:
			mMessagesContainer.add(_msg);
		}
	}
}