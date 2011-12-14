package com.bomber.common;

/**
 * Vai encapsular os objectos que existiram no ObjectsPool
 */
public abstract class PoolObject {
	private Short mIndex;
	public int mOwnerUUID = -1;
	
	public void setIndex(int _uuid, Short _idx)
	{
		if(mOwnerUUID == -1)
			mOwnerUUID = _uuid;
		
		if( _uuid != mOwnerUUID)
			throw new IllegalStateException(); 
		
		
		mIndex = _idx;
	}
	
	public Short getIndex(){
		return mIndex;
	}
	
	/**
	 * É utilizado pela ObjectPool quando o objecto é marcado como disponivel.
	 * Este método deve ser sempre chamar o seu super antes/depois de efectuar
	 * mudanças numa classe derivada
	 */
	public abstract void reset();
}