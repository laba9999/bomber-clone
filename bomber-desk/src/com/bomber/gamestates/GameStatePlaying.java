package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.Game;
import com.bomber.common.Achievements;
import com.bomber.common.assets.Assets;
import com.bomber.gameobjects.Player;
import com.bomber.gametypes.GameTypeCTF;
import com.bomber.gametypes.GameTypeCampaign;
import com.bomber.gametypes.GameTypeDeathmatch;
import com.bomber.input.InputPlayingState;
import com.bomber.world.Clock;

public class GameStatePlaying extends GameState {

	private static final int SECONDS_TO_START_BLINK_CLOCK = 15000;

	private short mClockBlinkInterval = 100;
	private short mTicksSinceLastClockBlink = 100;
	private boolean mPaintingClockRed = false;
	
	public long mStartingTime ;
	
	public GameStatePlaying(Game _gameScreen) {
		super(_gameScreen);
		mStartingTime = System.currentTimeMillis();
		mInput = new InputPlayingState(this);
	}

	@Override
	public void onUpdate()
	{
		mInput.update();
		mGameWorld.update();

		mTicksSinceLastClockBlink++;
		
		
		if (mGameWorld.mGameTypeHandler.isOver())
		{
			Achievements.mTotalTimePlayed += System.currentTimeMillis() - mStartingTime;
			Achievements.saveFile();
			if (mGameWorld.mGameTypeHandler instanceof GameTypeCampaign)
				mGame.setGameState(new GameStateLevelCompleted(mGame));
			else if (mGameWorld.mGameTypeHandler instanceof GameTypeDeathmatch)
				mGame.setGameState(new GameStateRoundEndDM(mGame));
			if (mGameWorld.mGameTypeHandler instanceof GameTypeCTF)
				mGame.setGameState(new GameStateRoundEndCTF(mGame));
			//else
				//throw new InvalidParameterException(" final de gamestate não definido!");
		} else if (mGameWorld.mGameTypeHandler.isLost())
		{
			Achievements.mTotalTimePlayed += System.currentTimeMillis() - mStartingTime;		
			Achievements.saveFile();
			mGame.setGameState(new GameStateGameOver(mGame));
		}
	}

	public void onPresent(float _interpolation)
	{
		// Renderiza o mundo
		mWorldRenderer.render();

		mBatcher.setProjectionMatrix(mUICamera.combined);

		// Cache
		BitmapFont font = Assets.mFont;
		Player player = mGameWorld.getLocalPlayer();

		// desenha imagem do controller
		mBatcher.draw(Assets.mScreens.get("controller"), 0, 0);

		// Relógio
		drawClock(font);

		// Pontos
		font.draw(mBatcher, "SCORE: " + player.getPointsAsString(), 365, 473);

		//
		// Bonus

		// Quantidade de bónus
		drawAcummulatedBonus(font, player);

		// Bónus activos
		drawActiveBonus(player);
	}

	private void drawAcummulatedBonus(BitmapFont _font, Player _player)
	{
		// desenha quantidades de bonus ao fundo
		Integer value;

		value = (int) _player.mLives;
		_font.draw(mBatcher, value.toString(), 312, 26);

		value = (int) _player.mBombExplosionSize;
		_font.draw(mBatcher, value.toString(), 380, 26);

		value = (int) _player.mMaxSimultaneousBombs;
		_font.draw(mBatcher, value.toString(), 453, 26);

		value = (int) _player.mSpeedFactor;
		_font.draw(mBatcher, value.toString(), 523, 26);
	}

	private void drawActiveBonus(Player _player)
	{
		// desenha bonus ao canto
		float x = 764;
		float y = 445;
		boolean drawBonusHand = _player.mIsAbleToPushBombs;
		boolean drawBonusShield = _player.mIsShieldActive;
		boolean drawBonusStar = _player.mPointsMultiplier != 1;

		if (drawBonusHand)
		{
			mBatcher.draw(Assets.mBonusIcons.get("hand"), x, y);
			x -= 57;
		}
		if (drawBonusShield)
		{
			mBatcher.draw(Assets.mBonusIcons.get("shield"), x, y);
			x -= 57;
		}
		if (drawBonusStar)
		{
			mBatcher.draw(Assets.mBonusIcons.get("star"), x, y);
			x -= 57;
		}
	}

	private void drawClock(BitmapFont _font)
	{
		mBatcher.setColor(1, 1, 1, 1);
		Clock clock = mGameWorld.mClock;
		if (clock.getRemainingSeconds() < SECONDS_TO_START_BLINK_CLOCK)
		{
			if (mTicksSinceLastClockBlink > mClockBlinkInterval)
			{
				mTicksSinceLastClockBlink = 0;
				mPaintingClockRed = !mPaintingClockRed;
			}

			if (mPaintingClockRed)
				_font.setColor(1, 0, 0, 1);
		}

		_font.draw(mBatcher, clock.toString(), 290, 473);
		_font.setColor(1, 1, 1, 1);
	}

	@Override
	protected void onFinish()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUpdateFinishing()
	{
		// TODO Auto-generated method stub

	}

}