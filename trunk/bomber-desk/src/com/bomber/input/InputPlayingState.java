package com.bomber.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.common.Directions;
import com.bomber.gameobjects.Player;
import com.bomber.gamestates.GameState;
import com.bomber.gamestates.GameStatePaused;
import com.bomber.world.GameWorld;

public class InputPlayingState extends Input {

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
	final short directionsXWidth = 65;
	final short directionsXHeight = 60;
	final short directionsYWidth = 60;
	final short directionsYHeight = 65;
	final short cornerWidht = directionsXWidth - 2;
	final short cornerHeight = directionsYHeight - 2;

	private static boolean mJustPlacedBomb = false;

	private static int mDirectionsPointerIdx = -1;
	private static int mBombPointerIdx = -1;
	private static short mLastDirectionalInput = -1;

	private GameWorld mGameWorld;
	private Player mLocalPlayer;

	public InputPlayingState(GameState _gameState) {
		super(_gameState);

		// Inicializa as zonas de input para o android
		mInputZones = new Rectangle[10];

		mInputZones[INPUT_LEFT_DOWN] = new Rectangle(mOriginX, mOriginY, cornerWidht, cornerHeight);
		mInputZones[INPUT_DOWN] = new Rectangle(mOriginX + directionsXWidth, mOriginY, directionsYWidth, directionsYHeight);
		mInputZones[INPUT_RIGHT_DOWN] = new Rectangle(mInputZones[INPUT_DOWN].x + directionsYWidth + 2, mOriginY, cornerWidht, cornerHeight);

		mInputZones[INPUT_LEFT] = new Rectangle(mOriginX, mOriginY + directionsYHeight, directionsXWidth, directionsXHeight);
		mInputZones[INPUT_RIGHT] = new Rectangle(mInputZones[INPUT_DOWN].x + directionsYWidth, mInputZones[INPUT_DOWN].y + directionsYHeight, directionsXWidth, directionsXHeight);

		mInputZones[INPUT_LEFT_UP] = new Rectangle(mOriginX, mInputZones[INPUT_LEFT].y + directionsXHeight + 2, cornerWidht, cornerHeight);
		mInputZones[INPUT_UP] = new Rectangle(mOriginX + directionsXWidth, mInputZones[INPUT_LEFT].y + directionsXHeight, directionsYWidth, directionsYHeight);
		mInputZones[INPUT_RIGHT_UP] = new Rectangle(mInputZones[INPUT_RIGHT].x + 2, mInputZones[INPUT_UP].y + 2, cornerWidht, cornerHeight);

		mInputZones[INPUT_BOMB] = new Rectangle(640, 5, 150, 150);
		mInputZones[INPUT_PAUSE] = new Rectangle(10, 400, 80, 80);

		mGameWorld = _gameState.mGameScreen.mWorld;
		mLocalPlayer = mGameWorld.getLocalPlayer();
	}

	@Override
	protected void parseTouchInput()
	{
		short p = 0;
		while (Gdx.input.isTouched(p++))
		{
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(p - 1), Gdx.input.getY(p - 1), 0));

			if (mInputZones[INPUT_PAUSE].contains(mTouchPoint.x, mTouchPoint.y))
			{
				mGameState.mGameScreen.setGameState(new GameStatePaused(mGameState.mGameScreen));
				return;
			}

			if (mGameWorld.getLocalPlayer().mIsDead)
			{
				mDirectionsPointerIdx = -1;
				return;
			}

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

		}

		if (mDirectionsPointerIdx != -1 && !Gdx.input.isTouched(mDirectionsPointerIdx))
		{
			mGameWorld.getLocalPlayer().stop();
			mDirectionsPointerIdx = -1;
		}
		
		if (mBombPointerIdx != -1 && !Gdx.input.isTouched(mBombPointerIdx))
		{
			mJustPlacedBomb = false;
			mBombPointerIdx = -1;
		}
	}

	@Override
	protected void parseInputZone(short _zone)
	{
		switch (_zone)
		{
		case INPUT_LEFT:
			mLocalPlayer.changeDirection(Directions.LEFT);
			break;
		case INPUT_RIGHT:
			mLocalPlayer.changeDirection(Directions.RIGHT);
			break;
		case INPUT_UP:
			mLocalPlayer.changeDirection(Directions.UP);
			break;
		case INPUT_DOWN:
			mLocalPlayer.changeDirection(Directions.DOWN);
			break;

		case INPUT_LEFT_DOWN:
			if (mLastDirectionalInput == INPUT_DOWN)
				mLocalPlayer.changeDirection(Directions.LEFT);
			else
				mLocalPlayer.changeDirection(Directions.DOWN);
			break;

		case INPUT_RIGHT_DOWN:
			if (mLastDirectionalInput == INPUT_DOWN)
				mLocalPlayer.changeDirection(Directions.RIGHT);
			else
				mLocalPlayer.changeDirection(Directions.DOWN);
			break;

		case INPUT_RIGHT_UP:
			if (mLastDirectionalInput == INPUT_UP)
				mLocalPlayer.changeDirection(Directions.RIGHT);
			else
				mLocalPlayer.changeDirection(Directions.UP);
			break;

		case INPUT_LEFT_UP:
			if (mLastDirectionalInput == INPUT_UP)
				mLocalPlayer.changeDirection(Directions.LEFT);
			else
				mLocalPlayer.changeDirection(Directions.UP);
			break;

		}

		mLastDirectionalInput = _zone;
	}

	@Override
	protected void parseKeyboardInput()
	{
		if (Gdx.input.isKeyPressed(Keys.P))
			mGameState.mGameScreen.setGameState(new GameStatePaused(mGameState.mGameScreen));

		if (mGameWorld.getLocalPlayer().mIsDead)
			return;

		if (Gdx.input.isKeyPressed(Keys.LEFT))
			mLocalPlayer.changeDirection(Directions.LEFT);
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			mLocalPlayer.changeDirection(Directions.RIGHT);
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
			mLocalPlayer.changeDirection(Directions.DOWN);
		else if (Gdx.input.isKeyPressed(Keys.UP))
			mLocalPlayer.changeDirection(Directions.UP);
		else
			mLocalPlayer.stop();

		if (Gdx.input.isKeyPressed(Keys.SPACE))
		{
			if (!mJustPlacedBomb)
			{
				mLocalPlayer.dropBomb();
				mJustPlacedBomb = true;
			}
		} else
			mJustPlacedBomb = false;
	}

}
