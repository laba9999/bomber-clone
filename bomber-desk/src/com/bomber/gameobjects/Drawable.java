package com.bomber.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;

public abstract class Drawable extends GameObject {
	public int mAnimationTicks = 0;
	/**
	 * Se TRUE então obtem a sprite baseado no numero de ticks, caso contrário
	 * obtem a 1ª
	 */
	public boolean mLoopAnimation;
	public Animation mCurrentAnimation;

	/**
	 * Actualiza o animation ticks. mas apenas se o mLoopAnimation = true
	 */
	public void update()
	{

	}

	/**
	 * É utilizado pela ObjectPool quando o objecto é marcado como disponivel.
	 * Este método deve ser sempre chamar o seu super antes/depois de efectuar
	 * mudanças numa classe derivada
	 */
	public void reset()
	{
		throw new UnsupportedOperationException();
	}
}