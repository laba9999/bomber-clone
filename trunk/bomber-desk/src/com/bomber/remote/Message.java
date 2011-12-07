package com.bomber.remote;

import java.nio.ByteBuffer;

import com.badlogic.gdx.math.Vector2;
import com.bomber.common.PoolObject;

/**
 * Os atributos representam todo o tipo de dados que serão nececssários por
 * qualquer tipo de evento.
 */
public class Message extends PoolObject {
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
	public short remoteEventType;
	public Vector2 valVector2_0;
	private Vector2 valVector2_1;
	public short valShort;
	public int valInt;
	public String valString;

	/**
	 * O ByteBuffer oferece metodos do tipo getFloat, etc...
	 */
	public void parse(ByteBuffer _buff)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Transforma os atributos em bytes. ByteBuffer oferece metodos do tipo
	 * putFloat();
	 */
	public void fillBuffer(ByteBuffer _destination)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		
	}
}