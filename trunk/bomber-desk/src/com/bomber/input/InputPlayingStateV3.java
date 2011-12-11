package com.bomber.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.gameobjects.Player;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStatePaused;
import com.bomber.world.GameWorld;

public class InputPlayingStateV3 extends Input {

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

	private short mStartOriginX = 15;
	private short mStartOriginY = 15;

	private short mOriginX = mStartOriginX;
	private short mOriginY = mStartOriginY;
	final short directionsXWidth = 50;
	final short directionsXHeight = 30;
	final short directionsYWidth = 30;
	final short directionsYHeight = 50;

	final short cornersSpace = 1;
	final short cornerWidht = directionsXWidth - cornersSpace;
	final short cornerHeight = directionsYHeight - cornersSpace;

	private static boolean mJustPlacedBomb = false;

	private static int mDirectionsPointerIdx = -1;
	private static int mBombPointerIdx = -1;
	private static short mLastDirectionalInput = -1;

	private GameWorld mGameWorld;

	public InputPlayingStateV3(GameState _gameState) {
		super(_gameState);

		// Inicializa as zonas de input para o android
		mInputZones = new Rectangle[10];

		calcInputZonesForDPad();
		mInputZones[INPUT_BOMB] = new Rectangle(670, 20, 100, 100);
		mInputZones[INPUT_PAUSE] = new Rectangle(20, 410, 50, 50);

		mGameWorld = _gameState.mGameScreen.mWorld;
	}

	private void calcInputZonesForDPad()
	{
		mInputZones[INPUT_LEFT_DOWN] = new Rectangle(mOriginX, mOriginY, cornerWidht, cornerHeight);
		mInputZones[INPUT_DOWN] = new Rectangle(mOriginX + directionsXWidth, mOriginY, directionsYWidth, directionsYHeight);
		mInputZones[INPUT_RIGHT_DOWN] = new Rectangle(mInputZones[INPUT_DOWN].x + directionsYWidth + cornersSpace, mOriginY, cornerWidht, cornerHeight);

		mInputZones[INPUT_LEFT] = new Rectangle(mOriginX, mOriginY + directionsYHeight, directionsXWidth, directionsXHeight);
		mInputZones[INPUT_RIGHT] = new Rectangle(mInputZones[INPUT_DOWN].x + directionsYWidth, mInputZones[INPUT_DOWN].y + directionsYHeight, directionsXWidth, directionsXHeight);

		mInputZones[INPUT_LEFT_UP] = new Rectangle(mOriginX, mInputZones[INPUT_LEFT].y + directionsXHeight + cornersSpace, cornerWidht, cornerHeight);
		mInputZones[INPUT_UP] = new Rectangle(mOriginX + directionsXWidth, mInputZones[INPUT_LEFT].y + directionsXHeight, directionsYWidth, directionsYHeight);
		mInputZones[INPUT_RIGHT_UP] = new Rectangle(mInputZones[INPUT_RIGHT].x + cornersSpace, mInputZones[INPUT_UP].y + cornersSpace, cornerWidht, cornerHeight);
	}

	@Override
	protected void parseTouchInput()
	{
		short p = 0;
		while (Gdx.input.isTouched(p++))
		{
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(p - 1), Gdx.input.getY(p - 1), 0));

