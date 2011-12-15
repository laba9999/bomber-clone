package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.input.InputLevelCompletedState;
import com.bomber.world.Level;

public class GameStateLevelCompleted extends GameState {

	private Integer mPoints;
	private boolean mEndScoreAnimation;
	public GameStateLevelCompleted(GameScreen _gameScreen) {
		super(_gameScreen);
		mEndScoreAnimation = false;
		mInput = new InputLevelCompletedState(this);
		mPoints = mGameWorld.getLocalPlayer().mPoints;
		mGameWorld.getLocalPlayer().mPoints += mGameWorld.mClock.getRemainingSeconds()/100;		
		mGameWorld.mClock.setUpdateInterval(50);
		
		
	}

	@Override
	public void onUpdate()
	{
		
		mInput.update();
		
		if(!mEndScoreAnimation && mGameWorld.mClock.hasCompletedUpdateInterval())
		{			
			if(!mGameWorld.mClock.mReachedZero)
				mPoints += 10;
			else
			{
				mPoints += 10;
				mEndScoreAnimation = true;
			}
		}

	}

	public void onPresent(float _interpolation)
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);
		
		// Desenha o vidro escuro
		mBatcher.enableBlending();
		mBatcher.draw(Assets.DarkGlass.get(), 0, 0);
		

		mBatcher.draw(Assets.mLevelCompletedScreen, 125,60);

		BitmapFont font = Assets.mFont;	
		font.setScale(1.8f);
		font.draw(mBatcher, Level.mInfo.mCurrentLevelName, 320, 405);
		font.setScale(1);
		//TODO: Highscore!
		font.draw(mBatcher,"HIGHSCORE", 235 ,330);		
		font.draw(mBatcher,"0000000", 450 ,330);
		
		
		font.draw(mBatcher,"TIME", 235 ,295);
		font.draw(mBatcher, mGameWorld.mClock.toString(), 450,295);
		
		font.draw(mBatcher,"FINAL SCORE",235,260 );
		font.draw(mBatcher, mPoints.toString(),450,260 );
	}

	@Override
	protected void onFinish()
	{
		// TODO Auto-generated method stub
		mGameWorld.getLocalPlayer().mPoints = mGameWorld.getLocalPlayer().mStartLevelPoints;
		mGameScreen.setGameState(mNextGameState);
	}

	@Override
	protected void onUpdateFinishing()
	{
		// TODO Auto-generated method stub
		
	}


}