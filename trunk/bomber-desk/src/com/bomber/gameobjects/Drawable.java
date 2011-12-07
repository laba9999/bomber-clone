package com.bomber.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Drawable extends GameObject {

	public boolean mLoopAnimation;
	public boolean mLooped = false;
	public TextureRegion mCurrentFrame;
	
	private int mAnimationTicks = 0;
	private Animation mCurrentAnimation;
	private short mNumberOfAnimationFrames = 1;

	public void setCurrentAnimation(Animation _anim, short _duration)
	{
		mNumberOfAnimationFrames = _duration;
		mCurrentAnimation = _anim;
		mAnimationTicks = 0;
		mLooped = false;
	}

	/**
	 * Actualiza o animation ticks. mas apenas se o mLoopAnimation = true
	 */
	public void update()
	{
		if (!mLoopAnimation)
			return;
		
		mAnimationTicks++;
		mCurrentFrame = mCurrentAnimation.getKeyFrame(mAnimationTicks, true);

		// Verifica se a anima��o j� deu a volta
		if (!mLooped && (mAnimationTicks > mCurrentAnimation.frameDuration * mNumberOfAnimationFrames))
			mLooped = true;
	}

	/**
	 * � utilizado pela ObjectPool quando o objecto � marcado como disponivel.
	 * Este m�todo deve ser sempre chamar o seu super antes/depois de efectuar
	 * mudan�as numa classe derivada
	 */
	public void reset()
	{
		super.reset();
		
		mNumberOfAnimationFrames = 0;
		mCurrentAnimation = null;
		mAnimationTicks = 0;
		mLooped = false;
	}

	@Override
	public boolean equals(Object _rhs)
	{
		// TODO Auto-generated method stub
		return false;
	}
}