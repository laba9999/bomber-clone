package com.bomber.remote;

import com.bomber.Game;

public class Connection extends Thread {

	private static final short RTT_CHECK_INTERVAL = Game.TICKS_PER_SECOND * 1;
	private static final short TIMEOUT_VALUE = Game.TICKS_PER_SECOND * 1;

	/**
	 * ID que identifica este cliente perante todos os outros é atribuido pelo
	 * servidor.
	 */
	public short mLocalID;
	public short mRemoteID = -1;

	// Latência em ticks
	public short mRTT = 0;
	private long mLastRTTCheckTick = 0;
	private boolean mSentPing = false;

	private MessageSocketIO mSocket;
	private MessageContainer mMessagesContainer;

	// Para os pings/pongs e desconexões.
	private Message mMessageForInternalUse;

	public boolean mIsConnected = true;

	public Connection(MessageSocketIO _socket, MessageContainer _msgContainer) {
		mSocket = _socket;
		mMessagesContainer = _msgContainer;

		mMessageForInternalUse = new Message();
		mMessageForInternalUse.senderID = mLocalID;
	}

	@Override
	public String toString()
	{
		return "Remote id: " + mRemoteID + " - " + mSocket.toString();
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
		mMessageForInternalUse.remoteEventType = RemoteEventType.DISCONNECT;
		mMessageForInternalUse.valShort = mRemoteID;
		mMessageForInternalUse.setStringValue(_reason);
		mMessagesContainer.add(mMessageForInternalUse);

		mIsConnected = false;
		mSocket.close();

		System.out.println("Conexion " + mLocalID + " has disconnected...");
	}

	public void update()
	{
		if (!mIsConnected)
			return;

		if (mSentPing)
		{
			if ((Game.mCurrentTick - mLastRTTCheckTick) > TIMEOUT_VALUE)
			{
				System.out.println("Conexion " + mLocalID + " timed out...");
				disconnect("Timeout!");
			}
		}else
		{
			// Verifica a latência
			if (Game.mCurrentTick > (mLastRTTCheckTick + RTT_CHECK_INTERVAL))
			{
				mLastRTTCheckTick = Game.mCurrentTick;
				mMessageForInternalUse.messageType = MessageType.PING;
				sendMessage(mMessageForInternalUse);
				mSentPing = true;
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
			if (rcvedMsg == null)
				break;

			mRemoteID = rcvedMsg.senderID;
			addMessageToContainer(rcvedMsg);
		}

		disconnect("Erro receber mensagem!");
	}

	private void addMessageToContainer(Message _msg)
	{
		switch (_msg.messageType)
		{
		case MessageType.PING:
			// Responde imediatamente e não adiciona a mensagem ao contentor
			mMessageForInternalUse.messageType = MessageType.PONG;
			sendMessage(mMessageForInternalUse);
			break;

		case MessageType.PONG:
			// Actualiza o RTT e não adiciona a mensagem ao contentor
			mRTT = (short) (Game.mCurrentTick - mLastRTTCheckTick);
			System.out.println("RTT ligação(" + mLocalID + "): " + mRTT);
			mSentPing = false;
			break;

		default:
			mMessagesContainer.add(_msg);
		}
	}
}