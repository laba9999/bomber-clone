package com.bomber.remote.udp;

import java.nio.BufferOverflowException;
import java.util.Iterator;

public class UDPCircularBuffer implements Iterable<UDPMessage> {
	public UDPMessage[] mBuffer;
	public short mInsertIdx = 0;
	public short mFirstOccupied = 0;
	public short mFreePositions;
	public short mSize;

	public UDPBufferIterator mIterator;
	public UDPBufferIterator mCircularIterator;

	public UDPCircularBuffer(short _size) {
		mBuffer = new UDPMessage[_size];
		
		for(short i = 0; i < _size; i++)
			mBuffer[i] = new UDPMessage();
		
		mFreePositions = _size;
		mSize = _size;

		mIterator = new UDPBufferIterator(this, false);
		mCircularIterator = new UDPBufferIterator(this, true);
	}

	public void add(UDPMessage _newMessage)
	{
		if (mFreePositions-- < 0)
		{
			System.out.println("Buffer overflow!");
			throw new BufferOverflowException();
		}

		mBuffer[mInsertIdx] = _newMessage;
		mInsertIdx = (short) ((mInsertIdx + 1) % mSize);
	}

	public UDPMessage getNextFree()
	{
		if (mFreePositions-- < 0)
		{
			System.out.println("Buffer overflow!");
			throw new BufferOverflowException();
		}

		
		UDPMessage res = mBuffer[mInsertIdx];

		mInsertIdx = (short) ((mInsertIdx + 1) % mSize);
		
		return res;
	}

	public UDPMessage getNextOccupied()
	{
		if (mFreePositions == mSize)
			return null;

		UDPMessage res = mBuffer[mFirstOccupied];
		mFirstOccupied = (short) ((mFirstOccupied + 1) % mSize);

		return res;
	}

	@Override
	public Iterator<UDPMessage> iterator()
	{
		mIterator.mPosition = 0;
		return mIterator;
	}

	public Iterator<UDPMessage> iterator(short _startpos)
	{
		mCircularIterator.mPosition = (short) (_startpos - 1);
		return mCircularIterator;
	}

}
