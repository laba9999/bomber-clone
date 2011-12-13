package com.bomber.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bomber.gamestates.GameState;


public abstract class Input {
	protected Vector3 mTouchPoint;
	protected OrthographicCamera mUICamera;
	protected Rectangle[] mInputZones;
	protected GameState mGameState;
	
	Input(GameState _gameState)
	{
		mUICamera = _gameState.mGameScreen.mUICamera;
		mTouchPoint = new Vector3();
		mGameState = _gameState;
	}
	
	public final void update()
	{
		if (Gdx.app.getType() != Application.ApplicationType.Android)
			parseKeyboardInput();
		else
			parseTouchInput();
	}
	
	protected abstract void parseKeyboardInput();
	protected abstract void parseTouchInput();
	
	public final Rectangle[] getZones()
	{
		return mInputZones;
	}
}