			if (mDirectionsPointerIdx == -1)
			{
				for (short i = INPUT_LEFT; i <= INPUT_LEFT_UP; i++)
				{
					if (mInputZones[i].contains(mTouchPoint.x, mTouchPoint.y))
					{
						mDirectionsPointerIdx = p - 1;
						parseInputZone(i);
						break;
					}
				}
			} else if (mDirectionsPointerIdx == p - 1)
			{
				float deltaX = java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
				float deltaY = java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));

				if (deltaX > 5 || deltaY > 5)
				{

					if (deltaX > deltaY)
					{
						deltaX = Gdx.input.getDeltaX(mDirectionsPointerIdx);
						if (deltaX < 0)
							parseInputZone(INPUT_LEFT);
						else
							parseInputZone(INPUT_RIGHT);
					} else
					{
						deltaY = Gdx.input.getDeltaY(mDirectionsPointerIdx);
						if (deltaY < 0)
							parseInputZone(INPUT_UP);
						else
							parseInputZone(INPUT_DOWN);
					}
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
			mOriginX = mStartOriginX;
			mOriginY = mStartOriginY;
			calcInputZonesForDPad();
		}
		if (mDirectionsPointerIdx != -1)
		{

			float deltaX = java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			float deltaY = java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));

			float centerX = mOriginX + (((mInputZones[INPUT_RIGHT].x + directionsXWidth) - mOriginX) / 2);
			float centerY = mOriginY + (((mInputZones[INPUT_UP].y + directionsYHeight) - mOriginY) / 2);

			float centerDistX = mTouchPoint.x - centerX;
			float centerDistY = mTouchPoint.y - centerY;

			if (centerDistX < 1 && centerDistY < 1)
				return;

			mOriginX += centerDistX;
			mOriginY += centerDistY;

			if (mOriginX < 0)
				mOriginX = 0;

			if (mOriginY < 0)
				mOriginY = 0;

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
			mLastDirectionalInput = INPUT_LEFT;
			break;
		case INPUT_RIGHT:
			mGameWorld.getLocalPlayer().moveRight();
			mLastDirectionalInput = INPUT_RIGHT;
			break;
		case INPUT_UP:
			mGameWorld.getLocalPlayer().moveUp();
			mLastDirectionalInput = INPUT_UP;
			break;
		case INPUT_DOWN:
			mGameWorld.getLocalPlayer().moveDown();
			mLastDirectionalInput = INPUT_DOWN;
			break;

		case INPUT_LEFT_DOWN:
			// deltaX =
			// java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			// deltaY =
			// java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));
			//
			// if (deltaX + deltaY < 1)
			// return;
			//
			// if (deltaX > deltaY){
			if (mLastDirectionalInput == INPUT_DOWN)
			{
				mGameWorld.getLocalPlayer().moveLeft();
				mLastDirectionalInput = INPUT_LEFT;
			} else
			{
				mGameWorld.getLocalPlayer().moveDown();
				mLastDirectionalInput = INPUT_DOWN;
			}
			break;

		case INPUT_RIGHT_DOWN:
			// deltaX =
			// java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			// deltaY =
			// java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));
			// if (deltaX + deltaY < 1)
			// return;
			// if (deltaX > deltaY)
			if (mLastDirectionalInput == INPUT_DOWN)
			{
				mGameWorld.getLocalPlayer().moveRight();
				mLastDirectionalInput = INPUT_RIGHT;
			} else
			{
				mGameWorld.getLocalPlayer().moveDown();
				mLastDirectionalInput = INPUT_DOWN;
			}
			break;

		case INPUT_RIGHT_UP:

			// deltaX =
			// java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			// deltaY =
			// java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));
			// if (deltaX + deltaY < 1)
			// return;
			// if (deltaX > deltaY)
			if (mLastDirectionalInput == INPUT_UP)
			{
				mGameWorld.getLocalPlayer().moveRight();
				mLastDirectionalInput = INPUT_RIGHT;
			} else
			{
				mGameWorld.getLocalPlayer().moveUp();
				mLastDirectionalInput = INPUT_UP;
			}
			break;

		case INPUT_LEFT_UP:
			// deltaX =
			// java.lang.Math.abs(Gdx.input.getDeltaX(mDirectionsPointerIdx));
			// deltaY =
			// java.lang.Math.abs(Gdx.input.getDeltaY(mDirectionsPointerIdx));
			// if (deltaX + deltaY < 1)
			// return;
			// if (deltaX > deltaY)
			if (mLastDirectionalInput == INPUT_UP)
			{
				mGameWorld.getLocalPlayer().moveLeft();
				mLastDirectionalInput = INPUT_LEFT;
			} else
			{
				mGameWorld.getLocalPlayer().moveUp();
				mLastDirectionalInput = INPUT_UP;
			}
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

		float upMiddleX;
		/*
		 * switch (mGameWorld.getLocalPlayer().mDirection) { case Directions.UP:
		 * upMiddleX = mInputZones[INPUT_UP].x + directionsYWidth / 2; deltaX =
		 * mTouchPoint.x - upMiddleX; if (java.lang.Math.abs(deltaX) > 1)
		 * mOriginX += deltaX; break; }
		 */

		// if (_zone == INPUT_LEFT_DOWN || _zone == INPUT_RIGHT_DOWN || _zone ==
		// INPUT_LEFT_UP || _zone == INPUT_RIGHT_UP)
		// {

		// }
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
