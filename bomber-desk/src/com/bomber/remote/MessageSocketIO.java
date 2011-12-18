package com.bomber.remote;

import java.nio.ByteBuffer;

public abstract class MessageSocketIO {

	// O número de bytes que será enviado por mensagem
	public static final short MESSAGE_SIZE = 64;
	/**
	 * O ByteBuffer que está ligado ao mRecvBytes
	 */
	private ByteBuffer mRecvByteBuffer;
	/**
	 * O ByteBuffer que está ligado ao mSendBytes
	 */
	private ByteBuffer mSendByteBuffer;
	/**
	 * Os bytes que vão ser recebidos
	 */
	protected byte[] mRecvBytes = new byte[MESSAGE_SIZE];
	/**
	 * Os bytes que vão ser enviados
	 */
	protected byte[] mSendBytes = new byte[MESSAGE_SIZE];
	/**
	 * È usada para as mensagens recebidas, evita ter que criar um objecto novo
	 * de cada vez que é recebida uma nova mensagem.
	 */
	private Message mReceivedMessage;

	public boolean mIsClosed = false;
	public MessageSocketIO() {
		mRecvByteBuffer = ByteBuffer.wrap(mRecvBytes);
		mSendByteBuffer = ByteBuffer.wrap(mSendBytes);

		mReceivedMessage = new Message();
	}

	
	public synchronized boolean sendMessage(Message _msg)
	{
		if(mIsClosed)
			return false;
		
		_msg.fillBuffer(mSendByteBuffer);
		return onSendMessage();
	}
	
	public synchronized void close()
	{
		if(mIsClosed)
			return;
		
		mIsClosed = true;
		
		onClose();
	}

	public Message recvMessage()
	{
		if(!onRecvMessage())
			return null;
		
		mReceivedMessage.parse(mRecvByteBuffer);
		
		return mReceivedMessage;
	}
	
	protected abstract void onClose();
	
	/**
	 * Deve implementar o envio do array {@link #mSendBytes}.
	 */
	protected abstract boolean onSendMessage();

	/**
	 * Deve implementar o recebimento de mensagens. O que é recebido é um array
	 * de bytes, esse array deve ser o {@link mRecvBytes}. De seguida deve ser
	 * actualizada a {@link mReceivedMessage} passando-lhe o
	 * {@link mRecvByteBuffer}. De seguida a {@link mReceivedMessage} deve ser
	 * adicionada ao {@link mMessageContainer} que contém as mensagens à espera
	 * de serem tratadas .
	 */
	protected abstract boolean onRecvMessage();
}