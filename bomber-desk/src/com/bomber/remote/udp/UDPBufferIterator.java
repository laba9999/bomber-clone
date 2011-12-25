package com.bomber.remote.udp;

import java.util.Iterator;

public class UDPBufferIterator implements Iterator<UDPMessage> {

	UDPCircularBuffer mBuffer;
	boolean mIsCircular;
	public short mPosition = 0;

	public UDPBufferIterator(UDPCircularBuffer _buffer, boolean _isCircular) {
		mBuffer = _buffer;
		mIsCircular = _isCircular;
	}

	@Override
	public boolean hasNext()
	{
		return mIsCircular || mPosition < mBuffer.mSize;
	}

	@Override
	public UDPMessage next()
	{
		if (mIsCircular)
			return mBuffer.mBuffer[(mPosition++ + 1) % mBuffer.mSize];
		else
			return mBuffer.mBuffer[mPosition++];
	}

	@Override
	public void remove()
	{

	}

}
