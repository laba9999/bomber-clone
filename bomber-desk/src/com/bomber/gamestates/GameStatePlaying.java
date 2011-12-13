package com.bomber.gamestates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;
import com.bomber.gametypes.GameTypeCampaign;
import com.bomber.input.InputPlayingState;

public class GameStatePlaying extends GameState {

	public GameStatePlaying(GameScreen _gameScreen) {
		super(_gameScreen);

		mInput = new InputPlayingState(this);
	}

	public void update()
	{
		mInput.update();
		mGameWorld.update();
		
		if(mGameWorld.mGameType.isOver())
		{
			if(mGameWorld.mGameType instanceof GameTypeCampaign)
			{
				mGameScreen.setGameState(new GameStateLevelCompleted(mGameScreen));
			}
			
			//TODO : outros gametypes

		}
		else if(mGameWorld.mGameType.isLost())
		{
			mGameScreen.setGameState(new GameStateGameOver(mGameScreen));
		}

	}


	public void present(float _interpolation)
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);

	
		BitmapFont font = Assets.mFont;
		Player player = mGameWorld.getLocalPlayer();
	
		mBatcher.begin();	
		//desenha imagem do controller
		mBatcher.draw(Assets.mControllerBar,0,0);

		//desenha nivel ao canto
		font.draw(mBatcher,"PAUSE", 10 , 475);
		
		//desenha tempo e score		
		if(mGameWorld.mClock != null)
		font.draw(mBatcher, mGameWorld.mClock.toString(), 280, 475);
		//TODO: mPointsAsString não é necessário??
		font.draw(mBatcher, "SCORE: " + player.getPointsAsString(), 365, 475);
		
		//desenha quantidades de bonus ao fundo
		Integer value;
		
		value = (int) player.mLives;
		font.draw(mBatcher, value.toString(), 305, 30);
		
		value = (int) player.mBombExplosionSize;
		font.draw(mBatcher, value.toString(), 375, 30);
		
		value = (int) player.mMaxSimultaneousBombs;
		font.draw(mBatcher, value.toString(), 450, 30);
	
		value = (int) player.mSpeedFactor;
		font.draw(mBatcher, value.toString(), 520, 30);
		
		//desenha bonus ao canto
		float x = 764;
		float y = 445;		
		boolean drawBonusHand = player.mIsAbleToPushBombs;
		boolean drawBonusShield = player.mIsShieldActive;
		boolean drawBonusStar = player.mPointsMultiplier != 1;
		
		if(drawBonusHand)
		{
			mBatcher.draw(Assets.mBonusIcons.get("hand"), x, y);
			x -= 57;
		}
		if(drawBonusShield)
		{
			mBatcher.draw(Assets.mBonusIcons.get("shield"), x, y);
			x -= 57;
		}
		if(drawBonusStar)
		{
			mBatcher.draw(Assets.mBonusIcons.get("star"), x, y);
			x -= 57;	
		}
		
//		Rectangle[] zones = mInput.getZones();
//		for (int i = 0; i < zones.length; i++)
//			mBatcher.draw(Assets.mAtlas.findRegion("tiles_", 123), zones[i].x, zones[i].y, zones[i].width, zones[i].height);

		Integer fps = Gdx.graphics.getFramesPerSecond();
		Assets.mFont.draw(mBatcher, fps.toString(), 115, 470);
		Assets.mFont.draw(mBatcher, GameScreen.ticksPerSecond.toString(), 165, 470);

		
		mBatcher.end();

		
		
	}


	
}