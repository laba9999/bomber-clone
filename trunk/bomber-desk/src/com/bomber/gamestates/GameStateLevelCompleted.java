package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;
import com.bomber.input.InputLevelCompletedState;
import com.bomber.world.Level;

public class GameStateLevelCompleted extends GameState {

	private Integer mHighScoreDisplayed;
	private Integer mPointsDisplayed;
	private boolean mEndScoreAnimation;
	private short mTrophiesEarned;

	public GameStateLevelCompleted(GameScreen _gameScreen) {
		super(_gameScreen);
		mEndScoreAnimation = false;
		mInput = new InputLevelCompletedState(this);

		Player localPlayer = mGameWorld.getLocalPlayer();
		mPointsDisplayed = localPlayer.mPoints - mGameWorld.getLocalPlayer().mStartLevelPoints;

		// atribui nova pontuação ao jogador pelo extra do tempo restante
		localPlayer.mPoints += mGameWorld.mClock.getRemainingSeconds() / 100;

		// verifica se pontuação actual é melhor que o highscore
		// e guarda já o valor antes que o utilizador salte a animação da
		// pontuação
		mHighScoreDisplayed = Level.mInfo.getHighScore();
		int newHighScore = mPointsDisplayed + mGameWorld.mClock.getRemainingSeconds() / 100 + 10;
		if (newHighScore >= mHighScoreDisplayed)
			Level.mInfo.setHighScore( newHighScore);

		// calcula quantidade de troféus a apresentar
		float x = (float) mPointsDisplayed / mGameWorld.getMaxPoints();

		if (x >= 0.9)
			mTrophiesEarned = 3;
		else if (x == 0.9 && x >= 0.6)
			mTrophiesEarned = 2;
		else if (x == 0.6 && x >= 0.3)
			mTrophiesEarned = 1;
		else
			mTrophiesEarned = 0;

		mGameWorld.mClock.setUpdateInterval(50);
	}

	@Override
	public void onUpdate()
	{

		mInput.update();

		// animação da pontuação
		if (!mEndScoreAnimation && mGameWorld.mClock.hasCompletedUpdateInterval())
		{
			if (!mGameWorld.mClock.mReachedZero)
				mPointsDisplayed += 10;
			else
			{
				mPointsDisplayed += 10;
				mEndScoreAnimation = true;
			}

			if (mPointsDisplayed > mHighScoreDisplayed)
				mHighScoreDisplayed = mPointsDisplayed;
		}

	}

	public void onPresent(float _interpolation)
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);

		// Desenha o vidro escuro
		mBatcher.enableBlending();
		mBatcher.draw(Assets.DarkGlass.get(), 0, 0);

		mBatcher.draw(Assets.mScreens.get("levelcompleted"), 125, 60);

		BitmapFont font = Assets.mFont;
		font.setScale(1.8f);
		font.draw(mBatcher, Level.mInfo.mCurrentLevelName, 320, 405);
		font.setScale(1);

		font.draw(mBatcher, "HIGHSCORE", 235, 330);
		font.draw(mBatcher, mHighScoreDisplayed.toString(), 450, 330);

		font.draw(mBatcher, "TIME", 235, 295);
		font.draw(mBatcher, mGameWorld.mClock.toString(), 450, 295);

		font.draw(mBatcher, "FINAL SCORE", 235, 260);
		font.draw(mBatcher, mPointsDisplayed.toString(), 450, 260);

		for (int i = 0; i < mTrophiesEarned; i++)
		{
			mBatcher.draw(Assets.mTrophy, 304 + i * 63, 18);

		}
	}

	@Override
	protected void onFinish()
	{
		mGameWorld.getLocalPlayer().mPoints = mGameWorld.getLocalPlayer().mStartLevelPoints;
		mGameScreen.setGameState(mNextGameState);
	}

	@Override
	protected void onUpdateFinishing()
	{
		// TODO Auto-generated method stub

	}
}