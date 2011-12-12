package com.bomber.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Dá muito jeito poder iterar uma colecção por isso existirá apenas um iterador
 * 
 * Implementa Iterable<T>
 */
public class ObjectsPool<T extends PoolObject> implements Iterable<T> {
	private Stack<T> mFreeObjects = new Stack<T>();
	private ArrayList<T> mUsedObjects = new ArrayList<T>();
	private Stack<Short> mFreePositions = new Stack<Short>();
	private Factory<T> mFactory;
	private ObjectsPoolIterator<T> mObjectsIterator;

	public ObjectsPool(short _initialQuantity, Factory<T> _factory) {

		mFactory = _factory;

		// Inicializa os containers
		if (null != mFactory)
			allocateNewObjects(_initialQuantity);

		mObjectsIterator = new ObjectsPoolIterator<T>(mUsedObjects);
	}

	public void clear()
	{
		for(T tmp : this)
			releaseObject(tmp);
	}
	
	private void allocateNewObjects(short _quantity)
	{
		if (null == mFactory)
			throw new UnsupportedOperationException();

		short freePositionStart = (short) mUsedObjects.size();
		for (short i = 0; i < _quantity; freePositionStart++, i++)
		{
			T tmpObject = mFactory.create();

			mFreeObjects.push(tmpObject);
			mFreePositions.push(freePositionStart);
			mUsedObjects.add(null);
		}
	}

	public void addObject(T _obj)
	{
		// Verifica se existem lugares livres no array
		if (!mFreePositions.empty())
		{
			_obj.mIndex = mFreePositions.pop();
			mUsedObjects.set(_obj.mIndex, _obj);
		} else
		{
			_obj.mIndex = (short) mUsedObjects.size();
			mUsedObjects.add(_obj);
		}
	}

	/**
	 * coloca o objecto devolvido(obtido do topo da stack mFreeObjects) na
	 * posição do topo da mFreePositions.
	 */
	public T getFreeObject()
	{
		if (null == mFactory)
			throw new UnsupportedOperationException();

		// Verifica se existem objectos livres disponiveis
		if (mFreeObjects.size() == 0)
			allocateNewObjects((short) 1);

		// Obtem a posição onde vai ser inserido no array de objectos ocupados
		Short insertPos = mFreePositions.pop();

		// Obtem o objecto a devolver
		T result = mFreeObjects.pop();
		result.reset();
		
		// Actualiza o index do objecto para o libertar mais tarde
		result.mIndex = insertPos;

		mUsedObjects.set(insertPos, result);

		return result;
	}

	public void releaseObject(T obj)
	{
		short freeIndex = obj.mIndex;
		
		// Se não tiver sido providenciado uma factory então significa que os objectos
		// foram inseridos à mão e isto significa que não são reaproveitáveis, logo
		// não vale a pena guardar
		if (null != mFactory)
			mFreeObjects.push(obj);
		
		mFreePositions.push(freeIndex);
		mUsedObjects.set(freeIndex, null);
	}

	@Override
	public Iterator<T> iterator()
	{
		// Faz reset ao iterador e devolve sempre o mesmo
		mObjectsIterator.reset();
		return mObjectsIterator;
	}
	
	public int usedObjects()
	{
		return mUsedObjects.size() - mFreeObjects.size();
	}
}