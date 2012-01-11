package com.bomber.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bomber.gamestates.GameState;


public abstract class Input {
	protected Vector3 mTouchPoint;
	protected OrthographicCamera mUICamera;
	protected Rectangle[] mInputZones;
	protected GameState mGameState;
	public static boolean mIsUsingKeyboard = false;

	
	Input(GameState _gameState)
	{
		mUICamera = _gameState.mGame.mUICamera;
		mTouchPoint = new Vector3();
		mGameState = _gameState;
	}
	
	public final void update()
	{

			if(!parseTouchInput())
				parseKeyboardInput();
	}	
	
	public final Rectangle[] getZones()
	{
		return mInputZones;
	}
	
	protected abstract void parseKeyboardInput();
	protected abstract boolean parseTouchInput();
	protected abstract void parseInputZone(short _zone);

}