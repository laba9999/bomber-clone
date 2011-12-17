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
	private MessageContainer mMessageContainer;

	public Message mMessageToSend;

	public MessageSocketIO(MessageContainer _container) {
		mRecvByteBuffer = ByteBuffer.wrap(mRecvBytes);
		mSendByteBuffer = ByteBuffer.wrap(mSendBytes);
		mMessageContainer = _container;

		mReceivedMessage = new Message();
		mMessageToSend = new Message();
	}

	public boolean sendMessage()
	{
		mMessageToSend.fillBuffer(mSendByteBuffer);
		return onSendMessage();
	}

	/**
	 * Deve ser chamada de cada vez que o array {@link mRecvBytes} é
	 * actualizado.
	 */
	protected void onNewMessageReceived()
	{
		mReceivedMessage.parse(mRecvByteBuffer);
		mMessageContainer.add(mReceivedMessage);
	}

	/**
	 * Deve implementar o envio do array {@link #mSendBytes}.
	 */
	public abstract boolean onSendMessage();

	/**
	 * Deve implementar o recebimento de mensagens. O que é recebido é um array
	 * de bytes, esse array deve ser o {@link mRecvBytes}. De seguida deve ser
	 * actualizada a {@link mReceivedMessage} passando-lhe o
	 * {@link mRecvByteBuffer}. De seguida a {@link mReceivedMessage} deve ser
	 * adicionada ao {@link mMessageContainer} que contém as mensagens à espera
	 * de serem tratadas .
	 */
	public abstract boolean recvMessage();
}