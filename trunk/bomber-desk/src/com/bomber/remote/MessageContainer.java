package com.bomber.remote;

import java.util.concurrent.ArrayBlockingQueue;

import com.bomber.common.ObjectsPool;

public class MessageContainer {
	private ObjectsPool<Message> mMessagesPool;
	/**
	 * Uma fila de mensagens recebidas. A criação/obtenção destas mensagens é
	 * gerida pela pool.
	 */
	private ArrayBlockingQueue<Message> mMessages;

	/**
	 * A mensagem do parametro deve ser copiada para uma nova já existente na
	 * pool pois esta será reutlizada.
	 * 
	 * Se for uma mensagem do servidor do tipo SYNC, elimina todas as mensagens
	 * do mesmo tipo que já existam.
	 */
	public synchronized void add(Message _message)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Devolve uma referencia para a proxima mensagem na fila.
	 */
	public Message getNext()
	{
		throw new UnsupportedOperationException();
	}
}