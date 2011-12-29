package com.bomber.remote;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

import com.badlogic.gdx.math.Vector2;
import com.bomber.common.PoolObject;

/**
 * Os atributos representam todo o tipo de dados que serão nececssários por
 * qualquer tipo de evento.
 */
public class Message extends PoolObject {
	// O número de bytes que será enviado por mensagem
	public static final short MESSAGE_SIZE = 62;
	public static final short STRING_MAX_SIZE = 22;
	/**
	 * este senderID identifica unicamente o jogador que a enviou. Este sender
	 * id é colocado durante a criação pela pool que por sua vez é um atributo
	 * da classe RemoteConnections.
	 */
	public short senderID;
	public int UUID;
	/**
	 * um dos MessageType
	 */
	public short messageType;
	/**
	 * um dos RemoteEventType
	 */

	public short eventType;
	public Vector2 valVector2_0 = new Vector2();
	public short valShort;
	public int valInt;

	// Sempre que o servidor enviar uma mensagem envia também o número de ticks
	public long valLong1;
	public long valLong2;
	private String valString = "";

	private static final byte[] cleanMsg = new byte[Message.MESSAGE_SIZE];
	private byte[] valStringBuffer = new byte[STRING_MAX_SIZE];

	//
	// Estes get/set são necessários para controlar o tamanho da string
	public String getStringValue()
	{
		return valString;
	}

	/**
	 * 
	 * @param _newString
	 *            Pode ter no máximo {@link #STRING_MAX_SIZE}
	 */
	public void setStringValue(String _newString)
	{
		if (_newString.length() > STRING_MAX_SIZE)
			throw new InvalidParameterException("String demasiado comprida!");

		valString = _newString;
	}

	/**
	 * O ByteBuffer oferece metodos do tipo getFloat, etc...
	 */
	public void parse(ByteBuffer _buff)
	{
		_buff.clear();

		senderID = _buff.getShort();
		UUID = _buff.getInt();
		messageType = _buff.getShort();
		eventType = _buff.getShort();

		valVector2_0.x = _buff.getFloat();
		valVector2_0.y = _buff.getFloat();

		valShort = _buff.getShort();
		valInt = _buff.getInt();
		valLong1 = _buff.getLong();
		valLong2 = _buff.getLong();

		_buff.get(valStringBuffer, 0, valStringBuffer.length);
		valString = new String(valStringBuffer).trim();

		_buff.clear();
	}

	/**
	 * Transforma os atributos em bytes. ByteBuffer oferece metodos do tipo
	 * putFloat();
	 */
	public void fillBuffer(ByteBuffer _destination)
	{
		_destination.clear();
		_destination.put(cleanMsg);
		_destination.clear();
		
		_destination.putShort(senderID);
		_destination.putInt(UUID);
		_destination.putShort(messageType);
		_destination.putShort(eventType);

		_destination.putFloat(valVector2_0.x);
		_destination.putFloat(valVector2_0.y);

		_destination.putShort(valShort);
		_destination.putInt(valInt);
		_destination.putLong(valLong1);
		_destination.putLong(valLong2);
		
		_destination.put(valString.getBytes());

		_destination.clear();
	}

	public synchronized void cloneTo(Message _newMessage)
	{
		_newMessage.senderID = senderID;
		_newMessage.UUID = UUID;
		_newMessage.messageType = messageType;
		_newMessage.eventType = eventType;
		_newMessage.valVector2_0.set(valVector2_0);
		_newMessage.valShort = valShort;
		_newMessage.valInt = valInt;
		_newMessage.valLong1 = valLong1;
		_newMessage.valLong2 = valLong2;
		_newMessage.valString = new String(valString);
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("Sender ID: " + senderID).append("\r\n");
		str.append("UUID: " + UUID).append("\r\n");
		str.append("valVector2_0: (" + valVector2_0.x + ", " + valVector2_0.y + ")").append("\r\n");
		str.append("valShort: " + valShort).append("\r\n");
		str.append("valInt: " + valInt).append("\r\n");
		str.append("valString: " + valString);

		return str.toString();
	}

	@Override
	public void reset()
	{}
}