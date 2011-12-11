package com.bomber.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
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

	private static boolean mJustPlacedBomb = false;

	private static int mDirectionsPointerIdx = -1;
	private static int mBombPointerIdx = -1;
	private static short mLastDirectionalInput = -1;

	private GameWorld mGameWorld;

	public InputPlayingState(GameState _gameState) {
		super(_gameState);

		// Inicializa as zonas de input para o android
		mInputZones = new Rectangle[10];
		mInputZones[INPUT_LEFT] = new Rectangle(0, 50, 45, 40);
		mInputZones[INPUT_RIGHT] = new Rectangle(95, 50, 60, 40);
		mInputZones[INPUT_UP] = new Rectangle(50, 95, 40, 45);
		mInputZones[INPUT_DOWN] = new Rectangle(50, 0, 40, 45);
		mInputZones[INPUT_LEFT_DOWN] = new Rectangle(0, 0, 48, 48);
		mInputZones[INPUT_RIGHT_DOWN] = new Rectangle(92, 0, 60, 48);
		mInputZones[INPUT_RIGHT_UP] = new Rectangle(92, 92, 60, 48);
		mInputZones[INPUT_LEFT_UP] = new Rectangle(0, 92, 48, 48);
		mInputZones[INPUT_BOMB] = new Rectangle(670, 20, 100, 100);
		mInputZones[INPUT_PAUSE] = new Rectangle(20, 410, 50, 50);

		mGameWorld = _gameState.mGameScreen.mWorld;
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
					parseInputZone(i);
					mDirectionsPointerIdx = p - 1;
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
		}

		if (mBombPointerIdx != -1 && !Gdx.input.isTouched(mBombPointerIdx))
		{
			mJustPlacedBomb = false;
			mBombPointerIdx = -1;
		}
	}

	private void parseInputZone(short _zone)
	{
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
			if (mLastDirectionalInput == INPUT_DOWN)
				mGameWorld.getLocalPlayer().moveLeft();
			else
				mGameWorld.getLocalPlayer().moveDown();
			break;

		case INPUT_RIGHT_DOWN:
			if (mLastDirectionalInput == INPUT_DOWN)
				mGameWorld.getLocalPlayer().moveRight();
			else
				mGameWorld.getLocalPlayer().moveDown();
			break;

		case INPUT_RIGHT_UP:
			if (mLastDirectionalInput == INPUT_UP)
				mGameWorld.getLocalPlayer().moveRight();
			else
				mGameWorld.getLocalPlayer().moveUp();
			break;

		case INPUT_LEFT_UP:
			if (mLastDirectionalInput == INPUT_UP)
				mGameWorld.getLocalPlayer().moveLeft();
			else
				mGameWorld.getLocalPlayer().moveUp();
			break;
		}

		mLastDirectionalInput = _zone;
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
