package com.bomber.remote.udp;

import java.nio.ByteBuffer;

import com.bomber.common.PoolObject;
import com.bomber.remote.Message;

public class UDPMessage extends PoolObject {
	public short mSequenceId;
	public byte[] mMessage = new byte[Message.MESSAGE_SIZE + 2];
	public boolean mReceivedAck = true;
	ByteBuffer mBuffer = ByteBuffer.wrap(mMessage);

	public void set(short _sequenceId, ByteBuffer _msg)
	{
		mSequenceId = _sequenceId;
		mReceivedAck = false;

		mBuffer.position(0);
		mBuffer.putShort(_sequenceId);
		mBuffer.put(_msg);
	}

	public void cloneTo(UDPMessage _msg)
	{
		_msg.mSequenceId = mSequenceId;
		_msg.mReceivedAck = mReceivedAck;

		_msg.mBuffer.position(0);
		_msg.mBuffer.put(mMessage);

	}

	public void parse(byte[] _msg)
	{
		mBuffer.position(0);
		mBuffer.put(_msg);

		mBuffer.position(0);
		mSequenceId = mBuffer.getShort();

	}

	public void getMessage(byte[] _dest)
	{
		mBuffer.position(0);
		mBuffer.get(_dest);
	}

	public void getIOMessage(byte[] _dest)
	{
		mBuffer.position(2);
		mBuffer.get(_dest);
	}

	@Override
	public void reset()
	{
		mSequenceId = -1;
		mReceivedAck = false;

		mBuffer.position(0);
	}
}
