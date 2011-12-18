package com.bomber.remote;

import java.util.LinkedList;

import com.bomber.Game;
import com.bomber.common.ObjectFactory;
import com.bomber.common.ObjectsPool;

public class MessageContainer {
	private ObjectsPool<Message> mMessagesPool;
	private LinkedList<Message> mMessages;

	private int mMessagesPerSecondCount = 0;
	private short mTicks = 0;
	public static Integer mMessagesPerSecond = 0;
	
	public MessageContainer() {
		mMessages = new LinkedList<Message>();
		mMessagesPool = new ObjectsPool<Message>((short) 10, new ObjectFactory.CreateMessage());
	}

	
	public void update()
	{
		if(mTicks++ >= Game.TICKS_PER_SECOND)
		{
			mMessagesPerSecond = mMessagesPerSecondCount;
			mMessagesPerSecondCount = 0;
			mTicks = 0;
		}
	}
	
	/**
	 * Adiciona uma nova mensagem à fila de mensagens à espera de serem
	 * tratadas. A mensagem é copiada para uma pertencente à pool de mensagens
	 * locais e por isso pode ser reutilizada.
	 * 
	 * @param _message
	 *            A mensagem a adicionar
	 */
	public synchronized void add(Message _message)
	{
		
		mMessagesPerSecondCount++;
		
		Message tmpMessage;

		switch (_message.eventType)
		{
		case EventType.SYNC:

			// Um SYNC do servidor, podemos eliminar todas as mensagens do mesmo
			// tipo referentes ao mesmo objecto
			for (int i = 0; i < mMessages.size(); i++)
			{
				tmpMessage = mMessages.get(i);

				if (tmpMessage.UUID != _message.UUID)
					continue;

				if (tmpMessage.messageType == _message.messageType)
				{
					mMessages.remove(i);
					i--;
				}
			}

			// Os SYNC's têm prioridade sobre todos os outros tipos excepto os
			// DISCONNECTS por isso adiciona ao inicio da fila
			tmpMessage = mMessagesPool.getFreeObject();
			_message.cloneTo(tmpMessage);
			mMessages.addFirst(tmpMessage);
			break;

		case EventType.DISCONNECT:
			tmpMessage = mMessagesPool.getFreeObject();
			_message.cloneTo(tmpMessage);
			mMessages.addFirst(tmpMessage);
			break;

		default:
			// Adiciona a nova mensagem ao final da fila
			tmpMessage = mMessagesPool.getFreeObject();
			_message.cloneTo(tmpMessage);
			mMessages.add(tmpMessage);
		}
	}

	/**
	 * Deve ser chamado assim que uma mensagem obtida através do método
	 * {@link #getNext()} tiver sido tratada para que o objecto seja devolvido à
	 * pool.
	 * 
	 * @param _msg
	 *            A mensagem a libertar
	 */
	public synchronized void setParsed(Message _msg)
	{
		mMessagesPool.releaseObject(_msg);
	}

	/**
	 * Devolve uma referencia para a proxima mensagem na fila. Após o tratamento
	 * da mensagem, esta deve ser libertada usando o método.
	 * {@link #setParsed(Message)}
	 */
	public synchronized Message getNext()
	{
		return mMessages.remove();
	}
	
	public boolean hasNext()
	{
		return !mMessages.isEmpty();
	}
}