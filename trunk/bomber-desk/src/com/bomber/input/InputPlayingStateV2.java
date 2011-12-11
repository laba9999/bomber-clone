package com.bomber.input;

import java.awt.PageAttributes.OriginType;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.common.Directions;
import com.bomber.gameobjects.Player;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStatePaused;
import com.bomber.world.GameWorld;

public class InputPlayingStateV2 extends Input {

	private static final short INPUT_LEFT = 0;
	private static final short INPUT_RIGHT = 1;
	private static final short INPUT_UP = 2;
	private static final short INPUT_DOWN = 3;
	private static final short INPUT_LEFT_DOWN = 4;
	private static final short INPUT_RIGHT_DOWN = 5;
	private static final short INPUT_RIGHT_UP = 6;
	private static final short INPUT_LEFT_UP = 7;
	private static final short INPUT_BOMB = 8;
	private static final short INPUT_PAUSE = 9;

	private short mOriginX = 0;
	private short mOriginY = 0;
	final short directionsXWidth = 50;
	final short directionsXHeight = 60;
	final short directionsYWidth = 60;
	final short directionsYHeight = 50;
	final short cornerWidht = directionsXWidth - 2;
	final short cornerHeight = directionsYHeight - 2;

	private static boolean mJustPlacedBomb = false;

	private static int mDirectionsPointerIdx = -1;
	private static int mBombPointerIdx = -1;
	private static short mLastDirectionalInput = -1;

	private GameWorld mGameWorld;

	public InputPlayingStateV2(GameState _gameState) {
		super(_gameState);

		// Inicializa as zonas de input para o android
		mInputZones = new Rectangle[10];

		calcInputZonesForDPad();
		mInputZones[INPUT_BOMB] = new Rectangle(640, 5, 150, 150);
		mInputZones[INPUT_PAUSE] = new Rectangle(20, 410, 50, 50);

		mGameWorld = _gameState.mGameScreen.mWorld;
	}

	private void calcInputZonesForDPad()
	{
		mInputZones[INPUT_LEFT_DOWN] = new Rectangle(mOriginX, mOriginY, cornerWidht, cornerHeight);
		mInputZones[INPUT_DOWN] = new Rectangle(mOriginX + directionsXWidth, mOriginY, directionsYWidth, directionsYHeight);
		mInputZones[INPUT_RIGHT_DOWN] = new Rectangle(mInputZones[INPUT_DOWN].x + directionsYWidth + 2, mOriginY, cornerWidht, cornerHeight);

		mInputZones[INPUT_LEFT] = new Rectangle(mOriginX, mOriginY + directionsYHeight, directionsXWidth, directionsXHeight);
		mInputZones[INPUT_RIGHT] = new Rectangle(mInputZones[INPUT_DOWN].x + directionsYWidth, mInputZones[INPUT_DOWN].y + directionsYHeight, directionsXWidth, directionsXHeight);

		mInputZones[INPUT_LEFT_UP] = new Rectangle(mOriginX, mInputZones[INPUT_LEFT].y + directionsXHeight + 2, cornerWidht, cornerHeight);
		mInputZones[INPUT_UP] = new Rectangle(mOriginX + directionsXWidth, mInputZones[INPUT_LEFT].y + directionsXHeight, directionsYWidth, directionsYHeight);
		mInputZones[INPUT_RIGHT_UP] = new Rectangle(mInputZones[INPUT_RIGHT].x + 2, mInputZones[INPUT_UP].y + 2, cornerWidht, cornerHeight);
	}

	@Override
	protected void parseTouchInput()
	{
		short p = 0;
		while (Gdx.input.isTouched(p++))
		{
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(p - 1), Gdx.input.getY(p - 1), 0));

			for (short i = INPUT_LEFT; i <= INPUT_LEFT_UP; i++)
			{
				if (mInputZones[i].contains(mTouchPoint.x, mTouchPoint.y))
				{
					mDirectionsPointerIdx = p - 1;
					parseInputZone(i);
					break;
				}
			}

			if (!mJustPlacedBomb && mInputZones[INPUT_BOMB].contains(mTouchPoint.x, mTouchPoint.y))
			{
				mJustPlacedBomb = true;
				mBombPointerIdx = p - 1;
				mGameWorld.getLocalPlayer().dropBomb();
			}

