package com.bomber.common;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Se a opção de atribuir o hashcode estiver activa então o mUUID será criado
 * pela classe ObjectPool aquando da criação do novo objecto.
 * 
 * Dá muito jeito poder iterar uma colecção por isso existirá apenas um iterador
 * 
 * Implementa Iterable<T>
 */
public class ObjectsPool<T> {
	private Stack<PoolObject> mFreeObjects;
	private ArrayList<T> mUsedObjects;
	private Stack<Short> mFreePositions;
	public Factory<T> mFactory;
	public ObjectsPoolIterator<T> mObjectsIterator;

	public ObjectsPool(short _initialQuantity, boolean _autoCreateHashs,
			Factory<T> _factory) {
		throw new UnsupportedOperationException();
	}

	/**
	 * coloca o objecto devolvido(obtido do topo da stack mFreeObjects) na
	 * posição do topo da mFreePositions.
	 */
	public T getFreeObject()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Passa o objecto do array de ocupados para a stack de livres. Os objectos
	 * não são retirados do array de usados, a sua posição é apenas colocada a
	 * null e esse indice é adicionado à stack de posições livres.
	 */
	public void releaseObject(PoolObject obj)
	{
		throw new UnsupportedOperationException();
	}
}