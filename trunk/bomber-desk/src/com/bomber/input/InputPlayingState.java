package com.bomber.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bomber.Game;
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

	
	private static final Vector2 mCenter = new Vector2(115, 100);
	private Vector2 mTouchingPointXY;
	private float mTouchingAngle;
	private GameWorld mGameWorld;
	private Player mLocalPlayer;

	public InputPlayingState(GameState _gameState) {
		super(_gameState);

		// Inicializa as zonas de input para o android
		mInputZones = new Rectangle[10];

		mInputZones[INPUT_BOMB] = new Rectangle(640, 5, 150, 150);
		mInputZones[INPUT_PAUSE] = new Rectangle(10, 400, 80, 80);

		mGameWorld = _gameState.mGame.mWorld;
		
		mTouchingPointXY = new Vector2();
	}

	@Override
	protected void parseTouchInput()
	{
		short p = 0;
		while (Gdx.input.isTouched(p++))
		{
			mUICamera.unproject(mTouchPoint.set(Gdx.input.getX(p - 1), Gdx.input.getY(p - 1), 0));

			if (!Game.mIsPVPGame)
			{
				if (mInputZones[INPUT_PAUSE].contains(mTouchPoint.x, mTouchPoint.y))
				{
					mGameState.mGame.setGameState(new GameStatePaused(mGameState.mGame));
					return;
				}
			}

			if (!mGameWorld.getLocalPlayer().mAcceptPlayerInput)
			{
				mDirectionsPointerIdx = -1;
				mJustPlacedBomb = false;
				mBombPointerIdx = -1;
				return;
			}
			
			if(mTouchPoint.x <= 400 && mTouchPoint.y <= 300)
			{ //limita a zona de movimento direccional
				
				mTouchingPointXY.set(mTouchPoint.x,mTouchPoint.y);
				mTouchingPointXY.sub(mCenter);			
				mTouchingAngle = mTouchingPointXY.angle();
				
				if(mTouchingAngle>=0 && mTouchingAngle < 45  || mTouchingAngle<=360 && mTouchingAngle> 315)
				{//RIGHT
	                mDirectionsPointerIdx = p - 1;
					parseInputZone(INPUT_RIGHT);
				}else if(mTouchingAngle>=45 && mTouchingAngle < 135)
				{//UP
	                mDirectionsPointerIdx = p - 1;
					parseInputZone(INPUT_UP);
				}else if(mTouchingAngle >= 135 && mTouchingAngle < 225)
				{//LEFT
	                mDirectionsPointerIdx = p - 1;
					parseInputZone(INPUT_LEFT);
				}else if(mTouchingAngle >= 225 && mTouchingAngle < 315)
				{//DOWN
	                mDirectionsPointerIdx = p - 1;
					parseInputZone(INPUT_DOWN);
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
		mLocalPlayer = mGameWorld.getLocalPlayer();

		if (mLocalPlayer == null)
			return;
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

		}

		mLastDirectionalInput = _zone;
	}

	@Override
	protected void parseKeyboardInput()
	{
		mLocalPlayer = mGameWorld.getLocalPlayer();
		if (mLocalPlayer == null)
			return;

		if (Gdx.input.isKeyPressed(Keys.P) && !Game.mIsPVPGame)
			mGameState.mGame.setGameState(new GameStatePaused(mGameState.mGame));

		if (!mLocalPlayer.mAcceptPlayerInput)
			return;

		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A) )
			mLocalPlayer.changeDirection(Directions.LEFT);
		else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))
			mLocalPlayer.changeDirection(Directions.RIGHT);
		else if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))
			mLocalPlayer.changeDirection(Directions.DOWN);
		else if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))
			mLocalPlayer.changeDirection(Directions.UP);
		else
			mLocalPlayer.stop();

		if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.BUTTON_X) || Gdx.input.isKeyPressed(Keys.BUTTON_CIRCLE)  )
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
