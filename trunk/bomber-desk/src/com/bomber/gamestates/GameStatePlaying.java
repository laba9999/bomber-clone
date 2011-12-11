package com.bomber.gamestates;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;

public class GameStatePlaying extends GameState {

	private static final short INPUT_LEFT = 0;
	private static final short INPUT_RIGHT = 1;
	private static final short INPUT_UP = 2;
	private static final short INPUT_DOWN = 3;
	private static final short INPUT_BOMB = 4;
	private static final short INPUT_PAUSE = 5;
	
	private static boolean mJustPlacedBomb = false;

	private Rectangle[] mInputZones = new Rectangle[6];
	
	public GameStatePlaying(GameScreen _gameScreen) {
		super(_gameScreen);
		
		
		//Inicializa as zonas de input para o android
		//mInputZones[INPUT_LEFT] = new Rectangle(0,0, )
	}

	public void update()
	{
		parseInput();
		mGameWorld.update();

	}

	public void parseInput()
	{

		if (Gdx.app.getType() != Application.ApplicationType.Android)
			parseKeyboardInput();
		else
			parseTouchInput();
	}

	private void parseTouchInput()
	{
		if (!Gdx.input.justTouched())
			return;

		mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		
	}

	private void parseKeyboardInput()
	{
		Player localPlayer = mGameWorld.getLocalPlayer();
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			localPlayer.moveLeft();

		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			localPlayer.moveRight();
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
			localPlayer.moveDown();
		else if (Gdx.input.isKeyPressed(Keys.UP))
			localPlayer.moveUp();
		else
			localPlayer.stop();

		if (Gdx.input.isKeyPressed(Keys.SPACE))
		{
			if (!mJustPlacedBomb)
			{
				localPlayer.dropBomb();
				mJustPlacedBomb = true;
			}
		} else
			mJustPlacedBomb = false;

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
		font.draw(mBatcher,"LEVEL 1", 10 , 470);
		
		//desenha tempo e score		
		if(mGameWorld.mClock != null)
		font.draw(mBatcher, mGameWorld.mClock.toString(), 235, 150);
		//TODO: mPointsAsString não é necessário??
		font.draw(mBatcher, "SCORE: " + player.getPointsAsString(), 350, 150);
		
		//desenha quantidades de bonus ao fundo
		Integer value;
		
		value = (int) player.mLives;
		font.draw(mBatcher, value.toString(), 240, 50);
		
		value = (int) player.mBombExplosionSize;
		font.draw(mBatcher, value.toString(), 340, 50);
		
		value = (int) player.mMaxSimultaneousBombs;
		font.draw(mBatcher, value.toString(), 440, 50);
	
		value = (int) player.mSpeed;
		font.draw(mBatcher, value.toString(), 540, 50);
		
		//desenha bonus ao canto
		float x = 755;
		float y = 437;		
		boolean drawBonusHand = player.mIsAbleToPushBombs;
		boolean drawBonusShield = player.mIsShieldActive;
		boolean drawBonusStar = player.mPointsMultiplier != 1;
		
		if(drawBonusHand)
		{
			mBatcher.draw(Assets.mBonus.get("bonus_hand").getKeyFrame(0, false), x, y);
			x -= 57;
		}
		if(drawBonusShield)
		{
			mBatcher.draw(Assets.mBonus.get("bonus_shield").getKeyFrame(0, false), x, y);
			x -= 57;
		}
		if(drawBonusStar)
		{
			mBatcher.draw(Assets.mBonus.get("bonus_star").getKeyFrame(0, false), x, y - 5);
			x -= 57;	
		}
		
		mBatcher.end();
		
		
	}


	
}