package com.bomber.gameobjects;

import com.bomber.world.GameWorld;

/**
 * A bomba quando rebenta chama o metodo respectivo no GameWorld, que faz todas
 * as verificações e só depois cria a explosão.
 */
public abstract class Bomb extends MovableObject {
	private int mTicksUntilExplosion;
	private short mBombPower = 3;
	public GameWorld mWorld;

	public abstract void update();
}