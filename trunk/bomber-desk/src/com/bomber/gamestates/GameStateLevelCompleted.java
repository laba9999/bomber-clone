package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.Settings;
import com.bomber.Game;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.common.assets.Level;
import com.bomber.common.assets.SoundAssets;
import com.bomber.gameobjects.Player;
import com.bomber.input.InputLevelCompletedState;

public class GameStateLevelCompleted extends GameState {
	private static float START_X = 235;
	private static final float INTERVAL_BETWEEN_TEXT_AND_VALUE = 50;
	private Integer mHighScoreDisplayed;
	private Integer mPointsDisplayed;
	private boolean mEndScoreAnimation;
	private short mTrophiesEarned;

	private int mPointsBlingCounter = 2;

	private String mCurrenLevel = Level.mInfo.mCurrentLevelName.toUpperCase();
	public GameStateLevelCompleted(Game _gameScreen) {
		super(_gameScreen);

		SoundAssets.stop();

		mEndScoreAnimation = false;
		mInput = new InputLevelCompletedState(this);

		Player localPlayer = mGameWorld.getLocalPlayer();
		mPointsDisplayed = localPlayer.mPoints - mGameWorld.getLocalPlayer().mStartLevelPoints;

		// Adiciona pontos aos pontos totais
		Settings.addPlayerPoints(mPointsDisplayed);

		// atribui nova pontuação ao jogador pelo extra do tempo restante
		localPlayer.mPoints += mGameWorld.mClock.getRemainingSeconds() / 100;

		// verifica se pontuação actual é melhor que o highscore
		// e guarda já o valor antes que o utilizador salte a animação da
		// pontuação
		mHighScoreDisplayed = Level.mInfo.getHighScore();
		int newHighScore = mPointsDisplayed + mGameWorld.mClock.getRemainingSeconds() / 100 + 10;
		if (newHighScore >= mHighScoreDisplayed)
			Level.mInfo.setHighScore(newHighScore);

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

			if (mPointsBlingCounter++ > 1)
			{
				SoundAssets.playSound("bling");
				mPointsBlingCounter = 0;
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
		mBatcher.draw(GfxAssets.Pixmaps.getDarkGlass(), 0, 0);

		mBatcher.draw(GfxAssets.mScreens.get("levelcompleted"), 125, 60);

		BitmapFont font = GfxAssets.mBigFont;
		font.draw(mBatcher, mCurrenLevel, 350, 405);

		float valueX = findXPositionForValues();

		font = GfxAssets.mGenericFont;
		font.draw(mBatcher, Strings.mStrings.get("highscore"), START_X, 330);
		font.draw(mBatcher, mHighScoreDisplayed.toString(), valueX, 330);

		font.draw(mBatcher, Strings.mStrings.get("time"), START_X, 295);
		font.draw(mBatcher, mGameWorld.mClock.toString(), valueX, 295);

		font.draw(mBatcher, Strings.mStrings.get("final_score"), START_X, 260);
		font.draw(mBatcher, mPointsDisplayed.toString(), valueX, 260);

		for (int i = 0; i < mTrophiesEarned; i++)
		{
			mBatcher.draw(GfxAssets.mTrophy[0], 304 + i * 63, 180);

		}
	}

	private float findXPositionForValues()
	{
		float ret;
		BitmapFont font = GfxAssets.mGenericFont;

		String str = Strings.mStrings.get("highscore");
		ret = START_X + font.getBounds(str).width + INTERVAL_BETWEEN_TEXT_AND_VALUE;

		str = Strings.mStrings.get("time");
		if (ret < START_X + font.getBounds(str).width + INTERVAL_BETWEEN_TEXT_AND_VALUE)
		{
			ret = START_X + font.getBounds(str).width + INTERVAL_BETWEEN_TEXT_AND_VALUE;
		}

		str = Strings.mStrings.get("final_score");
		if (ret < START_X + font.getBounds(str).width + INTERVAL_BETWEEN_TEXT_AND_VALUE)
		{
			ret = START_X + font.getBounds(str).width + INTERVAL_BETWEEN_TEXT_AND_VALUE;
		}

		return ret;
	}

	@Override
	protected void onFinish()
	{
		mGameWorld.getLocalPlayer().mPoints = mGameWorld.getLocalPlayer().mStartLevelPoints;
		mGame.setGameState(mNextGameState);
	}

	@Override
	protected void onUpdateFinishing()
	{
	}
}