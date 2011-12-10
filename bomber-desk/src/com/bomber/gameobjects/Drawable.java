package com.bomber.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Drawable extends GameObject {

	public boolean mPlayAnimation;
	public boolean mAutoRepeat;
	public boolean mLooped = false;
	public TextureRegion mCurrentFrame;

	protected int mAnimationTicks = 0;
	private Animation mCurrentAnimation;
	private short mNumberOfAnimationFrames = 1;

	public void setCurrentAnimation(Animation _anim, short _numberOfFrames, boolean _play, boolean _autoRepeat)
	{
		mNumberOfAnimationFrames = _numberOfFrames;
		mCurrentAnimation = _anim;
		mCurrentFrame = _anim.getKeyFrame(0, false);
		mPlayAnimation = _play;
		mAnimationTicks = 0;
		mLooped = false;
		mAutoRepeat = _autoRepeat;
	}

	public void stopCurrentAnimation()
	{
		mAnimationTicks = 0;
		mPlayAnimation = false;
		mCurrentFrame = mCurrentAnimation.getKeyFrame(mAnimationTicks, false);
	}

	/**
	 * Actualiza o animation ticks. mas apenas se mLoopAnimation = true
	 */
	public void update()
	{
		if (!mPlayAnimation || (mLooped && !mAutoRepeat))
			return;

		mAnimationTicks++;

		// Verifica se a animação já deu a volta
		if (!mLooped && (mAnimationTicks >= mCurrentAnimation.frameDuration * mNumberOfAnimationFrames))
		{
			mLooped = true;
			
			if(!mAutoRepeat)
				return;
		}
		
		mCurrentFrame = mCurrentAnimation.getKeyFrame(mAnimationTicks, true);
	}

	/**
	 * É utilizado pela ObjectPool quando o objecto é marcado como disponivel.
	 * Este método deve ser sempre chamar o seu super antes/depois de efectuar
	 * mudanças numa classe derivada
	 * 
	 */
	public void reset()
	{
		mNumberOfAnimationFrames = 0;
		mCurrentAnimation = null;
		mAnimationTicks = 0;
		mLooped = false;
		mAutoRepeat = false;
	}
}