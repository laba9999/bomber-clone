package com.bomber.gameobjects;

import com.bomber.world.GameWorld;

public abstract class Monster extends KillableObject {
	private int mPointsValue;
	private short mMonsterType;
	public GameWorld mWorld;

	@Override
	public void update()
	{
		super.update();
		
		if(mIsDead)
		{
			// TODO: Verificar se chegou ao ultimo frame da animação
			// e se sim remover-se da lista
			mWorld.mMonsters.releaseObject(this);
		}
		
	}
	
	/**
	 * Baseado no monster type.
	 */
	@Override
	public abstract boolean equals(Object _rhs);

	protected abstract void onChangedDirection();

	protected abstract void onStop();

	protected abstract void onKill();

	public void interactWithPlayer(Player _player)
	{
		throw new UnsupportedOperationException();
	}
}