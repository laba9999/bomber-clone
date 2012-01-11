package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bomber.Game;
import com.bomber.common.Achievements;
import com.bomber.common.Strings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.common.assets.SoundAssets;
import com.bomber.gameobjects.Player;
import com.bomber.gametypes.GameTypeCTF;
import com.bomber.gametypes.GameTypeCampaign;
import com.bomber.gametypes.GameTypeDeathmatch;
import com.bomber.input.InputPlayingState;
import com.bomber.world.Clock;

public class GameStatePlaying extends GameState {

	private static final int SECONDS_TO_START_BLINK_CLOCK = 12500;

	private short mClockBlinkInterval = 100;
	private short mTicksSinceLastClockBlink = 100;
	private boolean mPaintingClockRed = false;

	public long mStartingTime;

	public GameStatePlaying(Game _gameScreen) {
		super(_gameScreen);
		mStartingTime = System.currentTimeMillis();
		mInput = new InputPlayingState(this);
	}

	@Override
	protected void onReset()
	{

		SoundAssets.playMusic(Game.mLevelToLoad, true, 1.0f);
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
			// else
			// throw new
			// InvalidParameterException(" final de gamestate não definido!");
		} else if (mGameWorld.mGameTypeHandler.isLost())
		{
			Achievements.mTotalTimePlayed += System.currentTimeMillis() - mStartingTime;
			Achievements.saveFile();
			mGame.setGameState(new GameStateGameOver(mGame));
		}
	}

	public void onPresent()
	{
		// Renderiza o mundo
		mWorldRenderer.render();

		mBatcher.setProjectionMatrix(mUICamera.combined);

		// Cache
		BitmapFont font = GfxAssets.mGenericFont;
		Player player = mGameWorld.getLocalPlayer();

		if (mGameWorld.getLocalPlayer().mAcceptPlayerInput)
		{
			// desenha imagem do d-pad
			mBatcher.draw(GfxAssets.mControlPad, 0, 0);

			// desenha imagem do botao para colocar bombas
			mBatcher.draw(GfxAssets.mButtonBomb, 650, 0);
		}

		if (!Game.mIsPVPGame)
			mBatcher.draw(GfxAssets.mButtonPause, 0, 400);

		// Relógio
		drawClock(font);

		// Pontos
		font.draw(mBatcher, Strings.mStrings.get("score") + player.getPointsAsString(), 365, 473);

		//
		// Bonus

		// Quantidade de bónus
		drawAcummulatedBonus(font, player);

		// Bónus activos
		drawActiveBonus(player);
		
		
		mBatcher.draw(GfxAssets.mTrophy[0], 100, 85,20,20);
	}

	private void drawAcummulatedBonus(BitmapFont _font, Player _player)
	{

		// desenha quantidades de bonus ao fundo
		mBatcher.draw(GfxAssets.mBonusBar, 250, 0);

		Integer value;

		value = (int) _player.mLives;
		_font.draw(mBatcher, value.toString(), 320, 26);

		value = (int) _player.mBombExplosionSize;
		_font.draw(mBatcher, value.toString(), 390, 26);

		value = (int) _player.mMaxSimultaneousBombs;
		_font.draw(mBatcher, value.toString(), 465, 26);

		value = (int) _player.mSpeedFactor;
		_font.draw(mBatcher, value.toString(), 535, 26);
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
			mBatcher.draw(GfxAssets.mBonusIcons.get("hand"), x, y);
			x -= 57;
		}
		if (drawBonusShield)
		{
			mBatcher.draw(GfxAssets.mBonusIcons.get("shield"), x, y);
			x -= 57;
		}
		if (drawBonusStar)
		{
			mBatcher.draw(GfxAssets.mBonusIcons.get("star"), x, y);
			x -= 57;
		}
	}

	private void drawClock(BitmapFont _font)
	{
		// desenha quantidades de bonus ao fundo
		mBatcher.draw(GfxAssets.mClockBar, 200, 435);

		mBatcher.setColor(1, 1, 1, 1);
		Clock clock = mGameWorld.mClock;
		if (clock.getRemainingSeconds() < SECONDS_TO_START_BLINK_CLOCK)
		{
			if (!SoundAssets.mLastMusicPlaying.equals("timeEnding"))
				SoundAssets.playMusic("timeEnding", false, 1.0f);

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
	}

	@Override
	protected void onUpdateFinishing()
	{
	}

}