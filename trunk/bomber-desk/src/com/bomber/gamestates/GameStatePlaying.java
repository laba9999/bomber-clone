package com.bomber.gamestates;

import java.awt.Rectangle;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
		
		mBatcher.begin();
		mBatcher.draw(Assets.mAtlas.findRegion("tiles_", 123), 50, 50, 50, 50);
		mBatcher.end();
	}

}