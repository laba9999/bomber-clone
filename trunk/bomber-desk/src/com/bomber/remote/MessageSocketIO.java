package com.bomber.remote;

import java.nio.ByteBuffer;

public abstract class MessageSocketIO {
	
	// O número de bytes que será enviado por mensagem
	public static final short MESSAGE_SIZE = 64;
	/**
	 * O ByteBuffer que está ligado ao mRecvBytes
	 */
	public ByteBuffer mRecvByteBuffer;
	/**
	 * O ByteBuffer que está ligado ao mSendBytes
	 */
	public ByteBuffer mSendByteBuffer;
	/**
	 * Os bytes que vão ser recebidos
	 */
	public byte[] mRecvBytes = new byte[MESSAGE_SIZE];
	/**
	 * Os bytes que vão ser enviados
	 */
	public byte[] mSendBytes = new byte[MESSAGE_SIZE];
	/**
	 * È usada para as mensagens recebidas, evita ter que criar um objecto novo
	 * de cada vez que é recebida uma nova mensagem.
	 */
	public Message mMessage;

	public abstract void sendMessage(Message _message);

	public abstract Message recvMessage();
}