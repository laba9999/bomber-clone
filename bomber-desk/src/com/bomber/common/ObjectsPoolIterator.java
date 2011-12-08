package com.bomber.common;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementa Iterator<T> Devolve o proximo elemento não null no mUsedObjects.
 */
public class ObjectsPoolIterator<T extends PoolObject> implements Iterator<T> {
	public ArrayList<T> mContainer;
	short mIndex = 0;

	public ObjectsPoolIterator(ArrayList<T> _container) {
		mContainer = _container;
	}

	public void reset()
	{
		mIndex = 0;
	}

	@Override
	public boolean hasNext()
	{
		for (short i = mIndex; i < mContainer.size(); i++)
			if (mContainer.get(i) != null)
			{
				mIndex = i;
				return true;
			}

		return false;
	}

	@Override
	public T next()
	{
		T next = null;
		for (short i = mIndex; i < mContainer.size(); i++)
		{
			next = mContainer.get(i);
			if (next != null)
			{
				mIndex = ++i;
				break;
			}
		}

		return next;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
}