package com.bomber.common;

/**
 * Vai encapsular os objectos que existiram no ObjectsPool
 */
public abstract class PoolObject {
	public Short mIndex;
	
	/**
	 * É utilizado pela ObjectPool quando o objecto é marcado como disponivel.
	 * Este método deve ser sempre chamar o seu super antes/depois de efectuar
	 * mudanças numa classe derivada
	 * 
	 * mIsBeingDestroyed = false;
	 */
	public abstract void reset();
}