			if (mInputZones[INPUT_PAUSE].contains(mTouchPoint.x, mTouchPoint.y))
			{
				mGameState.mGameScreen.setGameState(new GameStatePaused(mGameState.mGameScreen));
				return;
			}
		}

		if (mDirectionsPointerIdx != -1 && !Gdx.input.isTouched(mDirectionsPointerIdx))
		{
			mGameWorld.getLocalPlayer().stop();
			mDirectionsPointerIdx = -1;
			mOriginX =0;
			mOriginY =0;
			calcInputZonesForDPad();
		}

		if (mBombPointerIdx != -1 && !Gdx.input.isTouched(mBombPointerIdx))
		{
			mJustPlacedBomb = false;
			mBombPointerIdx = -1;
		}
	}

	private void parseInputZone(short _zone)
	{
		float deltaX;
		float deltaY;
		switch (_zone)
		{
		case INPUT_LEFT:
			mGameWorld.getLocalPlayer().moveLeft();
			break;
		case INPUT_RIGHT:
			mGameWorld.getLocalPlayer().moveRight();
			break;
		case INPUT_UP:
			mGameWorld.getLocalPlayer().moveUp();
			break;
		case INPUT_DOWN:
			mGameWorld.getLocalPlayer().moveDown();
			break;

		case INPUT_LEFT_DOWN:
			deltaX = java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			deltaY = java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));

			if (deltaX + deltaY < 1)
				return;

			//if (deltaX > deltaY)
				 if (mLastDirectionalInput == INPUT_DOWN)
				mGameWorld.getLocalPlayer().moveLeft();
			else
				mGameWorld.getLocalPlayer().moveDown();
			break;

		case INPUT_RIGHT_DOWN:
			deltaX = java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			deltaY = java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));
			if (deltaX + deltaY < 1)
				return;
			//if (deltaX > deltaY)
				 if (mLastDirectionalInput == INPUT_DOWN)
				mGameWorld.getLocalPlayer().moveRight();
			else
				mGameWorld.getLocalPlayer().moveDown();
			break;

		case INPUT_RIGHT_UP:

			deltaX = java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			deltaY = java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));
			if (deltaX + deltaY < 5)
				return;
			if (deltaX > deltaY)
				//if (mLastDirectionalInput == INPUT_UP)
				mGameWorld.getLocalPlayer().moveRight();
			else
				mGameWorld.getLocalPlayer().moveUp();
			break;

		case INPUT_LEFT_UP:
			deltaX = java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			deltaY = java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));
			if (deltaX + deltaY < 5)
				return;
			if (deltaX > deltaY)
				// if (mLastDirectionalInput == INPUT_UP)
				mGameWorld.getLocalPlayer().moveLeft();
			else
				mGameWorld.getLocalPlayer().moveUp();
			break;
		/*
		 * default: deltaX = java.lang.Math.abs(Gdx.input.getDeltaX()); deltaY =
		 * java.lang.Math.abs(Gdx.input.getDeltaY());
		 * 
		 * if(deltaX > deltaY) { if(Gdx.input.getDeltaX() > 0)
		 * mGameWorld.getLocalPlayer().moveRight(); else
		 * mGameWorld.getLocalPlayer().moveLeft(); }else {
		 * if(Gdx.input.getDeltaY() > 0) mGameWorld.getLocalPlayer().moveDown();
		 * else mGameWorld.getLocalPlayer().moveUp(); }
		 */
		}

		mLastDirectionalInput = _zone;

		float upMiddleX;
		/*
		 * switch (mGameWorld.getLocalPlayer().mDirection) { case Directions.UP:
		 * upMiddleX = mInputZones[INPUT_UP].x + directionsYWidth / 2; deltaX =
		 * mTouchPoint.x - upMiddleX; if (java.lang.Math.abs(deltaX) > 1)
		 * mOriginX += deltaX; break; }
		 */
/*
	//	if (_zone == INPUT_LEFT_DOWN || _zone == INPUT_RIGHT_DOWN || _zone == INPUT_LEFT_UP || _zone == INPUT_RIGHT_UP)
		//{
			float centerX = mOriginX + (((mInputZones[INPUT_RIGHT].x + directionsXWidth) - mOriginX) / 2);
			float centerY = mOriginY + (((mInputZones[INPUT_UP].y + directionsYHeight) - mOriginY) / 2);

			float centerDistX = mTouchPoint.x - centerX;
			float centerDistY = mTouchPoint.y - centerY;

			if ( centerDistX<2 && centerDistY <2)
				return;
			
			mOriginX += centerDistX;
			mOriginY += centerDistY;
			
			if( mOriginX < 0)
				mOriginX = 0;
			
			if( mOriginY < 0)
				mOriginY = 0;
			
			calcInputZonesForDPad();*/
		//}
	}

	@Override
	protected void parseKeyboardInput()
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

		if (Gdx.input.isKeyPressed(Keys.P))
			mGameState.mGameScreen.setGameState(new GameStatePaused(mGameState.mGameScreen));

	}

}
