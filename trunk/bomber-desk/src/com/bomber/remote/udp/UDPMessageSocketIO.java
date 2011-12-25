package com.bomber.remote.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.LinkedList;

import com.bomber.Game;
import com.bomber.common.ObjectFactory;
import com.bomber.common.ObjectsPool;
import com.bomber.remote.EventType;
import com.bomber.remote.Message;
import com.bomber.remote.MessageSocketIO;
import com.bomber.remote.MessageType;

public class UDPMessageSocketIO extends MessageSocketIO {
	private short BUFFERS_SIZE = 32;

	DatagramSocket mSocket = null;
	DatagramPacket mSendPacket = null;
	DatagramPacket mRecvPacket = null;

	private short mSendSequenceId = 0;
	private short mLastCorrectSequenceId = -1;

	private UDPCircularBuffer mLastMessagesSent = new UDPCircularBuffer(BUFFERS_SIZE);
	private UDPCircularBuffer mMessagesToSend = new UDPCircularBuffer(BUFFERS_SIZE);
	private LinkedList<UDPMessage> mMessagesReceived = new LinkedList<UDPMessage>();
	private ObjectsPool<UDPMessage> mMessagesReceivedPool = new ObjectsPool<UDPMessage>(false, BUFFERS_SIZE, new ObjectFactory.CreateUDPMessage());

	UDPMessage mAuxiliarUDPMsg = new UDPMessage();
	Message mAuxiliarIOMsg = new Message();

	private byte[] mUDPSendBytes = new byte[Message.MESSAGE_SIZE + 2];
	private byte[] mUDPReceiveBytes = new byte[Message.MESSAGE_SIZE + 2];

	private byte[] mSendIOBytes = new byte[Message.MESSAGE_SIZE];
	private ByteBuffer mSendIOBuffer = ByteBuffer.wrap(mSendIOBytes);

	private String mRemoteAddress;

	private boolean mHasSetupSendPacket = false;

	public UDPMessageSocketIO(String _remoteAddress, int _port) throws IOException {
		BUFFERS_SIZE = (short) ((Game.mNumberPlayers - 1) * 32);
		mSocket = new DatagramSocket();
		mSocket.setSoTimeout(5);

		System.out.println("Porto UDP atribuido:" + mSocket.getLocalPort());

		mRemoteAddress = _remoteAddress + ":" + _port;
		InetAddress addr = InetAddress.getByName(_remoteAddress);

		mSendPacket = new DatagramPacket(mUDPSendBytes, mUDPSendBytes.length, addr, Integer.valueOf(_port));
		mRecvPacket = new DatagramPacket(mUDPReceiveBytes, mUDPReceiveBytes.length);
		mSocket.send(mSendPacket);
	}

	public UDPMessageSocketIO(DatagramPacket _packet) throws IOException {
		BUFFERS_SIZE = (short) ((Game.mNumberPlayers - 1) * 32);
		mSocket = new DatagramSocket();
		mSocket.setSoTimeout(5);

		mHasSetupSendPacket = true;

		mRemoteAddress = _packet.getAddress().getHostAddress() + ":" + _packet.getPort();
		mSendPacket = new DatagramPacket(mUDPSendBytes, mUDPSendBytes.length, _packet.getAddress(), _packet.getPort());
		mRecvPacket = new DatagramPacket(mUDPReceiveBytes, mUDPReceiveBytes.length);
	}

	@Override
	public String toString()
	{
		return mRemoteAddress;
	}

	private synchronized void addSentMessage(UDPMessage _msg)
	{
		UDPMessage newMsg = mLastMessagesSent.getNextFree();
		_msg.cloneTo(newMsg);
		_msg.mReceivedAck = false;
	}

	private synchronized void freeSentMessage(short _sequenceId)
	{
		if (mLastMessagesSent.mFreePositions == BUFFERS_SIZE)
		{
			System.out.println("Tentativa de ACK de uma mensagem ainda não existente: " + _sequenceId);
			return;
		}

		// Procura a mensagem com este Id
		boolean messageExists = false;
		for (UDPMessage msg : mLastMessagesSent)
		{
			if (msg.mSequenceId == _sequenceId)
			{
				if (msg.mReceivedAck)
					System.out.println("ACK de uma mensagem já ACKed!");

				msg.mReceivedAck = true;
				messageExists = true;
				break;
			}
		}

		if (!messageExists)
		{
			System.out.println("Tentativa de ACK de uma mensagem já não existente no buffer: " + _sequenceId);
			return;
		}

		// Actualiza manualmente o numero de mensagens livres neste buffer
		Iterator<UDPMessage> it = mLastMessagesSent.iterator(mLastMessagesSent.mInsertIdx);
		mLastMessagesSent.mFreePositions = 0;
		while (it.next().mReceivedAck && mLastMessagesSent.mFreePositions < BUFFERS_SIZE)
			mLastMessagesSent.mFreePositions++;
		
		//System.out.println("Sent free positions: " + mLastMessagesSent.mFreePositions);
	}

	private synchronized void addMessageToSend()
	{
		UDPMessage newMsg = mMessagesToSend.getNextFree();
		newMsg.set(mSendSequenceId++, mSendByteBuffer);
	}

	private synchronized void addMessageToReceived(UDPMessage _msg) throws IOException
	{
		UDPMessage newMsg = mMessagesReceivedPool.getFreeObject();
		_msg.cloneTo(newMsg);

		_msg.getIOMessage(mRecvBytes);
		mAuxiliarIOMsg.parse(mRecvByteBuffer);

		//System.out.println("Adicionada msg, evento: " + mAuxiliarIOMsg.eventType + ", sequencia: " + _msg.mSequenceId);

		//
		// Coloca a mensagem no sitio correcto

		// Verifica se é o primeiro elemento
		if (mMessagesReceived.size() == 0)
		{
			mMessagesReceived.add(newMsg);
			return;
		}

		// verifica se é menor que a cabeça
		if (newMsg.mSequenceId < mMessagesReceived.peek().mSequenceId)
		{
			mMessagesReceived.add(0, newMsg);
			return;
		}
		// verifica se é maior que a cauda
		else if (newMsg.mSequenceId > mMessagesReceived.peekLast().mSequenceId)
		{
			mMessagesReceived.add(newMsg);
			return;
		}

		for (int i = 1; i < mMessagesReceived.size(); i++)
		{
			if (newMsg.mSequenceId < mMessagesReceived.get(i).mSequenceId)
			{
				mMessagesReceived.add(i, newMsg);
				return;
			}
		}
	}

	private synchronized void resendMessage(short _sequenceId) throws IOException
	{
		for (UDPMessage msg : mLastMessagesSent)
		{
			if (msg.mSequenceId == _sequenceId)
			{
				mSendPacket.setData(msg.mMessage);
				mSocket.send(mSendPacket);
				
				System.out.println("Reenviada msg: " + _sequenceId);
				return;
			}
		}

		// Não é suposto chegar aqui
		throw new InvalidParameterException("A mensagem já não se encontra no buffer!");
	}

	private synchronized boolean checkReceivedMessages() throws IOException
	{
		if (mMessagesReceived.size() == 0)
			return false;

		if (mLastCorrectSequenceId + 1 == mMessagesReceived.peek().mSequenceId)
		{
			UDPMessage msg = mMessagesReceived.removeFirst();

			mLastCorrectSequenceId++;
			msg.getIOMessage(mRecvBytes);

			mMessagesReceivedPool.releaseObject(msg);

			return true;
		}

		// Está pelo menos 1 mensagem em falta...
		mAuxiliarIOMsg.messageType = MessageType.CONNECTION;
		mAuxiliarIOMsg.eventType = EventType.UDP_RESEND;
		mAuxiliarIOMsg.fillBuffer(mSendIOBuffer);
		mAuxiliarUDPMsg.set((short) (mLastCorrectSequenceId + 1), mSendIOBuffer);

		mSendPacket.setData(mAuxiliarUDPMsg.mMessage);
		mSocket.send(mSendPacket);
		System.out.println("Mensagem perdida!");
		return false;
	}

	private synchronized void sendCachedMessages() throws IOException
	{
		UDPMessage msg = mMessagesToSend.getNextOccupied();

		if (msg == null)
			return;

		mMessagesToSend.mFreePositions++;
		mSendPacket.setData(msg.mMessage);
		mSocket.send(mSendPacket);

		addSentMessage(msg);

		msg.getIOMessage(mSendIOBytes);
		mAuxiliarIOMsg.parse(mSendIOBuffer);
		//System.out.println("Enviada msg tipo: " + mAuxiliarIOMsg.eventType + " para o porto: " + mSendPacket.getPort() + " do porto: " + mSocket.getLocalPort());

	}

	@Override
	public boolean onSendMessage()
	{
		addMessageToSend();
		return true;
	}

	@Override
	public boolean onRecvMessage()
	{
		while (true)
		{
			try
			{
				mSocket.receive(mRecvPacket);

				if (!mHasSetupSendPacket)
				{
					mSendPacket = new DatagramPacket(mUDPSendBytes, mUDPSendBytes.length, mRecvPacket.getAddress(), mRecvPacket.getPort());
					mHasSetupSendPacket = true;
				}

				// Recebemos uma mensagem
				mAuxiliarUDPMsg.parse(mUDPReceiveBytes);

				// Obtem a mensagem sem o cabeçalho UDP
				mAuxiliarUDPMsg.getIOMessage(mRecvBytes);
				mAuxiliarIOMsg.parse(mRecvByteBuffer);

				// Verifica se é um ACK
				if (mAuxiliarIOMsg.eventType == EventType.UDP_ACK)
					freeSentMessage(mAuxiliarUDPMsg.mSequenceId);

				// Verifica se é um pedido de reenvio
				else if (mAuxiliarIOMsg.eventType == EventType.UDP_RESEND)
					resendMessage(mAuxiliarUDPMsg.mSequenceId);

				// Não queremos msg repetidas
				else if (!(mAuxiliarUDPMsg.mSequenceId <= mLastCorrectSequenceId))
				{
					addMessageToReceived(mAuxiliarUDPMsg);

					// Envia um ACK para esta mensagem
					mAuxiliarIOMsg.messageType = MessageType.CONNECTION;
					mAuxiliarIOMsg.eventType = EventType.UDP_ACK;
					mAuxiliarIOMsg.fillBuffer(mSendIOBuffer);
					mAuxiliarUDPMsg.set(mAuxiliarUDPMsg.mSequenceId, mSendIOBuffer);

					mSendPacket.setData(mAuxiliarUDPMsg.mMessage);
					mSocket.send(mSendPacket);

					//System.out.println("Enviado ACK para o porto: " + mSendPacket.getPort() + " do porto: " + mSocket.getLocalPort());
				}

			} catch (SocketTimeoutException e)
			{

			} catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}

			// Envia mensagens em atraso
			try
			{
				if (mHasSetupSendPacket)
					sendCachedMessages();
			} catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}

			// Verifica se existem mensagens na ordem correcta prontas a
			// serem entregues
			try
			{
				if (checkReceivedMessages())
					return true;
			} catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	@Override
	protected void onClose()
	{
		if (mSocket != null)
			mSocket.close();

	}

}